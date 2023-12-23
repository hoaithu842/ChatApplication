package model.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author hoaithu842
 */
public class GroupInformation implements Serializable {
    private int ID;
    private String groupName;
    private ArrayList<String> groupMembers;
    public HashSet<MessageModel> historyGroupChat;
    
    public GroupInformation(int ID, String groupName, ArrayList<String> groupMembers) {
        this.ID = ID;
        this.groupName = groupName;
        this.groupMembers = groupMembers;
        this.historyGroupChat = new HashSet<>();
    }
    
    public int getID() {
        return ID;
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public ArrayList<String> getGroupMembers() {
        return groupMembers;
    }
    
    public void updateGroupChat(MessageModel msgModel) {
        if (historyGroupChat == null) {
            historyGroupChat = new HashSet<>();
        }
        historyGroupChat.add(msgModel);
    }
}
