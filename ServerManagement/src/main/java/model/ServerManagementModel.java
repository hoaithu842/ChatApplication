package model;

import controller.ServerManagementController;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author hoaithu842
 */
public class ServerManagementModel {
    static private ConnectionManager connManager;
    ServerManagementController theController;
    
    public ServerManagementModel() {
        connManager = new ConnectionManager();
    }
    
    public void setController(ServerManagementController theController) {
        this.theController = theController;
    }
    
    public ConnectionManager getConnManager() {
        return connManager;
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
                    System.out.println("Client " + name + " connected!");
                
                    ClientInformation clientInfo = new ClientInformation(br, bw, socket, name);
                    connInfo.addClientInformation(clientInfo);
                    
                    TalkingThread tt = new TalkingThread(clientInfo);
                    tt.start();
                    theController.reloadConnectionTree();
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
                        receivedMessage=clientInfo.br.readLine();
                    } catch (IOException ex) {
                        System.out.println("Some error occured!");
                        // Bao la user da bi mat ket noi
                        return;
                    }
                    System.out.println("Received from " + clientInfo.name + ": " + receivedMessage);
                    if (receivedMessage.equalsIgnoreCase("quit")) {
                        System.out.println("Client has left !");
                        break;
                    }
                } while (true);
                // tt_ci.bw.close();
                // tt_ci.br.close();
            } while (true);
//            System.out.println("Exiting child thread.");
        }
    }
    
    public boolean createConnection(int port) {
        if (connManager.connectionExists(port)) {
            return false;
        }
        try{
            ServerSocket ss = new ServerSocket(port);
            ClientManager clientManager = new ClientManager();
            
            ConnectionInformation connInfo = new ConnectionInformation(ss, clientManager, port);
            connManager.addConnectionInformation(connInfo);
                        
            ConnectingThread ct = new ConnectingThread(connInfo);
            ct.start();
            theController.reloadConnectionTree();
            return true;
        } catch (IOException e) {
            System.out.println("There're some error");
            return false;
        }
    }
}
