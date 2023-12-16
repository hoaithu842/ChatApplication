package model;

import controller.AppChatController;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 *
 * @author hoaithu842
 */
public class AppChatModel {
    ClientInformation clientInfo;
    private int port;
    final private int DEFAULT_PORT = 8080;
    AppChatController theController;
        
    public AppChatModel() {
        port = 0;
    }
    // Getters
    public String getUsername() {
        return clientInfo.getUsername();
    }
    // Setters
    public void setController(AppChatController theController) {
        this.theController = theController;
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
           
            ois.close();
            oos.close();
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
            System.out.println("There's an error: " + e.getMessage());
            return false;
        }
    }
    class TalkingThread extends Thread {
        TalkingThread() {
            
        }
        @Override
        public void run() {
            
        }
    }
    public boolean checkServerPort(int port, String username) {
        if (port == DEFAULT_PORT) {
            return false;
        }
        try (
                Socket socket = new Socket("localhost", port);
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
        ){
            this.port = port;
            
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            
            
            ClientInformation clientInfo = new ClientInformation(is, os, socket, username);
            this.clientInfo = clientInfo;
            
            //  publish username to server
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(username);
            bw.newLine();
            bw.flush();
//            bw.close();
            
            //  receive data to prepare UI
            ArrayList<String> onlineUsers = (ArrayList<String>) ois.readObject();
            theController.prepareUIComponents(onlineUsers);
            
//            ois.close();
            // start talkingthread
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
//    public getOnlineUsers() {
//        
//    }
}
