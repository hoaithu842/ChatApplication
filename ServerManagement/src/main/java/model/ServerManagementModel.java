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
    static private ConnectionManager connManager;
    ServerManagementController theController;
    
    public ServerManagementModel() {
        userPassword = new HashMap<>();
        connManager = new ConnectionManager();
        openPortToAuthorize();
    }
    // Getters
    public ConnectionManager getConnManager() {
        return connManager;
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
    
    
    
    
    
    class ConnectingThread extends Thread {
        ConnectionInformation connInfo;
        ConnectingThread(ConnectionInformation connInfo) {
            this.connInfo = connInfo;
        }
        @Override
        public void run() {
            try {  
                do {
                    Socket socket = connInfo.ss.accept(); //synchronous

                    InputStream is = socket.getInputStream();
                    BufferedReader br=new BufferedReader(new InputStreamReader(is));
                    
                    OutputStream os = socket.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                    
                    String name = br.readLine();
//                    System.out.println("Client " + name + " connected!");
                
                    ClientInformation clientInfo = new ClientInformation(is, os, socket, name);
                    connInfo.addClientInformation(clientInfo);
                    theController.reloadConnectionTree();
                    
                    TalkingThread tt = new TalkingThread(clientInfo);
                    tt.start();
                // bw.close();
                // br.close();
                } while (true);
            } catch(IOException e) {
                System.out.println("There're some error");
            }
        }
    }
    
    class TalkingThread extends Thread {
        ClientInformation clientInfo;
        ClientManager clientManager;
        TalkingThread(ClientInformation clientInfo) {
//            this.clientManager = clientManager;
            this.clientInfo = clientInfo;
        }
        @Override
        public void run() {
            do {
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
        }
    }
    
    public void createConnection(int port) {
        if (connManager.connectionExists(port)) {
            theController.displayMessage("Already Started!");
            return;
        }
        try{
            ServerSocket ss = new ServerSocket(port);
            ClientManager clientManager = new ClientManager();
            
            ConnectionInformation connInfo = new ConnectionInformation(ss, clientManager, port);
            connManager.addConnectionInformation(connInfo);
                        
            ConnectingThread ct = new ConnectingThread(connInfo);
            ct.start();
            
            theController.reloadConnectionTree();
            theController.displayMessage("Server Started!");
        } catch (IOException e) {
            theController.displayMessage("Unavailable Port!");
        }
    }
}
