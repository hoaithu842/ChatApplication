package view.component;

import java.awt.event.MouseListener;

/**
 *
 * @author hoaithu842
 */
public class GroupItem extends javax.swing.JPanel {

    /**
     * Creates new form GroupItem
     */
    public GroupItem(String groupId, String groupName) {
        initComponents();
        groupIdLabel.setText(groupId);
        nameLabel.setText(groupName);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        avatarLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        groupIdLabel = new javax.swing.JLabel();

        avatarLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user-avatar.png"))); // NOI18N

        nameLabel.setText("@groupname");

        groupIdLabel.setBackground(new java.awt.Color(153, 153, 153));
        groupIdLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        groupIdLabel.setText("ID");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(avatarLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(groupIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(groupIdLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(avatarLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avatarLabel;
    private javax.swing.JLabel groupIdLabel;
    private javax.swing.JLabel nameLabel;
    // End of variables declaration//GEN-END:variables

    // Getters
    @Override
    public String getName() {
        return nameLabel.getText();
    }
    
    public String getGroupId() {
        return groupIdLabel.getText();
    }
    
    // Event Handlers
    public void addUsernameLabelMouseListener(MouseListener listenForClick) {
        nameLabel.addMouseListener(listenForClick);
    }
}