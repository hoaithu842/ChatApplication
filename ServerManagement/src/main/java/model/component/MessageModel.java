package model.component;

import java.io.Serializable;

/**
 *
 * @author hoaithu842
 */
public class MessageModel implements Serializable {
    private String from;
    private String to;
    private String content;
    
    public MessageModel(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }
    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }
    public String getContent() {
        return content;
    }
}
