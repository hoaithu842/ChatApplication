package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import model.ServerManagementModel;
import view.ServerManagementView;

/**
 *
 * @author hoaithu842
 */
public class ServerManagementController {
    private final ServerManagementView theView;
    private final ServerManagementModel theModel;
    
    public ServerManagementController(ServerManagementView theView, ServerManagementModel theModel) {
        this.theModel = theModel;
        this.theView = theView;
        
        this.theView.addStartServerButtonListener(new StartServerButtonListener());
        this.theView.setIPLabel(theModel.getIP());
        this.theView.addServerManagementWindowListener(new ServerManagementWindowListener());
    }
    
    public void reloadConnectionTree() {
        theView.reloadConnectionTree(theModel);
    }
    
    public void displayMessage(String message) {
        theView.displayMessage(message);
    }
    
    class StartServerButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            int port;
            try {
                port = Integer.parseInt(theView.getPortTextField().getText());
            } catch(NumberFormatException e) {
                System.out.println("Error message!");
                return;
            }
            theModel.createConnection(port);
        }
    }
    
    class ServerManagementWindowListener implements WindowListener {
        @Override
        public void windowClosing(WindowEvent e) {
            // close socket
            theModel.saveServerData();
        }
        @Override
        public void windowOpened(WindowEvent e) {}
        @Override
        public void windowClosed(WindowEvent e) {}
        @Override
        public void windowIconified(WindowEvent e) {}
        @Override
        public void windowDeiconified(WindowEvent e) {}
        @Override
        public void windowActivated(WindowEvent e) {}
        @Override
        public void windowDeactivated(WindowEvent e) {}
    }
}
