package model;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author hoaithu842
 */
public class ClientInformation {
    public InputStream is;
    public OutputStream os;
    public Socket socket;
    public String username;
    
    ClientInformation(InputStream is, OutputStream os, Socket socket, String username) {
        this.is = is;
        this.os = os;
        this.socket = socket;
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
