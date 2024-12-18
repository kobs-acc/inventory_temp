package  reception;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Random;

public class BillingFrame{
    
    //attributes
    private JFrame billingFrame;
    private JButton backButton;
    private JButton logoutButton;
    private JButton generateBillButton;
    private JButton deleteButton;
    private JTextField productIdField;
    private JTable inventoryTable;
    private JLabel totalLabel;

    private DefaultTableModel tableModel;
    private double totalPrice = 0.0;

    //files
    private static final String INVENTORY_FILE = "inventory.txt";
    private static final String ORDERS_FILE = "orders.txt";

    //constructor
    public BillingFrame(){
        //main billing frame (main frame)
        billingFrame = new JFrame("StockTrack: Supermarket Inventory System");
        billingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        billingFrame.setSize(800, 500);
        billingFrame.setResizable(false);
        billingFrame.setLayout(new BorderLayout());

        //header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0x0054B4));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Billing Option");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        //buttons for logout and back
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(0x0054B4));

        backButton = createStyledButton("BACK");
        logoutButton = createStyledButton("LOG OUT");
        
        //add them to the header panel
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        //center Panel for the table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(0xFFFFFF));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //product key input
        JPanel productPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        productPanel.setBackground(Color.WHITE);

        JLabel productIdLabel = new JLabel("Product Key:");
        productIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        productIdField = new JTextField(15);
        productIdField.setBorder(BorderFactory.createLineBorder(new Color(0x0054B4), 2));
        productPanel.add(productIdLabel);
        productPanel.add(productIdField);

        centerPanel.add(productPanel, BorderLayout.NORTH);

        //table for inventory orders
        String[] columnNames = {"Item Name", "Quantity", "Price", "Product Key"};
        tableModel = new DefaultTableModel(columnNames, 0);
        inventoryTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x0054B4), 2));
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        //bottom panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(0x0054B4));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //generate and delete buttons
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionButtonsPanel.setBackground(new Color(0x0054B4));

        generateBillButton = createStyledButton("Generate Bill");
        deleteButton = createStyledButton("Delete");

        //add them to the button panel
        actionButtonsPanel.add(generateBillButton);
        actionButtonsPanel.add(deleteButton);

        //add the button panel to footer panel
        footerPanel.add(actionButtonsPanel, BorderLayout.WEST);

        //total
        totalLabel = new JLabel("Total: P0.0");
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        footerPanel.add(totalLabel, BorderLayout.EAST);

        //add panels to main billing frame
        billingFrame.add(headerPanel, BorderLayout.NORTH);
        billingFrame.add(centerPanel, BorderLayout.CENTER);
        billingFrame.add(footerPanel, BorderLayout.SOUTH);

        //button actions
        backButton.addActionListener(click -> goBack());
        productIdField.addActionListener(click -> searchProductByKey());
        generateBillButton.addActionListener(click -> generateBill());
        deleteButton.addActionListener(click -> deleteSelectedProduct());

        //billing main frame visible
        billingFrame.setVisible(true);
        logoutButton.addActionListener(click -> logout());
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

    //method for searching product key and adding it to the order
    private void searchProductByKey(){
        String inputKey = productIdField.getText().trim();
            if(inputKey.isEmpty()){
                JOptionPane.showMessageDialog(billingFrame, "Please enter a product key.");
                return;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            boolean found = false;

            while((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if(data.length >= 4 && data[3].equals(inputKey)) { // Product key matches
                    // Name, Quantity, Price, Product Key, Category, Supplier
                    tableModel.addRow(new Object[]{
                        data[0],      // Item Name
                        "1",           // Default to 1 quantity
                        data[2],       // Price
                        data[3]        // Product Key
                    });
                    totalPrice += Double.parseDouble(data[2]); //Update total price
                    totalLabel.setText("Total: P" + String.format("%.2f", totalPrice));
                    found = true;
                    break;
                }
            }

            if(!found){
                JOptionPane.showMessageDialog(billingFrame, "Product key not found.");
            }

        } catch(IOException ex){
            JOptionPane.showMessageDialog(billingFrame, "Error reading inventory file: " + ex.getMessage());
        }
    }

    //method for generating bill
    private void generateBill(){
        if(tableModel.getRowCount() == 0){
            JOptionPane.showMessageDialog(billingFrame, "No items in the order to generate a bill.");
            return;
        }

        int orderKey = generateOrderKey();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE, true))) {
            writer.write(orderKey + "\n");              //writes the order key first

            //then wrties the products next
            for(int i = 0; i < tableModel.getRowCount(); i++){
                writer.write(tableModel.getValueAt(i, 0) + "," 
                        + tableModel.getValueAt(i, 1) + ","   
                        + tableModel.getValueAt(i, 2) + ","   
                        + tableModel.getValueAt(i, 3) + "\n");
            }

            writer.write(totalPrice + "\n\n");             //last writes the total in the txt last
            JOptionPane.showMessageDialog(billingFrame, "Bill Generated...Order Key: " + orderKey);

        }
        catch(IOException ex){
            JOptionPane.showMessageDialog(billingFrame, "Error writing to orders file: " + ex.getMessage());
        }
    }

    //method for delete button
    private void deleteSelectedProduct(){
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow != -1) {
            double price = Double.parseDouble(tableModel.getValueAt(selectedRow, 2).toString());
            totalPrice -= price; 
            tableModel.removeRow(selectedRow);
            totalLabel.setText("Total: P" + totalPrice);
        } else {
            JOptionPane.showMessageDialog(billingFrame, "Please select a product to delete.");
        }
    }

    //method to generate 4 digit unique order key
    private int generateOrderKey(){
        Random rand = new Random();
        return 1000 + rand.nextInt(9000); // Generates random number between 1000 and 9999
    }

    //method for going back
    private void goBack(){
        billingFrame.dispose();
        new ReceptionistDashboard();
    }
    // Method to handle logout
    private void logout() {
        billingFrame.dispose(); // Close BillingFrame
        new login.Login().setVisible(true); // Open Login screen
    }
    
    
}
