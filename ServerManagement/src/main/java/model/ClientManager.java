package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hoaithu842
 */
public class ClientManager {
    private HashMap<String, ClientInformation> clientList;
    ClientManager() {
        clientList = new HashMap<>();
    }
    public ArrayList<ClientInformation> getClientInformationList() {
        return new ArrayList<>(clientList.values());
    }
    public ArrayList<String> getClientUsernameList() {
        return new ArrayList<>(clientList.keySet());
    }
    void addClientInformation(ClientInformation clientInfo) {
        clientList.put(clientInfo.getUsername(), clientInfo);
    }
}
