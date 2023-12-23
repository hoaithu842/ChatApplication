package model.component;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author hoaithu842
 */
public class SocketPackage implements Serializable {
    int code;
    Integer groupID;
    MessageModel msgModel;
    String newOnlineUser;
    GroupInformation newGroup;
    ArrayList<String> totalUsers;

    public SocketPackage(int code) {
        this.code = code;
    }
    public SocketPackage(int code, MessageModel msgModel) {
        this.code = code;
        this.msgModel = msgModel;
    }

    public SocketPackage(int code, String newOnlineUser) {
        this.code = code;
        this.newOnlineUser = newOnlineUser;
    }
    
    public SocketPackage(int code, String groupName, ArrayList<String> groupMembers) {
        this.code = code;
        this.groupID = -1;
        this.newOnlineUser = groupName;
        this.totalUsers = groupMembers;
    }
    
    public int getCode() {
        return code;
    }
    
    public Integer getGroupID() {
        return groupID;
    }
    
    public GroupInformation getNewGroup() {
        return newGroup;
    }

    public MessageModel getMessageModel() {
        return msgModel;
    }
    
    public String getNewOnlineUser() {
        return newOnlineUser;
    }
    
    public ArrayList<String> getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(ArrayList<String> totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public void setGroupID(Integer ID) {
        this.groupID = ID;
    }
    
    public void setNewGroup(GroupInformation newGroup) {
        this.newGroup = newGroup;
    }
}
