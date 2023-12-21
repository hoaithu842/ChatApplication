package model.component;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author hoaithu842
 */
public class SocketPackage implements Serializable {
    String code;
    MessageModel msgModel;
    String newOnlineUser;

    final String PRIVATE_MESSAGE = "private_message";
    final String UPDATE_ONLINE = "update_online";

    public SocketPackage(MessageModel msgModel) {
        this.code = PRIVATE_MESSAGE;
        this.msgModel = msgModel;
    }

    public SocketPackage(String newOnlineUser) {
        this.code = UPDATE_ONLINE;
        this.newOnlineUser = newOnlineUser;
    }

    public String getCode() {
        return code;
    }

    public MessageModel getMessageModel() {
        return msgModel;
    }
    
    public String getNewOnlineUser() {
        return newOnlineUser;
    }
}
