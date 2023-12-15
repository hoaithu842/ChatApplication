package model;

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
        
    public AppChatModel() {
        port = 0;
    }
    
    public boolean authorize(String username, String password, String method) {
        // Connect to DEFAULT_PORT to authorize
        try (Socket socket = new Socket("localhost", DEFAULT_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            ArrayList<String> sendMsg = new ArrayList<>();
            sendMsg.add(username);
            sendMsg.add(password);
            sendMsg.add(method);

            oos.writeObject(sendMsg);
            oos.flush();

            ArrayList<String> receivedMsg = (ArrayList<String>) ois.readObject();
            
            socket.close();
            ois.close();
            oos.close();
            
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
    
    public String getUsername() {
        return clientInfo.getName();
    }
    public boolean checkServerPort(int port, String username) {
        if (port == DEFAULT_PORT) {
            return false;
        }
        try {
            Socket socket = new Socket("localhost", port);
            
            this.port = port;
            
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            OutputStream os = socket.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            
                
            ClientInformation clientInfo = new ClientInformation(br, bw, socket, username);
            this.clientInfo = clientInfo;
            
//            DataInputStream din = new DataInputStream(System.in);
//            bw.write(this.clientInfo);
//            bw.newLine();
//            bw.flush();
            

//                String sentMessage="";
//                String receivedMessage;
//
//                // System.out.println("Talking to Server");
                DataInputStream din=new DataInputStream(System.in);
//                System.out.println("enter your name: ");
//                sentMessage=din.readLine();
                bw.write(username);
                bw.newLine();
                bw.flush();
//                do {
//                        System.out.print("message: ");
//                        sentMessage=din.readLine();
//                        bw.write(sentMessage);
//                        bw.newLine();
//                        bw.flush();
//                        if (sentMessage.equalsIgnoreCase("quit"))
//                                break;
//                        else
//                        {
//                // 		receivedMessage=br.readLine();
//                // 		System.out.println("Received : " + receivedMessage);					
//                        }
//
//                } while(true);
//                bw.close();
//                br.close();
            return true;
        } catch(IOException e) {
            return false;
//                System.out.println("There're some error");
        }
    }
//    public getOnlineUsers() {
//        
//    }
}
