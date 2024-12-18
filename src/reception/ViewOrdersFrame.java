package  reception;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class ViewOrdersFrame {
    private JFrame viewOrdersFrame;
    private JButton backButton;
    private JButton logoutButton;
    private JComboBox<String> orderIdComboBox;
    private JTable ordersTable;
    private JLabel totalPriceLabel;
    private DefaultTableModel tableModel;

    public ViewOrdersFrame() {
        //orders main frame stuff
        viewOrdersFrame = new JFrame("View Orders");
        viewOrdersFrame.setSize(800, 500);
        viewOrdersFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewOrdersFrame.setResizable(false);
        viewOrdersFrame.setLayout(new BorderLayout());

        //head panel stuff
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0x0054B4));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //stuff to add in head panel
        JLabel titleLabel = new JLabel("View Orders");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        //buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(0x0054B4));

        //buttons
        backButton = createStyledButton("Back");
        logoutButton = createStyledButton("Log out");

        //add them to the buttons panel
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        //center panel stuff
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //order key/id panel
        JPanel orderIdPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel orderIdLabel = new JLabel("Select Order Key");
        orderIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        //drop down or combobox stuff
        orderIdComboBox = new JComboBox<>();
        orderIdComboBox.setPreferredSize(new Dimension(150, 25));

        orderIdPanel.add(orderIdLabel);
        orderIdPanel.add(orderIdComboBox);
        centerPanel.add(orderIdPanel, BorderLayout.NORTH);

        //table for order details
        String[] columnNames = {"Item Name", "Quantity", "Price", "Product Key"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ordersTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(ordersTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x0054B4), 2));
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        //total price 
        totalPriceLabel = new JLabel("Total Price: 0.0");
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        centerPanel.add(totalPriceLabel, BorderLayout.SOUTH);

        //add panels to frame
        viewOrdersFrame.add(headerPanel, BorderLayout.NORTH);
        viewOrdersFrame.add(centerPanel, BorderLayout.CENTER);

        //load the orders from file
        loadOrderKeys();

        //button actions
        backButton.addActionListener(click -> goBack());
        orderIdComboBox.addActionListener(click -> loadOrderDetails());

        //make visible orders frame
        viewOrdersFrame.setVisible(true);
        logoutButton.addActionListener(click -> logout());
    }

    //method to go back
    private void goBack(){
        viewOrdersFrame.dispose();
        new ReceptionistDashboard();
    }

    //method for styling buttons
    private JButton createStyledButton(String text){
        JButton button = new JButton(text);
        button.setBackground(new Color(0x5085C1));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return button;
    }

    // method for loading the order key to dropdown
    private void loadOrderKeys(){
        try(BufferedReader reader = new BufferedReader(new FileReader("src/reception/orders.txt"))){
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().matches("\\d+")) {      //match order keys
                    orderIdComboBox.addItem(line.trim());
                }
            }
        } catch(IOException ex){
            JOptionPane.showMessageDialog(viewOrdersFrame, "Error reading orders file.");
        }
    }

    // method for showing order details
    private void loadOrderDetails(){
        String selectedOrderKey = (String) orderIdComboBox.getSelectedItem();
        if (selectedOrderKey == null) return;

        tableModel.setRowCount(0);              //clears the table
        double totalPrice = 0;

        try(BufferedReader reader = new BufferedReader(new FileReader("src/reception/orders.txt"))){
            String line;
            boolean isOrderFound = false;

            while((line = reader.readLine()) != null){
                line = line.trim();

                //start reading the order when the key matches
                if(line.equals(selectedOrderKey)){
                    isOrderFound = true;
                    continue;
                }

                //if a total price is reached, stop reading
                if(isOrderFound && line.matches("\\d+\\.\\d+")){
                    totalPrice = Double.parseDouble(line);
                    break;
                }

                // Add items to the table
                if(isOrderFound && line.contains(",")){
                    String[] parts = line.split(",");
                    if(parts.length >= 4){
                        tableModel.addRow(new Object[]{
                            parts[0], // Item Name
                            parts[1], // Quantity
                            parts[2], // Price
                            parts[3]  // Product Key
                        });
                    }
                }
            }

            //update total price
            totalPriceLabel.setText("Total Price: " + String.format("%.2f", totalPrice));

        } catch(IOException ex){
            JOptionPane.showMessageDialog(viewOrdersFrame, "Error loading order details.");
        }
    }
    // Method to handle logout
    private void logout() {
        viewOrdersFrame.dispose(); // Close ViewOrdersFrame
        new login.Login().setVisible(true); // Open Login screen
    }
}
