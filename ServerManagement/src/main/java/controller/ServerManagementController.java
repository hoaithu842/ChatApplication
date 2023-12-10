package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    }
    
    public void reloadConnectionTree() {
        theView.reloadConnectionTree(theModel.getConnManager());
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
            
            if (theModel.createConnection(port)) {
                theView.getPortTextField().setText("created successfully!");
//                count++;
//                if (count == 2) {
//                    theView.reloadConnectionTree(theModel.getConnManager());
//                }
            } else {
                theView.getPortTextField().setText("created unccessfully!");
            }
        }
    }
}