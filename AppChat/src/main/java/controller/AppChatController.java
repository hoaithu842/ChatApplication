package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import model.AppChatModel;
import model.component.MessageModel;
import view.AppChatView;
import view.JoinServerView;
import view.LogInView;
import view.SignUpView;
import view.component.ChatItem;

/**
 *
 * @author hoaithu842
 */
public class AppChatController {
    private final AppChatView theView;
    private final AppChatModel theModel;
    private final LogInView theLogInView;
    private final SignUpView theSignUpView;
    private final JoinServerView theJoinServerView;
    
    public AppChatController(AppChatView theView, AppChatModel theModel) {
        // Declaration
        this.theView = theView;
        this.theModel = theModel;
        this.theLogInView = new LogInView();
        this.theSignUpView = new SignUpView();
        this.theJoinServerView = new JoinServerView();
        
        addEventHandlers();
//        prepareUIComponents();
        
        // Start flow
        forceLogIn();
        
    }
    
    public void prepareUIComponents(ArrayList<String> onlineUsers) {
//        theView.prepareChats();
//        theView.prepareGroups();
        theView.prepareUsers(onlineUsers);
    }
    
    // Methods for SignUp/LogIn
    void forceLogIn() {
        theLogInView.refreshView();
        theSignUpView.setVisible(false);
        theLogInView.setVisible(true);
    }
    
    void forceSignUp() {
        theSignUpView.refreshView();
        theLogInView.setVisible(false);
        theSignUpView.setVisible(true);
    }
    
    void forceJoinServer() {
        theLogInView.setVisible(false);
        theJoinServerView.setVisible(true);
    }
    
    void enterAppChat() {
        theJoinServerView.setVisible(false);
        theView.setVisible(true);
    }
    
    public void updateChat(MessageModel msgModel) {
        String from = msgModel.getFrom();
        String to = msgModel.getTo();
        String with = "";

        if (theModel.getUsername().equals(from)) {
            with = to;
        } else if (theModel.getUsername().equals(to)) {
            with = from;
        }
        System.out.println("\t" + from + " to " + to);
        theModel.updateChat(with, msgModel);
        if (theModel.getUsername().equals(from) && theView.getToWhomLabel().getText().equals(to)){
            System.out.println("\t\t-> Updating UI!");
            theView.updateChat(msgModel);
        }
        
        if (theModel.getUsername().equals(to) && theView.getToWhomLabel().getText().equals(from)) {
            System.out.println("\t\t-> Updating UI!");
            theView.updateChat(msgModel);
        }
    }
    
    public void updateOnlineUser(String username) {
        theView.updateUser(username);
    }
    
    // Event Handlers
    final void addEventHandlers() {
        // Event Handlers for theLogInView
        theLogInView.addLogInButtonListener(new LogInButtonListenerForLogIn());
        theLogInView.addSignUpButtonListener(new SignUpButtonListenerForLogIn());
        
        // Event Handlers for theSignUpView
        theSignUpView.addLogInButtonListener(new LogInButtonListenerForSignUp());
        theSignUpView.addSignUpButtonListener(new SignUpButtonListenerForSignUp());
        
        // Event Handlers for theJoinServerView
        theJoinServerView.addJoinServerButtonListener(new JoinServerButtonListener());
        
        // Event Handlers for theView
        theView.addShowChatsPanelMouseListener(new ShowChatsPanelMouseListener());
        theView.addShowGroupsPanelMouseListener(new ShowGroupsPanelMouseListener());
        theView.addShowUsersPanelMouseListener(new ShowUsersPanelMouseListener());
        
        theView.addSendMessageButtonListener(new SendMessageButtonListener());
    }
    
    // Event Handlers for theLogInView
    class LogInButtonListenerForLogIn implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String username = theLogInView.getUsername();
            String password = theLogInView.getPassword();
            
            if (theModel.authorize(username, password, "login")) {
                theLogInView.displayMessage("Logged In!");
                forceJoinServer();
            } else {
                theLogInView.displayMessage("Login Failed!");
            }
        }
    }
    class SignUpButtonListenerForLogIn implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            forceSignUp();
       }
    }
    
    // Event Handlers for theSignUpView
    class LogInButtonListenerForSignUp implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            forceLogIn();
        }
    }
    class SignUpButtonListenerForSignUp implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (!theSignUpView.getPassword().equals(theSignUpView.getReenterPassword())) {
                theSignUpView.displayMessage("Passwords do not match!");
            } else {
                String username = theSignUpView.getUsername();
                String password = theSignUpView.getPassword();
                if (theModel.authorize(username, password, "signup")) {
                    theSignUpView.displayMessage("Signed Up!");
                    forceLogIn();
                } else {
                    theLogInView.displayMessage("Username already exists!");
                }
            }
        }
    }
    
    // Event Handlers for theJoinServerView
    class JoinServerButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            int port;
            
            try {
                port = Integer.parseInt(theJoinServerView.getPort());
                if (theModel.checkServerPort(port, theLogInView.getUsername())) {
                    theView.getUsernameLabel().setText(theModel.getUsername());
                    
                    enterAppChat();
                } else {
                    theJoinServerView.displayMessage("Invalid port!");
                }
            } catch(NumberFormatException e) {
                System.out.println("Error message!");
                return;
            }
        }
    }
    
    // Even Handlers for theView    
    class ShowChatsPanelMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            Component clickedComponent = theView.getShowChatsPanel().getComponentAt(event.getPoint());
            if (clickedComponent instanceof ChatItem chatItem) {
                theView.getToWhomLabel().setText(chatItem.getName());
            }
        }
    }
    class ShowGroupsPanelMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            Component clickedComponent = theView.getShowGroupsPanel().getComponentAt(event.getPoint());
            if (clickedComponent instanceof ChatItem chatItem) {
                theView.getToWhomLabel().setText(chatItem.getName());
            }
        }
    }
    class ShowUsersPanelMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            Component clickedComponent = theView.getShowUsersPanel().getComponentAt(event.getPoint());
            if (clickedComponent instanceof ChatItem chatItem) {
                theView.getToWhomLabel().setText(chatItem.getName());
            }
        }
    }
    class SendMessageButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String from = theModel.getUsername();
            String to = theView.getToWhomLabel().getText();
            String content = theView.getTypedMessageTextField().getText();
            theView.getTypedMessageTextField().setText("");
            
            MessageModel msgModel = new MessageModel(from, to, content);
            if (theView.isShowGroupsOpening()) {
                theModel.sendMessageGroupChat(msgModel);
            } else {
                theModel.sendMessagePrivateChat(msgModel);
            }
        }
    }
}
