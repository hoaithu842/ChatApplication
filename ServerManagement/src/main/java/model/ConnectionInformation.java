package model;

import java.net.ServerSocket;

/**
 *
 * @author hoaithu842
 */
public class ConnectionInformation {
    ServerSocket ss;
    private int port;
    ClientManager clientManager;

    ConnectionInformation(ServerSocket ss, ClientManager clientManager, int port) {
        this.ss = ss;
        this.clientManager = clientManager;
        this.port = port;
    }
    public int getPort() {
        return port;
    }
    public ClientManager getClientManager() {
        return clientManager;
    }
    boolean portEquals(int port) {
        return this.port == port;
    }
    void addClientInformation(ClientInformation clientInfo) {
        clientManager.addClientInformation(clientInfo);
    }
}
