package model.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author hoaithu842
 */
public class UserInformation implements Serializable {    
    private String username;
    private String password;
    public HashMap<String, HashSet<MessageModel>> historyPrivateChat;
    public HashMap<String, HashSet<MessageModel>> historyGroupChat;
    
    public UserInformation(String username, String password) {        
        this.username = username;
        this.password = password;
        this.historyPrivateChat = new HashMap<>();
        this.historyGroupChat = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean passwordMatching(String password) {
        return this.password.equals(password);
    }

    public void updateChat(String with, MessageModel msgModel) {
        if (!historyPrivateChat.containsKey(with)) {
            historyPrivateChat.put(with, new HashSet<>());
        }
        historyPrivateChat.get(with).add(msgModel);
    }
}
