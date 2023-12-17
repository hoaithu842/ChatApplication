package model;

import controller.ServerManagementController;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hoaithu842
 */
public class ServerManagementModel {
    private HashMap<String, String> userPassword;
    final private int DEFAULT_PORT = 8080;
    ServerSocket ss;
    private int port;
    ClientManager clientManager;
    ServerManagementController theController;
    
    public ServerManagementModel() {
        userPassword = new HashMap<>();
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
    public void setController(ServerManagementController theController) {
        this.theController = theController;
    }
    // Methods for authorization
    void openPortToAuthorize() {
        try{
            ServerSocket ss = new ServerSocket(DEFAULT_PORT);
            AuthorizingThread at = new AuthorizingThread(ss);
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
                                sendMsg.add("true");
                            }
                        }
                            
                        oos.writeObject(sendMsg);
                        oos.flush();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("There's an error: " + e.getMessage());
            }
        }
    }
    // Methods for server to talk to connected client
    class TalkingThread extends Thread {
        BufferedReader br;
        BufferedWriter bw;
        ObjectInputStream ois;
        ObjectOutputStream oos;
        TalkingThread(BufferedReader br, BufferedWriter bw, ObjectInputStream ois, ObjectOutputStream oos) {
            this.br = br;
            this.bw = bw;
            this.ois = ois;
            this.oos = oos;
        }
        @Override
        public void run() {
/*            do {
                String receivedMessage = "";
                do {
                    try {
                        BufferedReader br=new BufferedReader(new InputStreamReader(clientInfo.is));
                        receivedMessage=br.readLine();
                    } catch (IOException ex) {
                        System.out.println("Some error occured!");
                        // Bao la user da bi mat ket noi, reload lai cai bang connection
                        return;
                    }
                    System.out.println("Received from " + clientInfo.username + ": " + receivedMessage);
                    if (receivedMessage.equalsIgnoreCase("quit")) {
                        System.out.println("Client has left !");
                        // Bao la user da bi mat ket noi, reload lai cai bang connection
                        break;
                    }
                } while (true);
                // tt_ci.bw.close();
                // tt_ci.br.close();
            } while (true);
//            System.out.println("Exiting child thread.");
*/
        }
    }
    // Methods for server to listen to client connection
    class ConnectingThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try (
                        Socket socket = ss.accept(); //synchronous
                        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        ) {
                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();
                    
                    // receive username
                    String username = br.readLine();
                    ClientInformation clientInfo = new ClientInformation(is, os, socket, username);
                    clientManager.addClientInformation(clientInfo);
                    theController.reloadConnectionTree();
                    theController.displayMessage("Client " + username + " connected!");
                    
                    // publish data to render UI
                    ArrayList<String> onlineUsers = (ArrayList<String>)clientManager.getClientUsernameList();
                    onlineUsers.remove(username);
                    
                    
                    oos.writeObject(onlineUsers);
                    oos.flush();
//                    oos.close();
                    
                    TalkingThread tt = new TalkingThread(br, bw, ois, oos);
                    tt.start();
                } catch (IOException e) {
                    System.out.println("There's an error: " + e.getMessage());
                }
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
            ServerSocket ss = new ServerSocket(port);
            
            this.ss = ss;
            this.port = port;
                        
            ConnectingThread ct = new ConnectingThread();
            ct.start();
            
            theController.reloadConnectionTree();
            theController.displayMessage("Server Started!");
        } catch (IOException e) {
            theController.displayMessage("Unavailable Port!");
        }
    }
}
