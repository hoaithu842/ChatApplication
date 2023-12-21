package model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hoaithu842
 */
public class ClientManager {
    // private HashMap<String, ClientInformation> clientList;
    private HashMap<String, Socket> clientSocket;
    ClientManager() {
        // clientList = new HashMap<>();
        // clientStream = new HashMap<>();
        clientSocket = new HashMap<>();
    }
    // Getters
    // public ClientInformation getClientInformation(String username) {
    //     return clientList.get(username);
    // }
    // public ArrayList<ClientInformation> getClientInformationList() {
    //     return new ArrayList<>(clientList.values());
    // }
    public ArrayList<String> getClientUsernameList() {
        return new ArrayList<>(clientSocket.keySet());
    }
    public boolean containsClient(String username) {
        return clientSocket.containsKey(username);
    }
    // Setters
    // void addClientInformation(ClientInformation clientInfo) {
    //     clientList.put(clientInfo.getUsername(), clientInfo);
    // }
    // void addClientStream(String username, BufferedReader br, BufferedWriter bw, ObjectInputStream ois, ObjectOutputStream oos) {
    //     TalkingStream newStream = new TalkingStream(br, bw, ois, oos);
    //     clientStream.put(username, newStream);
    // }
    void addClient(String username, Socket socket) {
        clientSocket.put(username, socket);
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
