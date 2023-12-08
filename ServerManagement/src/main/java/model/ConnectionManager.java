package model;

import java.util.ArrayList;

/**
 *
 * @author hoaithu842
 */
public class ConnectionManager {
    private ArrayList<ConnectionInformation> connList;
    ConnectionManager() {
        connList = new ArrayList<>();
    }
    public ArrayList<ConnectionInformation> getConnectionInformationList() {
        return connList;
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
