package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author hoaithu842
 */
public class ConnectionManager {
    private HashMap<Integer, ConnectionInformation> connList;
    ConnectionManager() {
        connList = new HashMap<>();
    }
    public ArrayList<ConnectionInformation> getConnectionInformationList() {
        return new ArrayList<>(connList.values());
    }
    boolean connectionExists(int port) {
        return connList.containsKey(port);
    }
    void addConnectionInformation(ConnectionInformation connInfo) {
        connList.put(connInfo.getPort(), connInfo);
    }
}
