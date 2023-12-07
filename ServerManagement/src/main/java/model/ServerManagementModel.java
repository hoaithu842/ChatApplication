package model;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author hoaithu842
 */
public class ServerManagementModel {
    static ConnectionManager connManager;
    
    public ServerManagementModel() {
        connManager = new ConnectionManager();
    }
    
    class ConnectionManager {
        ArrayList<ConnectionInformation> connList;
        ConnectionManager() {
            connList = new ArrayList<>();
        }
        boolean connectionExists(int port) {
            for (ConnectionInformation connInfo : connList) {
                if (connInfo.portEquals(port)) {
                    return true;
                }
            }
            return false;
        }
        void addConnectionInformation(ConnectionInformation connInfo) {
            connList.add(connInfo);
        }
    }
    
    class ConnectionInformation {
        ServerSocket ss;
        private int port;
        ClientManager clientManager;
        
        ConnectionInformation(ServerSocket ss, ClientManager clientManager, int port) {
            this.ss = ss;
            this.clientManager = clientManager;
            this.port = port;
        }
        boolean portEquals(int port) {
            return this.port == port;
        }
    }
    
    class ClientManager {
        ClientManager() {
            
        }
    }
    
    class ClientInformation {
        ClientInformation() {
            
        }
    }
    
    class ConnectingThread extends Thread {
        public void run() {
            
        }
    }
    
    class TalkingThread extends Thread {
        public void run() {
            
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
            
            
//            cm.addConnectionInformation(ci, manager);
//            ConnectingThread ct = new ConnectingThread(ci, cm);
//            ct.start();
            return true;
        } catch (IOException e) {
            System.out.println("There're some error");
            return false;
        }
    }
}
