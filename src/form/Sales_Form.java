
package form;

import javax.swing.ImageIcon;
import model.Card_Model;


public class Sales_Form extends javax.swing.JPanel {


    public Sales_Form() {
        initComponents();
            card1.setData(new Card_Model(new ImageIcon(getClass().getResource("/images/stock.png")), "Top-Selling Product", "Fiji", "Increased by 60%"));
            card2.setData(new Card_Model(new ImageIcon(getClass().getResource("/images/profit.png")), "Total Revenue", "$15000", "Increased by 25%"));
            card3.setData(new Card_Model(new ImageIcon(getClass().getResource("/images/sold.png")), "Amount Sold", "2400 Items", "Increased by 50% Last Month"));
            card4.setData(new Card_Model(new ImageIcon(getClass().getResource("/images/customer.png")), "Customers Served", "560", "Within the Day"));
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pane = new javax.swing.JLayeredPane();
        card1 = new sales.Card();
        card2 = new sales.Card();
        card3 = new sales.Card();
        panelBorder1 = new homepage.PanelBorder();
        card4 = new sales.Card();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(237, 249, 255));
        setPreferredSize(new java.awt.Dimension(900, 600));

        pane.setPreferredSize(new java.awt.Dimension(900, 200));
        pane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        card1.setColor1(new java.awt.Color(186, 15, 82));
        card1.setColor2(new java.awt.Color(186, 15, 82));
        card1.setPreferredSize(new java.awt.Dimension(250, 180));
        pane.add(card1);

        card2.setColor1(new java.awt.Color(82, 186, 15));
        card2.setColor2(new java.awt.Color(82, 186, 15));
        card2.setPreferredSize(new java.awt.Dimension(250, 180));
        pane.add(card2);

        card3.setColor1(new java.awt.Color(15, 82, 186));
        card3.setColor2(new java.awt.Color(40, 159, 255));
        card3.setPreferredSize(new java.awt.Dimension(250, 180));
        pane.add(card3);

        panelBorder1.setBackground(new java.awt.Color(237, 249, 255));

        card4.setColor1(new java.awt.Color(186, 99, 15));
        card4.setColor2(new java.awt.Color(238, 137, 40));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/scrambIm.png"))); // NOI18N

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBorder1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(pane, javax.swing.GroupLayout.PREFERRED_SIZE, 851, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private sales.Card card1;
    private sales.Card card2;
    private sales.Card card3;
    private sales.Card card4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane pane;
    private homepage.PanelBorder panelBorder1;
    // End of variables declaration//GEN-END:variables
}
