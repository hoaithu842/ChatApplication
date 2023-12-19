package model.component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author hoaithu842
 */
public class TalkingStream {
    public BufferedReader br;
    public BufferedWriter bw;
    public ObjectInputStream ois;
    public ObjectOutputStream oos;
    
    public TalkingStream(BufferedReader br, BufferedWriter bw, ObjectInputStream ois, ObjectOutputStream oos) {
        this.br = br;
        this.bw = bw;
        this.ois = ois;
        this.oos = oos;
    }
}
