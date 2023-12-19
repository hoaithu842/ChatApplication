package model;

import java.util.HashMap;
import java.util.HashSet;
import model.component.MessageModel;

/**
 *
 * @author hoaithu842
 */
public class ClientInformation {    
    public String username;
    public HashMap<String, HashSet<MessageModel>> historyPrivateChat;
    public HashMap<String, HashSet<MessageModel>> historyGroupChat;
    
    ClientInformation(String username) {        
        this.username = username;
        this.historyPrivateChat = new HashMap<>();
        this.historyGroupChat = new HashMap<>();
    }
    public String getUsername() {
        return username;
    }
    public void updateChat(String with, MessageModel msgModel) {
        if (!historyPrivateChat.containsKey(with)) {
            historyPrivateChat.put(with, new HashSet<>());
        }
        historyPrivateChat.get(with).add(msgModel);
    }
}
