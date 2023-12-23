package model.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 *
 * @author hoaithu842
 */
public class UserInformation implements Serializable {    
    private String username;
    private String password;
    public LinkedHashMap<String, HashSet<MessageModel>> historyPrivateChat;
    public LinkedHashMap<Integer, GroupInformation> groupChat;
    
    public UserInformation(String username, String password) {        
        this.username = username;
        this.password = password;
        this.historyPrivateChat = new LinkedHashMap<>();
        this.groupChat = new LinkedHashMap<>();
    }

    public String getUsername() {
        return username;
    }
    
    public ArrayList<String> getHistoryChatUsers() {
        return new ArrayList<>(historyPrivateChat.keySet());
    }
    
    public ArrayList<GroupInformation> getHistoryChatGroups() {
        return new ArrayList<>(groupChat.values());
    }
    
    public HashSet<MessageModel> getMessagesWithUser(String username) {
        if (historyPrivateChat.containsKey(username)) {
            return historyPrivateChat.get(username);
        }
        return null;
    }
    
    public HashSet<MessageModel> getMessagesWithGroup(Integer groupId) {
        if (groupChat.containsKey(groupId)) {
            return groupChat.get(groupId).historyGroupChat;
        }
        return null;
    }
    
    public boolean passwordMatching(String password) {
        return this.password.equals(password);
    }

    public void createGroup(Integer ID, GroupInformation newGroup) {
        groupChat.put(ID, newGroup);
    }
    
    public void updateChat(String with, MessageModel msgModel) {
        if (!historyPrivateChat.containsKey(with)) {
            historyPrivateChat.put(with, new HashSet<>());
        }
        HashSet<MessageModel> msgModels = historyPrivateChat.get(with);
        msgModels.add(msgModel);
        historyPrivateChat.put(with, msgModels);
    }
    
    public void updateGroupChat(int ID, MessageModel msgModel) {
        GroupInformation groupInfo = groupChat.get(ID);
        groupInfo.updateGroupChat(msgModel);
        groupChat.put(ID, groupInfo);
    }
}
