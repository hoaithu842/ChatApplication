package model.component;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author hoaithu842
 */
public class MessageModel implements Serializable {
    private String from;
    private String to;
    private String content;
    private File file;
    
    public MessageModel(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }
    public MessageModel(String from, String to, File file) {
        this.from = from;
        this.to = to;
        this.file = file;
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
    public File getFile() {
        return file;
    }
}
