package model;

import controller.AppChatController;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import model.component.MessageModel;

/**
 *
 * @author hoaithu842
 */
public class AppChatModel {
    private int port;
    ClientInformation clientInfo;
    AppChatController theController;
    
    final private int DEFAULT_PORT = 8080;
    final String PRIVATE_MESSAGE = "private_message";
    TalkingThread tt;
    
    // Constructor
    public AppChatModel() {
        
    }
    // Getters
    public String getUsername() {
        return clientInfo.getUsername();
    }
    // Setters
    public void setController(AppChatController theController) {
        this.theController = theController;
    }
    public void updateChat(String with, MessageModel msgModel) {
        clientInfo.updateChat(with, msgModel);
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
    // Thread to talk to server
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
                        System.out.println("Socket closed!");
                        break;
                    }
                    String code = br.readLine();
                    System.out.println("Receive code: " + code);
                    switch (code) {
                        case PRIVATE_MESSAGE -> {
                            System.out.println("Entered case: " + code);
                            MessageModel msgModel = (MessageModel) ois.readObject();
                            System.out.println("\t Content from " + msgModel.getFrom() + " to " + msgModel.getTo() + ": " + msgModel.getContent());
                            theController.updateChat(msgModel);
                        }
                        case "CODE2" -> {
                            continue;
                        }
                        default -> {
                            continue;
                        }
                    }

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("There's an error from TalkingThread: " + e.getMessage());
                }
            }
        }
        public void sendPrivateChat(MessageModel msgModel) {
            try {
                synchronized (socket) {
                    bw.write(PRIVATE_MESSAGE);
                    bw.newLine();
                    bw.flush();

                    oos.writeObject(msgModel);
                    oos.flush();
                }
            } catch (IOException e) {
                System.out.println("There's an error from sendMessagePrivateChat(): " + e.getMessage());
            }
        }
    }
    public boolean checkServerPort(int port, String username) {
        if (port == DEFAULT_PORT) {
            return false;
        }
        try {
                Socket socket = new Socket("localhost", port);
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                
                this.port = port;            
            
                ClientInformation clientInfo = new ClientInformation(username); // import o duoi de lay lsu chat
                this.clientInfo = clientInfo;

                //  publish username to server
                bw.write(username);
                bw.newLine();
                bw.flush();

                //  receive data to prepare UI
                ArrayList<String> onlineUsers = (ArrayList<String>) ois.readObject();
                theController.prepareUIComponents(onlineUsers);

                this.tt = new TalkingThread(socket, br, bw, ois, oos);
                this.tt.start();
                
                return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    // Methods to publish MessageModel to Server
    public void sendMessagePrivateChat(MessageModel msgModel) {
        tt.sendPrivateChat(msgModel);
    }
    public void sendMessageGroupChat(MessageModel msgModel) {
    }
}
