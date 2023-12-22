package model.component;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author hoaithu842
 */
public class GroupInformation implements Serializable {
    private int ID;
    private String groupName;
    private ArrayList<String> groupMembers;
    
    public GroupInformation(int ID, String groupName, ArrayList<String> groupMembers) {
        this.ID = ID;
        this.groupName = groupName;
        this.groupMembers = groupMembers;
    }
}
