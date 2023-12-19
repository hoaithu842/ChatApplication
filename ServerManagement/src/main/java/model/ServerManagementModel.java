package model;

import controller.ServerManagementController;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import model.component.MessageModel;

/**
 *
 * @author hoaithu842
 */
public class ServerManagementModel {
    private final HashMap<String, String> userPassword;
    private final HashMap<String, ClientInformation> userInformation;
    
    final private int DEFAULT_PORT = 8080;
    private int port;
    ClientManager clientManager;
    ServerManagementController theController;
    
    final String PRIVATE_MESSAGE = "private_message";
    public ServerManagementModel() {
        userPassword = new HashMap<>();
        userInformation = new HashMap<>();
        
        port = DEFAULT_PORT;
        clientManager = new ClientManager();
        openPortToAuthorize();
    }
    // Getters
    public ClientManager getClientManager() {
        return clientManager;
    }
    public int getPort() {
        return port;
    }
    // Setters
    public void refreshClientManager() {
        clientManager = new ClientManager();
    }
    public void setController(ServerManagementController theController) {
        this.theController = theController;
    }
    // Methods for authorization
    final void openPortToAuthorize() {
        try{
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            AuthorizingThread at = new AuthorizingThread(serverSocket);
            at.start();            
        } catch (IOException e) {
            theController.displayMessage("There're some error!");
        }
    }
    String validUsername(String username) {
        return userPassword.containsKey(username) ? "true" : "false";
    }
    
    String validPassword(String username, String password) {
        return userPassword.get(username).equals(password) ? "true" : "false";
    }
    class AuthorizingThread extends Thread {
        ServerSocket ss;
        AuthorizingThread(ServerSocket ss) {
            this.ss = ss;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    Socket socket = ss.accept(); // synchronous

                    try (
                            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())
                    ) {
                        ArrayList<String> receivedMsg = (ArrayList<String>) ois.readObject();
                        String username = receivedMsg.get(0);
                        String password = receivedMsg.get(1);
                        String method = receivedMsg.get(2);

                        ArrayList<String> sendMsg = new ArrayList<>();
                        
                        if (validUsername(username).equals("true")) {
                            if (method.equals("login")) {
                                sendMsg.add("true");
                                sendMsg.add(validPassword(username, password));
                            } else if (method.equals("signup")) {
                                sendMsg.add("false");
                            }
                            
                        } else {
                            if (method.equals("login")) {
                                sendMsg.add("false");
                                sendMsg.add("false");
                            } else if (method.equals("signup")) {
                                userPassword.put(username, password);
                                userInformation.put(username, new ClientInformation(username));
                                sendMsg.add("true");
                            }
                        }
                            
                        oos.writeObject(sendMsg);
                        oos.flush();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("There's an error: " + e.getMessage());
            } finally {
                try {
                    ss.close();
                } catch (IOException e) {
                    System.out.println("There's some error: " + e.getMessage());
                }
            }
        }
    }
    // Methods for server to talk to connected client
    class TalkingThread extends Thread {
        Socket socket;
        BufferedReader br;
        BufferedWriter bw;
        ObjectInputStream ois;
        ObjectOutputStream oos;

        TalkingThread(Socket socket, BufferedReader br, BufferedWriter bw, ObjectInputStream ois, ObjectOutputStream oos) {
            this.socket = socket;
            this.br = br;
            this.bw = bw;
            this.ois = ois;
            this.oos = oos;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    if (socket.isClosed()) {
                        System.out.println("Connection lost!");
                        break;
                    }
                    String code = br.readLine();
                    System.out.println("Receive code: " + code);
                    switch (code) {
                        case PRIVATE_MESSAGE -> {
                            MessageModel msgModel = (MessageModel) ois.readObject();
                            System.out.println("\t Content from " + msgModel.getFrom() + " to " + msgModel.getTo() + ": " + msgModel.getContent());
                            this.sendPrivateMessage(msgModel);
                        }
                        case "CODE2" -> {
                            continue;
                        }
                        default -> {
                            continue;
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("There's an error: " + e.getMessage());           
                }
            }
        }
        public void sendPrivateMessage(MessageModel msgModel) {
            try {
                String from = msgModel.getFrom();
                String to = msgModel.getTo();
                if (userInformation.containsKey(to)) {
                    // if receiver is a user - add to history
                    System.out.println("Sending back to sender to render");
                    synchronized (socket) {
                        bw.write(PRIVATE_MESSAGE);
                        bw.newLine();
                        bw.flush();
                        oos.writeObject(msgModel);
                        oos.flush();
                        
                        userInformation.get(from).updateChat(to, msgModel);
                        userInformation.get(to).updateChat(from, msgModel);

                        if (clientManager.containsClient(to)) {
                            // if is an online user - send and add to history
                            System.out.println("Sending message to " + to);
                            clientManager.bw(to).write(PRIVATE_MESSAGE);
                            clientManager.bw(to).newLine();
                            clientManager.bw(to).flush();
                            clientManager.oos(to).writeObject(msgModel);
                            clientManager.oos(to).flush();
                        }
                    }
                    
                    

                } else {
                    // Tao code cho Notification
                }
            } catch (IOException e) {
                System.out.println("There's some error: " + e.getMessage());
            }
        }
    }
    // Methods for server to listen to client connection
    class ConnectingThread extends Thread {
        ServerSocket ss;
        ConnectingThread(ServerSocket ss) {
            this.ss = ss;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    Socket socket = ss.accept(); //synchronous
                    
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    
                    // receive username
                    String username = br.readLine();
                    ClientInformation clientInfo;
                    if (userInformation.containsKey(username)) {
                        clientInfo = userInformation.get(username);
                    } else {
                        clientInfo = new ClientInformation(username);
                    }
                    clientManager.addClientInformation(clientInfo);
                    clientManager.addClientStream(username, br, bw, ois, oos);
                    theController.reloadConnectionTree();
                    
                    // publish data to render UI
                    ArrayList<String> onlineUsers = (ArrayList<String>)clientManager.getClientUsernameList();
                    onlineUsers.remove(username);
                    
                    oos.writeObject(onlineUsers);
                    oos.flush();

                    new TalkingThread(socket, br, bw, ois, oos).start();
                }
            } catch (IOException e) {
                System.out.println("There's an error: " + e.getMessage());
            }
        }
    }
    // Methods to start server
    public void createConnection(int port) {
        if (port==DEFAULT_PORT) {
            theController.displayMessage("Unavailable port!");
            return;
        }
        if (this.port!=DEFAULT_PORT) {
            theController.displayMessage("Server already started on port " + this.port + "!");
            return;
        }
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            this.port = port;
                        
            new ConnectingThread(serverSocket).start();
            
            theController.reloadConnectionTree();
            theController.displayMessage("Server Started!");
        } catch (IOException e) {
            theController.displayMessage("Unavailable Port!");
        }
    }
}
