import controller.ServerManagementController;
import model.ServerManagementModel;
import view.ServerManagementView;

/**
 *
 * @author hoaithu842
 */
public class ServerManagement {

    public static void main(String[] args) {
        ServerManagementModel theModel = new ServerManagementModel();
        ServerManagementView theView = new ServerManagementView();
        ServerManagementController theController = new ServerManagementController(theView, theModel);
        theModel.setController(theController);
        theView.setVisible(true);
    }
}
