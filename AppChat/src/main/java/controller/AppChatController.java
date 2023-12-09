package controller;

import model.AppChatModel;
import view.AppChatView;

/**
 *
 * @author hoaithu842
 */
public class AppChatController {
    private final AppChatView theView;
    private final AppChatModel theModel;
    
    public AppChatController(AppChatView theView, AppChatModel theModel) {
        this.theView = theView;
        this.theModel = theModel;
    }
}
