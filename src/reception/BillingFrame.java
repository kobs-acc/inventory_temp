import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Random;

public class BillingFrame{
    //attributes/instance variables
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

    //for files handling
    private static final String INVENTORY_FILE = "inventory.txt";
    private static final String ORDERS_FILE = "orders.txt";

    //constructor
    public BillingFrame(){
        //main billing frame stuff tapos set up
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
        buttonPanel.add(backButton);
        buttonPanel.add(logoutButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        //center Panel for the table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
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
        String[] columnNames = {"Product ID", "Product Name", "Our Price", "Quantity", "Company"};
        tableModel = new DefaultTableModel(columnNames, 0);
        inventoryTable = new JTable(tableModel);
        inventoryTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE); 
        
        //table actions (checks if the quantity is changed)
        inventoryTable.getModel().addTableModelListener(e -> updateTotalPrice());

        JScrollPane tableScrollPane = new JScrollPane(inventoryTable);
        tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x0054B4), 2));
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        //bottom Panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(0x0054B4));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //generate bills and delete buttons
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionButtonsPanel.setBackground(new Color(0x0054B4));

        generateBillButton = createStyledButton("Generate Bill");
        deleteButton = createStyledButton("Delete");
        actionButtonsPanel.add(generateBillButton);
        actionButtonsPanel.add(deleteButton);
        footerPanel.add(actionButtonsPanel, BorderLayout.WEST);

        //total label for showing the overall total of the order
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

        //make the billing main frame visible
        billingFrame.setVisible(true);
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

    //method for search product key
    private void searchProductByKey(){
        String inputKey = productIdField.getText().trim();
        if(inputKey.isEmpty()){
            JOptionPane.showMessageDialog(billingFrame, "Please enter a product key.");
            return;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(INVENTORY_FILE))){
            String line;
            boolean found = false;

            while((line = br.readLine()) != null){
                String[] data = line.split(",");

                if(data.length == 6 && data[0].equals(inputKey)){              //check kung match ID
                    tableModel.addRow(new Object[]{data[0], data[1], data[3], 1, data[5]});
                    found = true;
                    break;
                }
            }

            if(!found){
                JOptionPane.showMessageDialog(billingFrame, "Product key not found.");
            }

        }
        catch(IOException ex){
            JOptionPane.showMessageDialog(billingFrame, "Error reading inventory file: " + ex.getMessage());
        }
    }

    //method for updating the total price whenever it is changed in the table
    private void updateTotalPrice(){
        totalPrice = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            double price = Double.parseDouble(tableModel.getValueAt(i, 2).toString());
            int quantity = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
            totalPrice += price * quantity;
        }
        totalLabel.setText("Total: P" + totalPrice);
    }

    //method for generating bill
    private void generateBill(){
        if(tableModel.getRowCount() == 0){
            JOptionPane.showMessageDialog(billingFrame, "No items in the order to generate a bill.");
            return;
        }

        int orderKey = generateOrderKey();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE, true))){
            writer.write(orderKey + "\n");                                     //writes the order key first

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write(tableModel.getValueAt(i, 0) + ","          //writes the Product ID
                        + tableModel.getValueAt(i, 1) + ","             //writes the Product Name
                        + tableModel.getValueAt(i, 2) + ","             //writes the Price
                        + tableModel.getValueAt(i, 3) + ","             //writes the Quantity
                        + tableModel.getValueAt(i, 4) + "\n");          //writes the Company
            }

            writer.write(totalPrice + "\n\n");                                  //writes the total amount

            JOptionPane.showMessageDialog(billingFrame, "Bill Generated...Order Key: " + orderKey);
        }
        catch(IOException ex){
            JOptionPane.showMessageDialog(billingFrame, "Error writing to orders file: " + ex.getMessage());
        }
    }

    //method for deleting product
    private void deleteSelectedProduct(){
        int selectedRow = inventoryTable.getSelectedRow();
        if(selectedRow != -1){
            tableModel.removeRow(selectedRow);
            updateTotalPrice();
        } 
        else{
            JOptionPane.showMessageDialog(billingFrame, "Please select a product to delete.");
        }
    }

    //method for generating four digit order key
    private int generateOrderKey(){
        Random rand = new Random();
        return 1000 + rand.nextInt(9000);
    }

    //method to go back
    private void goBack(){
        billingFrame.dispose();
        new ReceptionistDashboard();
    }

    //main method (used for testing - delete nalangs)
    public static void main(String[] args){
        new BillingFrame();
    }
}
