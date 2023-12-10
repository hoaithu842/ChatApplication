import controller.AppChatController;
import model.AppChatModel;
import view.AppChatView;

/**
 *
 * @author hoaithu842
 */
public class AppChat {

    public static void main(String[] args) {
        AppChatModel theModel = new AppChatModel();
        AppChatView theView = new AppChatView();
        AppChatController theController = new AppChatController(theView, theModel);
    }
}
