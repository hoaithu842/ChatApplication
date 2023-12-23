package model;

import controller.ServerManagementController;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import model.component.GroupInformation;
import model.component.MessageModel;
import model.component.UserInformation;
import model.component.SocketPackage;

/**
 *
 * @author hoaithu842
 */
public class ServerManagementModel {
    private final HashMap<String, UserInformation> userData;
    private final HashMap<Integer, GroupInformation> groupData;
    ClientManager clientManager;

    final private int DEFAULT_PORT = 8080;
    private int port;
    ServerManagementController theController;
    
    final int PRIVATE_MESSAGE = 1;
    final int UPDATE_ONLINE = 2;
    final int TOTAL_USERS = 3;
    final int CREATE_GROUP = 4;
    final int GROUP_MESSAGE = 5;
    
    public ServerManagementModel() {
        port = DEFAULT_PORT;
        userData = new HashMap<>();
        groupData = new HashMap<>();
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
        return userData.containsKey(username) ? "true" : "false";
    }
    
    String validPassword(String username, String password) {
        return userData.get(username).passwordMatching(password) ? "true" : "false";
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
                                userData.put(username, new UserInformation(username, password));
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
    // Methods for server to listen to connected client
    class ListeningThread extends Thread {
        String username;
        Socket socket;

        ListeningThread(String username, Socket socket) {
            this.username = username;
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    if (socket.isClosed()) {
                        System.out.println("Connection lost!");
                        break;
                    }
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    SocketPackage pkg = (SocketPackage) ois.readObject();

                    switch (pkg.getCode()) {
                        case PRIVATE_MESSAGE -> {
                            MessageModel msgModel = pkg.getMessageModel();

                            String from = msgModel.getFrom();
                            String to = msgModel.getTo();

                            if (userData.containsKey(to)) {
                                userData.get(from).updateChat(to, msgModel);
                                userData.get(to).updateChat(from, msgModel);

                                for (Socket item : clientManager.getReceiverSockets(from, to)) {
                                    ObjectOutputStream oos = new ObjectOutputStream(item.getOutputStream());
                                    oos.writeObject(pkg);
                                    oos.flush();
                                }
                            } else {
                                continue; //notify
                            }
                        }
                        case TOTAL_USERS -> {
                            ArrayList<String> totalUsers = new ArrayList<>(userData.keySet());
                            pkg.setTotalUsers(totalUsers);
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                            oos.writeObject(pkg);
                            oos.flush();
                        }
                        case CREATE_GROUP -> {
                            String groupName = pkg.getNewOnlineUser(); // String obj represents groupName
                            ArrayList<String> groupMembers = pkg.getTotalUsers(); // Same
                            
                            int ID = groupData.size() + 1;
                            groupData.put(ID, new GroupInformation(ID, groupName, groupMembers));
                            
                            pkg.setGroupID(ID);
                            pkg.setNewGroup(groupData.get(ID));
                            
                            for (String member : groupMembers) {
                                userData.get(member).createGroup(ID, groupData.get(ID));
                                if (clientManager.containsClient(member)) {
                                    Socket socket = clientManager.getReceiver(member);
                                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                                    
                                    oos.writeObject(pkg);
                                    oos.flush();

                                    System.out.println("Sent createGroup!");
                                }
                            }
                        }
                        case GROUP_MESSAGE -> {
                            MessageModel msgModel = pkg.getMessageModel();
                            
                            String from = msgModel.getFrom();
                            int to = Integer.parseInt(msgModel.getTo());
                            System.out.println("\t Content from " + from + " to group " + to + ": " + msgModel.getContent());
                            
                            if (groupData.containsKey(to)) {
                                groupData.get(to).updateGroupChat(msgModel);
                                for (String member : groupData.get(to).getGroupMembers()) {
                                    if (clientManager.containsClient(member)) {
                                        Socket item = clientManager.getReceiver(member);
                                        ObjectOutputStream oos = new ObjectOutputStream(item.getOutputStream());

                                        oos.writeObject(pkg);
                                        oos.flush();

                                        System.out.println("Sent group message!");
                                    }
                                }
                            } else {
                                continue; //notify
                            }
                        }
                        

    //                        default -> {
    //                            continue;
    //                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error from ListeningThread: " + e.getMessage());
                System.out.println(e.getStackTrace());
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
                    
                    // receive username
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String username = br.readLine();
                    
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    UserInformation clientInformation = userData.get(username);
                    oos.writeObject(clientInformation);
                    oos.flush();

                    clientManager.addClient(username, socket);
                    theController.reloadConnectionTree();
                    
                    // publish data to render UI
                    ArrayList<String> onlineUsers = (ArrayList<String>)clientManager.getClientUsernameList();
                    onlineUsers.remove(username);
                    oos.writeObject(onlineUsers);
                    oos.flush();
                    
                    updateOnlineUsers(username);    // for the rest clients
                    
                    new ListeningThread(username, socket).start();
                }
            } catch (Exception e) {
                System.out.println("Error from ConnectingThread: " + e.getMessage());
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

            //TalkingThread.start
                        
            new ConnectingThread(serverSocket).start();
            
            theController.reloadConnectionTree();
            theController.displayMessage("Server Started!");
        } catch (Exception e) {
            theController.displayMessage("Unavailable Port!");
        }
    }
    // Methods
    public void updateOnlineUsers(String newOnlineUser) {
        if (clientManager.getReceiverSocketsExcept(newOnlineUser) == null ) {
            return;
        }
        for (Socket item : clientManager.getReceiverSocketsExcept(newOnlineUser)) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(item.getOutputStream());

                oos.writeObject(new SocketPackage(UPDATE_ONLINE ,newOnlineUser));
                oos.flush();
            } catch (IOException e) {
                System.out.println("Error from updateOnlineUsers: " + e.getMessage());
            }     
        }
    }
}