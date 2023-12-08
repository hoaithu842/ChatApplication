package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

/**
 *
 * @author hoaithu842
 */
public class ClientInformation {
    public BufferedReader br;
    public BufferedWriter bw;
    public Socket socket;
    public String name;
    ClientInformation(BufferedReader br, BufferedWriter bw, Socket socket, String name) {
        this.br = br;
        this.bw = bw;
        this.socket = socket;
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
