package model;

import controller.AppChatController;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import model.component.MessageModel;
import model.component.SocketPackage;
import model.component.UserInformation;

/**
 *
 * @author hoaithu842
 */
public class AppChatModel {
    private int port;
    UserInformation clientInfo;
    AppChatController theController;
    
    final private int DEFAULT_PORT = 8080;
    final String PRIVATE_MESSAGE = "private_message";
    final String UPDATE_ONLINE = "update_online";
    Socket socket;
    
    // Constructor
    public AppChatModel() {
        
    }
    // Getters
    public String getUsername() {
        return clientInfo.getUsername();
    }
    public HashSet<MessageModel> getMessagesWithUser(String username) {
        return clientInfo.getMessagesWithUser(username);
    }
    public ArrayList<String> getHistoryChatUsers() {
        return clientInfo.getHistoryChatUsers();
    }
    public ArrayList<String> getHistoryChatGroups() {
        return clientInfo.getHistoryChatGroups();
    }
    // Setters
    public void setController(AppChatController theController) {
        this.theController = theController;
    }
    public void updateChat(String with, MessageModel msgModel) {
        clientInfo.updateChat(with, msgModel);
        theController.prepareChats();
    }
    //  Methods for authorization
    public boolean authorize(String username, String password, String method) {
        // Connect to DEFAULT_PORT to authorize
        try (
            Socket socket = new Socket("localhost", DEFAULT_PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
        ) {
            ArrayList<String> sendMsg = new ArrayList<>();
            sendMsg.add(username);
            sendMsg.add(password);
            sendMsg.add(method);

            oos.writeObject(sendMsg);
            oos.flush();

            ArrayList<String> receivedMsg = (ArrayList<String>) ois.readObject();

            socket.close();
            
            if (method.equals("login")) {
                if (receivedMsg.get(0).equals("false")) {
                    return false;
                }
                if (receivedMsg.get(1).equals("false")) {
                    return false;
                }
                return true;
            } else if (method.equals("signup")) {
                if (receivedMsg.get(0).equals("false")) {
                    return false;
                }    
                return true;
            }
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("There's an error from authorize(): " + e.getMessage());
            return false;
        }
    }
    
    class ListeningThread extends Thread {
        Socket socket;
        ListeningThread(Socket socket) {
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

                    System.out.println(clientInfo.getUsername() + " received from server code: " + pkg.getCode());

                    switch (pkg.getCode()) {
                            case PRIVATE_MESSAGE -> {
                                MessageModel msgModel = pkg.getMessageModel();

                                System.out.println("\t Content from " + msgModel.getFrom() + " to " + msgModel.getTo() + ": " + msgModel.getContent());
                                theController.updateChat(msgModel);
                            }
                            case UPDATE_ONLINE -> {
                                String newOnlineUser = pkg.getNewOnlineUser();

                                System.out.println("\t New online user: " + newOnlineUser);
                                theController.updateOnlineUser(newOnlineUser);
                            }
    //                        default -> {
    //                            continue;
    //                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error from ListeningThread: " + e.getMessage());
            }
        }
    }

    public boolean checkServerPort(int port, String username) {
        if (port == DEFAULT_PORT) {
            return false;
        }
        try {
            Socket socket = new Socket("localhost", port);          

            // publish username to server
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(username);
            bw.newLine();
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            this.clientInfo = (UserInformation) ois.readObject();             

            this.socket = socket;
            this.port = port;

            //  receive data to prepare UI
            ArrayList<String> onlineUsers = (ArrayList<String>) ois.readObject();
            theController.prepareUIComponents(onlineUsers);

            new ListeningThread(socket).start();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    // Methods to publish MessageModel to Server
    public void sendMessagePrivateChat(MessageModel msgModel) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new SocketPackage(msgModel));
            oos.flush();

            System.out.println("Sent!");
        } catch (Exception e) {
            System.out.println("Error from sendMessagePrivateChat: " + e.getMessage());
        }
    }
    public void sendMessageGroupChat(MessageModel msgModel) {
    }
}
