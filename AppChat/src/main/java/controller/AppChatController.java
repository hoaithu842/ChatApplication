package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.AppChatModel;
import view.AppChatView;
import view.LogInView;
import view.SignUpView;

/**
 *
 * @author hoaithu842
 */
public class AppChatController {
    private final AppChatView theView;
    private final AppChatModel theModel;
    private final LogInView theLogInView;
    private final SignUpView theSignUpView;
    
    public AppChatController(AppChatView theView, AppChatModel theModel) {
        this.theView = theView;
        this.theModel = theModel;
        this.theLogInView = new LogInView();
        this.theSignUpView = new SignUpView();
        forceLogIn();
        
        // Event Handler for theLogInView
        this.theLogInView.addLogInButtonListener(new LogInButtonListenerForLogIn());
        this.theLogInView.addSignUpButtonListener(new SignUpButtonListenerForLogIn());
        
        // Event Handler for theSignUpView
        this.theSignUpView.addLogInButtonListener(new LogInButtonListenerForSignUp());
        this.theSignUpView.addSignUpButtonListener(new SignUpButtonListenerForSignUp());
    }
    
    void forceLogIn() {
        theLogInView.setVisible(true);
    }
    
    // Event Handler for theLogInView
    class LogInButtonListenerForLogIn implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            int temp = 2 + 3;
        }
    }
    class SignUpButtonListenerForLogIn implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            theLogInView.setVisible(false);
            theSignUpView.setVisible(true);
       }
    }
    
    // Event Handler for theSignUpView
    class LogInButtonListenerForSignUp implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            theSignUpView.setVisible(false);
            theLogInView.setVisible(true);
        }
    }
    class SignUpButtonListenerForSignUp implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            int temp = 2 + 3;
        }
    }
}
