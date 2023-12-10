package view;

import model.ConnectionManager;
import model.ConnectionInformation;
import model.ClientManager;
import model.ClientInformation;
import java.awt.event.ActionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author hoaithu842
 */
public class ServerManagementView extends javax.swing.JFrame {

    /**
     * Creates new form ServerView
     */
    public ServerManagementView() {
        initComponents();
        connectionTree.setRootVisible(false);
//        connectionTree.setCellRenderer();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        portLabel = new javax.swing.JLabel();
        portTextField = new javax.swing.JTextField();
        ipTextField = new javax.swing.JTextField();
        ipLabel = new javax.swing.JLabel();
        startServerButton = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        connectionTree = new javax.swing.JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server Management - 21127176 - 21KTPM1");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(400, 600));
        setMinimumSize(new java.awt.Dimension(400, 600));
        setName("ServerManagementFrame"); // NOI18N
        setResizable(false);

        portLabel.setText("Port");

        portTextField.setMaximumSize(new java.awt.Dimension(100, 22));
        portTextField.setMinimumSize(new java.awt.Dimension(100, 22));
        portTextField.setPreferredSize(new java.awt.Dimension(100, 22));

        ipTextField.setEditable(false);
        ipTextField.setMaximumSize(new java.awt.Dimension(100, 22));
        ipTextField.setMinimumSize(new java.awt.Dimension(100, 22));
        ipTextField.setPreferredSize(new java.awt.Dimension(100, 22));

        ipLabel.setText("IP");

        startServerButton.setText("Start Server");

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        connectionTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(connectionTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(startServerButton)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ipLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(portLabel)
                                .addGap(69, 69, 69)
                                .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ipLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portLabel))
                .addGap(18, 18, 18)
                .addComponent(startServerButton)
                .addGap(51, 51, 51)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree connectionTree;
    private javax.swing.JLabel ipLabel;
    private javax.swing.JTextField ipTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel portLabel;
    private javax.swing.JTextField portTextField;
    private javax.swing.JToggleButton startServerButton;
    // End of variables declaration//GEN-END:variables

    // Setters
    public void reloadConnectionTree(ConnectionManager connManager) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        
        for (ConnectionInformation connInfo : connManager.getConnectionInformationList()) {
            DefaultMutableTreeNode connInfoNode = new DefaultMutableTreeNode(connInfo.getPort());
            
            ClientManager clientManager = connInfo.getClientManager();
            for (ClientInformation clientInfo : clientManager.getClientInformationList()) {
                connInfoNode.add(new DefaultMutableTreeNode(clientInfo.getName()));
            }
            root.add(connInfoNode);
        }
        
        connectionTree.setModel(new javax.swing.tree.DefaultTreeModel(root));
        connectionTree.setRootVisible(false);
        
    }
    // Getters
    public javax.swing.JTextField getPortTextField() {
        return portTextField;
    }
    // Event Handler
    public void addStartServerButtonListener(ActionListener listenForClick) {
        startServerButton.addActionListener(listenForClick);
    }
}