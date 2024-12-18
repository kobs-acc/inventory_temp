package homepage;

import event.EventMenuSelected;
import javax.swing.JComponent;
import form.Sales_Form;
import form.Employee_Form;
import sales.Stock_Form;

public class Homepage extends javax.swing.JFrame {

    /**
     * Creates new form Homepage
     */
    public Homepage() {
        initComponents();
        menu.initMoving(Homepage.this);
        menu.addEeventMenuSelected(new EventMenuSelected() {
            @Override
            public void selected(int index) {
                System.out.println("Selected Index: " + index);
                if (index == 0) {
                    setForm(new Sales_Form());
                } else if (index == 1) {
                    setForm(new Employee_Form());
                } else if (index == 2) {
                    setForm(new Stock_Form());  
                } else if (index == 6) {
                    // Close the current window (Homepage)
                    Homepage.this.dispose();

                    // Reopen the Login screen
                    java.awt.EventQueue.invokeLater(() -> {
                        new login.Login().setVisible(true);
                    });
                }
            }
        });
        setForm(new Sales_Form());  
    }
    
    
    private void setForm(JComponent com){
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();

    }
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBorderMenu = new homepage.PanelBorder();
        menu = new homepage.Menu();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelBorderMenu.setBackground(new java.awt.Color(237, 249, 255));
        panelBorderMenu.setPreferredSize(new java.awt.Dimension(1200, 600));

        menu.setOpaque(true);

        mainPanel.setOpaque(false);
        mainPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout panelBorderMenuLayout = new javax.swing.GroupLayout(panelBorderMenu);
        panelBorderMenu.setLayout(panelBorderMenuLayout);
        panelBorderMenuLayout.setHorizontalGroup(
            panelBorderMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorderMenuLayout.createSequentialGroup()
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelBorderMenuLayout.setVerticalGroup(
            panelBorderMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorderMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 1234, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBorderMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private homepage.Menu menu;
    private homepage.PanelBorder panelBorderMenu;
    // End of variables declaration//GEN-END:variables
}

