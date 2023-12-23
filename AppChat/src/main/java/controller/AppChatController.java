package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import model.AppChatModel;
import model.component.GroupInformation;
import model.component.MessageModel;
import view.AppChatView;
import view.CreateGroupView;
import view.JoinServerView;
import view.LogInView;
import view.SignUpView;
import view.component.ChatItem;
import view.component.GroupItem;

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
    private final CreateGroupView theCreateGroupView;
    
    public AppChatController(AppChatView theView, AppChatModel theModel) {
        // Declaration
        this.theView = theView;
        this.theModel = theModel;
        this.theLogInView = new LogInView();
        this.theSignUpView = new SignUpView();
        this.theJoinServerView = new JoinServerView();
        this.theCreateGroupView = new CreateGroupView();
        
        addEventHandlers();
        
        // Start flow
        forceLogIn();
        
    }
    
    public void prepareUIComponents(ArrayList<String> onlineUsers) {
        theView.prepareChats(theModel.getHistoryChatUsers());
        theView.prepareGroups(theModel.getHistoryChatGroups());
        theView.prepareUsers(onlineUsers);
        theView.switchToChats();
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

        theModel.updateChat(with, msgModel);
        theView.prepareChats(theModel.getHistoryChatUsers());

        if (theModel.getUsername().equals(from) && theView.getToWhomLabel().getText().equals(to)){
            theView.updateChat(msgModel);
        }
        
        if (theModel.getUsername().equals(to) && theView.getToWhomLabel().getText().equals(from)) {
            theView.updateChat(msgModel);
        }
    }
    
    public void updateGroupChat(MessageModel msgModel) {
//        String from = msgModel.getFrom();
        int to = Integer.parseInt(msgModel.getTo());
        
        theModel.updateGroupChat(to, msgModel);
        theView.prepareGroups(theModel.getHistoryChatGroups());
        
        if (theView.getGroupIdLabel().getText().equals(Integer.toString(to))) {
            theView.updateChat(msgModel);
        }
    }
    public void receivePrivateFile(MessageModel msgModel) {
        Path targetDirectory = Path.of(".");

        try {
            Files.createDirectories(targetDirectory);
            Path targetFile = targetDirectory.resolve(msgModel.getFile().getName());
            Files.write(targetFile.toAbsolutePath(), Files.readAllBytes(msgModel.getFile().toPath()), StandardOpenOption.CREATE);

            theView.displayMessage("Received file from user " + msgModel.getFrom() + " at: " + targetFile.toAbsolutePath());
            System.out.println("File data successfully written to: " + targetFile.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void receiveGroupFile(MessageModel msgModel) {
        Path targetDirectory = Path.of(".");

        try {
            Files.createDirectories(targetDirectory);
            Path targetFile = targetDirectory.resolve(msgModel.getFile().getName());
            Files.write(targetFile.toAbsolutePath(), Files.readAllBytes(msgModel.getFile().toPath()), StandardOpenOption.CREATE);

            theView.displayMessage("Received file from group ID" + msgModel.getTo() + " at: " + targetFile.toAbsolutePath());
            System.out.println("File data successfully written to: " + targetFile.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateOnlineUser(String username) {
        theView.updateUser(username);
    }
    
    public void updateCreateGroups(int ID, GroupInformation newGroup) {
        theModel.updateCreateGroups(ID, newGroup);
        theView.prepareGroups(theModel.getHistoryChatGroups());
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
        
        // Event Handlers for theCreateGroupView
        theCreateGroupView.addCreateGroupButtonActionListener(new CreateGroupButtonActionListener());
        
        // Event Handlers for theView
            // 3 menu button
        theView.addShowChatsButtonActionListener(new ShowChatsButtonActionListener());
        theView.addShowGroupsButtonMouseListener(new ShowGroupsButtonMouseListener());
        theView.addShowUsersButtonActionListener(new ShowUsersButtonActionListener());
            // item of panel
        theView.addShowChatsPanelMouseListener(new ShowChatsPanelMouseListener());
        theView.addShowGroupsPanelMouseListener(new ShowGroupsPanelMouseListener());
        theView.addShowUsersPanelMouseListener(new ShowUsersPanelMouseListener());
        
        theView.addSendMessageButtonListener(new SendMessageButtonListener());
        theView.addUploadButtonListener(new UploadButtonListener());
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
    
    // Event Handlers for theCreateGroupView
    class CreateGroupButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String groupName = theCreateGroupView.getGroupName();
            ArrayList<String> groupMembers = theCreateGroupView.getGroupMembers();
            
            if (groupMembers.size()<=2 || groupName.isEmpty() || groupName.isBlank()) {
                theCreateGroupView.displayMessage("Cannot create with empty group name or member!");
            } else {
                theModel.sendCreateGroupRequest(groupName, groupMembers);
                theCreateGroupView.displayMessage("Done");
                theCreateGroupView.setVisible(false);
            }
        }
    }
    // Event Handlers for theView    
    class ShowChatsButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            theView.switchToChats();
        }
    }
    class ShowGroupsButtonMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent event) {
            if (event.getClickCount()==2) { // double-click to create group
                theModel.sendTotalUsersRequest();
                return;
            }
            theView.switchToGroups();
        }
    }
    class ShowUsersButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            theView.switchToUsers();
        }
    }
    class ShowChatsPanelMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            Component clickedComponent = theView.getShowChatsPanel().getComponentAt(event.getPoint());
            if (clickedComponent instanceof ChatItem chatItem) {
                theView.getToWhomLabel().setText(chatItem.getName());
                theView.getGroupIdLabel().setText("");
                theView.prepareChat(theModel.getMessagesWithUser(chatItem.getName()));
            }
        }
    }
    class ShowGroupsPanelMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            Component clickedComponent = theView.getShowGroupsPanel().getComponentAt(event.getPoint());
            if (clickedComponent instanceof GroupItem groupItem) {
                theView.getToWhomLabel().setText(groupItem.getName());
                theView.getGroupIdLabel().setText(groupItem.getGroupId());
                theView.prepareChat(theModel.getMessagesWithGroup(groupItem.getGroupId()));
            }
        }
    }
    class ShowUsersPanelMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            Component clickedComponent = theView.getShowUsersPanel().getComponentAt(event.getPoint());
            if (clickedComponent instanceof ChatItem chatItem) {
                theView.getToWhomLabel().setText(chatItem.getName());
                theView.getGroupIdLabel().setText("");
                theView.prepareChat(theModel.getMessagesWithUser(chatItem.getName()));
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
            if (to.equals("") || content.isEmpty() || content.isBlank()) {
                theView.displayMessage("Invalid user or message!");
                return;
            }
            if (!theView.getGroupIdLabel().getText().isEmpty()) {   
                to = theView.getGroupIdLabel().getText();
                MessageModel msgModel = new MessageModel(from, to, content);
                theModel.sendMessageGroupChat(msgModel);
                theView.switchToGroups();
            } else {
                MessageModel msgModel = new MessageModel(from, to, content);
                theModel.sendMessagePrivateChat(msgModel);
                theView.switchToChats();
            }
        }
    }
    class UploadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            File file = theView.openFileChooser();
            if (file!=null) {
                theView.displayMessage("File sent!");
                String from = theModel.getUsername();
                String to = theView.getToWhomLabel().getText();
                theView.getTypedMessageTextField().setText("");
                if (to.equals("")) {
                    theView.displayMessage("Invalid user!");
                }
                if (!theView.getGroupIdLabel().getText().isEmpty()) {   
                    to = theView.getGroupIdLabel().getText();
                    MessageModel msgModel = new MessageModel(from, to, file);
                    theModel.sendFileGroupChat(msgModel);
                    theView.switchToGroups();
                } else {
                    MessageModel msgModel = new MessageModel(from, to, file);
                    theModel.sendFilePrivateChat(msgModel);
                    theView.switchToChats();
            }
            }
        }
    }
    // Methods to create group
    public void createGroup(ArrayList<String> totalUsers) {
        theCreateGroupView.prepareList(totalUsers);
        theCreateGroupView.setVisible(true);
    }
}
