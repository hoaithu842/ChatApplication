package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import model.component.TalkingStream;

/**
 *
 * @author hoaithu842
 */
public class ClientManager {
    private HashMap<String, ClientInformation> clientList;
    private HashMap<String, TalkingStream> clientStream;
    ClientManager() {
        clientList = new HashMap<>();
        clientStream = new HashMap<>();
    }
    // Getters
    public ClientInformation getClientInformation(String username) {
        return clientList.get(username);
    }
    public ArrayList<ClientInformation> getClientInformationList() {
        return new ArrayList<>(clientList.values());
    }
    public ArrayList<String> getClientUsernameList() {
        return new ArrayList<>(clientList.keySet());
    }
    public boolean containsClient(String username) {
        return clientList.containsKey(username);
    }
    // Setters
    void addClientInformation(ClientInformation clientInfo) {
        clientList.put(clientInfo.getUsername(), clientInfo);
    }
    void addClientStream(String username, BufferedReader br, BufferedWriter bw, ObjectInputStream ois, ObjectOutputStream oos) {
        TalkingStream newStream = new TalkingStream(br, bw, ois, oos);
        clientStream.put(username, newStream);
    }
    public BufferedReader br(String username) {
        return clientStream.get(username).br;
    }
    public BufferedWriter bw(String username) {
        return clientStream.get(username).bw;
    }
    public ObjectInputStream ois(String username) {
        return clientStream.get(username).ois;
    }
    public ObjectOutputStream oos(String username) {
        return clientStream.get(username).oos;
    }
}
