import controller.ServerManagementController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import model.ServerManagementModel;
import view.ServerManagementView;

/**
 *
 * @author hoaithu842
 */
public class ServerManagement {
    private static ServerManagementModel getModel() throws FileNotFoundException, IOException, ClassNotFoundException {
        // Load saved data (not First-time Access)
        File f = new File("server.dat");
        if (f.exists()){
            FileInputStream fis = new FileInputStream("server.dat");
            
            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                ServerManagementModel theModel = (ServerManagementModel)ois.readObject();
                ois.close();
                theModel.refresh();
                return theModel;
            }
        } else {
            // First-time Access
            return new ServerManagementModel();
        }
    }
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
//        ServerManagementModel theModel = getModel();
        ServerManagementModel theModel = new ServerManagementModel();
        ServerManagementView theView = new ServerManagementView();
        ServerManagementController theController = new ServerManagementController(theView, theModel);
        theModel.setController(theController);
        theView.setVisible(true);
    }
}
