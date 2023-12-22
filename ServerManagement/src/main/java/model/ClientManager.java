package model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hoaithu842
 */
public class ClientManager {
    private HashMap<String, Socket> clientSocket;
    ClientManager() {
        clientSocket = new HashMap<>();
    }
    
    // Getters
    public ArrayList<String> getClientUsernameList() {
        return new ArrayList<>(clientSocket.keySet());
    }
    public boolean containsClient(String username) {
        return clientSocket.containsKey(username);
    }
    
    // Setters
    void addClient(String username, Socket socket) {
        clientSocket.put(username, socket);
    }
    
    public Socket getReceiver(String username) {
        return clientSocket.get(username);
    }

    public ArrayList<Socket> getReceiverSocketsExcept(String username) {
        if (clientSocket.values().size() == 1) {
            return null;
        }
        System.out.println(clientSocket.values().size());
        Socket toRemove = clientSocket.get(username);
        ArrayList<Socket> sockets = new ArrayList<>(clientSocket.values());
        sockets.remove(toRemove);
        System.out.println(sockets.size());
        return sockets;
    }
    public ArrayList<Socket> getReceiverSockets(String from, String to) {
        ArrayList<Socket> sockets = new ArrayList<>();
        sockets.add(clientSocket.get(from));
        sockets.add(clientSocket.get(to));
        return sockets;
    }
}
