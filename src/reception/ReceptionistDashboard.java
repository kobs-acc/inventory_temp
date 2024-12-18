package reception;


import javax.swing.*;
import java.awt.*;

public class ReceptionistDashboard{
    //notes: #0054B4, #D4DFE2, white and black
    //attributes
    JFrame recepFrame;

    //contructor
    public ReceptionistDashboard(){
        //main frame stuff
        recepFrame = new JFrame("StockTrack: Supermarket Inventory System");
        recepFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recepFrame.setResizable(false);
        recepFrame.setSize(600,450);
        recepFrame.setLayout(new BorderLayout());

        //header panel stuff
        JPanel headPanel = new JPanel();
        headPanel.setBackground(new Color(0x0054B4));
        headPanel.setPreferredSize(new Dimension(600, 60));
        headPanel.setLayout(new BorderLayout());
        headPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Receptionist Dashboard");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headPanel.add(titleLabel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("LOG OUT");
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        logoutButton.setBackground(new Color(0x5085C1));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        headPanel.add(logoutButton, BorderLayout.EAST);

        //Left panel stuff
        ImageIcon recep = new ImageIcon("src/reception/recep.png");

        JPanel leftPanel = new JPanel(null);
        leftPanel.setPreferredSize(new Dimension(200, 340));
        leftPanel.setBackground(new Color(0xF1F3F5));
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(0xD4DFE2)));
        
        JLabel placeholderLabel = new JLabel();
        placeholderLabel.setIcon(recep);
        placeholderLabel.setBounds(25, 50, 150, 150);
        placeholderLabel.setBackground(Color.WHITE);
        placeholderLabel.setForeground(new Color(0x0054B4));
        placeholderLabel.setBorder(BorderFactory.createLineBorder(new Color(0xD4DFE2), 2));
        leftPanel.add(placeholderLabel);

        JLabel welcomeLabel = new JLabel("Welcome", SwingConstants.CENTER);
        welcomeLabel.setBounds(25, 220, 150, 30);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(new Color(0x0054B4));
        leftPanel.add(welcomeLabel);

        JLabel billingInfoLabel = new JLabel("<html>Click on <b>Billing Section</b><br>to manage bills</html>", SwingConstants.CENTER);
        billingInfoLabel.setBounds(25, 250, 150, 40);
        billingInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        billingInfoLabel.setForeground(new Color(0x555555));
        leftPanel.add(billingInfoLabel);

        JLabel ordersInfoLabel = new JLabel("<html>Click on <b>View Orders</b><br>to check your orders</html>", SwingConstants.CENTER);
        ordersInfoLabel.setBounds(25, 290, 150, 40);
        ordersInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ordersInfoLabel.setForeground(new Color(0x555555));
        leftPanel.add(ordersInfoLabel);

        //panels for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        buttonPanel.setBackground(new Color(0xF1F3F5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JButton billingButton = createStyledButton("Billing Section");
        buttonPanel.add(billingButton);

        JButton viewOrdersButton = createStyledButton("View Orders");
        buttonPanel.add(viewOrdersButton);

        //add panels to main frame
        recepFrame.add(headPanel, BorderLayout.NORTH);
        recepFrame.add(leftPanel, BorderLayout.WEST);
        recepFrame.add(buttonPanel, BorderLayout.CENTER);

        //buttons actions
        billingButton.addActionListener(click -> goBillingFrame());
        viewOrdersButton.addActionListener(click -> goOrdersFrame());

        //make the main frame of the reception visible
        recepFrame.setVisible(true);
        logoutButton.addActionListener(click -> logout());

    }
    //method to jump to the billing frame
    private void goBillingFrame(){
        recepFrame.dispose();
        new BillingFrame();
    }

    //method to jump to the view orders frame
    private void goOrdersFrame(){
        recepFrame.dispose();
        new ViewOrdersFrame();
    }

    //method for styling the buttons
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(0x5085C1));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x0054B4), 5),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return button;
    }
    
        private void logout() {
        recepFrame.dispose(); // Close ReceptionistDashboard
        new login.Login().setVisible(true); // Open Login screen
    }
}
