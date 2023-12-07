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
    
    class StartServerButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
//            theView.getPortTextField().setText("Hello");
            int port = Integer.parseInt(theView.getPortTextField().getText());
            if (theModel.createConnection(port)) {
                theView.getPortTextField().setText("created successfully!");
            } else {
                theView.getPortTextField().setText("created unccessfully!");
            }
        }
    }
}
