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
    public LinkedHashMap<Integer, HashSet<MessageModel>> historyGroupChat;
    
    public UserInformation(String username, String password) {        
        this.username = username;
        this.password = password;
        this.historyPrivateChat = new LinkedHashMap<>();
        this.historyGroupChat = new LinkedHashMap<>();
    }

    public String getUsername() {
        return username;
    }
    
    public ArrayList<String> getHistoryChatUsers() {
        return new ArrayList<>(historyPrivateChat.keySet());
    }
    
    public ArrayList<Integer> getHistoryChatGroups() {
        return new ArrayList<>(historyGroupChat.keySet());
    }
    
    public HashSet<MessageModel> getMessagesWithUser(String username) {
        if (historyPrivateChat.containsKey(username)) {
            return historyPrivateChat.get(username);
        }
        return null;
    }
    
    public boolean passwordMatching(String password) {
        return this.password.equals(password);
    }

    public void createGroup(Integer ID) {
        historyGroupChat.put(ID, new HashSet<>());
    }
    
    public void updateChat(String with, MessageModel msgModel) {
        if (!historyPrivateChat.containsKey(with)) {
            historyPrivateChat.put(with, new HashSet<>());
        }
        HashSet<MessageModel> msgModels = historyPrivateChat.get(with);
        msgModels.add(msgModel);
        historyPrivateChat.put(with, msgModels);
    }
}
