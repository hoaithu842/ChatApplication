package model;

import java.util.ArrayList;

/**
 *
 * @author hoaithu842
 */
public class ClientManager {
    private ArrayList<ClientInformation> clientList;
    ClientManager() {
        clientList = new ArrayList<>();
    }
    public ArrayList<ClientInformation> getClientInformationList() {
        return clientList;
    }
    void addClientInformation(ClientInformation clientInfo) {
        clientList.add(clientInfo);
    }
}
