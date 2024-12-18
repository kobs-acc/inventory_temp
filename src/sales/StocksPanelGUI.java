package sales;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.DefaultTableModel;

class Item {
    String id, company, name, qty, price, tax, ourPrice, category;

    public Item(String id, String company, String name, String qty, 
                String price, String tax, String ourPrice, String category) {
        this.id = id;
        this.company = company;
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.tax = tax;
        this.ourPrice = ourPrice;
        this.category = category;
    }


    @Override
    public String toString() {
        return id + " - " + name + " (" + company + ") - Qty: " + qty;
    }

    // Method to save item to inventory file
    public void saveToInventoryFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/reception/inventory.txt", true))) {
            writer.write(name + "," + qty + "," + price + "," + id + "," + category + "," + company + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving item to inventory: " + e.getMessage());
        }
    }

    // Static method to load items from inventory file
    public static List<Item> loadItemsFromFile() {
        List<Item> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/reception/inventory.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    items.add(new Item(
                        parts[3],  // id
                        parts[5],  // company
                        parts[0],  // name
                        parts[1],  // qty
                        parts[2],  // price
                        "0%",      // default tax
                        parts[2],  // ourPrice (same as price)
                        parts[4]   // category
                    ));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading inventory: " + e.getMessage());
        }
        return items;
    }
}


public class StocksPanelGUI extends JPanel {
    private List<Item> items;
    private JTable stocksTable;
    private DefaultTableModel tableModel;

    public StocksPanelGUI() {
        setLayout(new BorderLayout());
        
        // Load items from file
        items = Item.loadItemsFromFile();

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        
        // Create buttons
        JButton addBtn = new JButton("Add Item");
        JButton updateBtn = new JButton("Update Item");
        JButton removeBtn = new JButton("Remove Item");
        JButton refreshBtn = new JButton("Refresh Stocks");

        // Add action listeners
        addBtn.addActionListener(e -> showAddItemPanel());
        updateBtn.addActionListener(e -> showUpdateItemPanel());
        removeBtn.addActionListener(e -> showRemoveItemPanel());
        refreshBtn.addActionListener(e -> refreshStocksTable());

        // Add buttons to button panel
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(refreshBtn);

        // Create table model
        String[] columnNames = {"Product ID", "Name", "Company", "Quantity", "Price", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0);
        stocksTable = new JTable(tableModel);
        
        // Populate table
        populateStocksTable();

        // Make table cells editable
        stocksTable.setCellSelectionEnabled(true);
        stocksTable.setSurrendersFocusOnKeystroke(true);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(stocksTable);

        // Add components to main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add main panel to this panel
        add(mainPanel, BorderLayout.CENTER);
    }

    private void populateStocksTable() {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        // Add items to table
        for (Item item : items) {
            tableModel.addRow(new Object[]{
                item.id, 
                item.name, 
                item.company, 
                item.qty, 
                item.price, 
                item.category
            });
        }
    }

    private void refreshStocksTable() {
        // Reload items from file
        items = Item.loadItemsFromFile();
        populateStocksTable();
    }

    private void showAddItemPanel() {
        JFrame addFrame = new JFrame("Add Item Panel");
        addFrame.setSize(500, 500);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setLayout(new GridLayout(9, 2, 10, 10));

        JTextField idField = new JTextField();
        JTextField companyField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox<String> taxCombo = new JComboBox<>(new String[]{"0%", "5%", "10%", "15%"});
        JTextField ourPriceField = new JTextField();
        JTextField categoryField = new JTextField();

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            String id = idField.getText();
            String company = companyField.getText();
            String name = nameField.getText();
            String qty = qtyField.getText();
            String price = priceField.getText();
            String tax = (String) taxCombo.getSelectedItem();
            String ourPrice = ourPriceField.getText();
            String category = categoryField.getText();

            if (id.isEmpty() || company.isEmpty() || name.isEmpty() || qty.isEmpty() 
                || price.isEmpty() || ourPrice.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(addFrame, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Item newItem = new Item(id, company, name, qty, price, tax, ourPrice, category);
                items.add(newItem);
                newItem.saveToInventoryFile();
                JOptionPane.showMessageDialog(addFrame, "Item added successfully.");
                refreshStocksTable();
                addFrame.dispose();
            }
        });

        // Add fields to frame (same as before)
        addFrame.add(new JLabel("Product Id:"));
        addFrame.add(idField);
        addFrame.add(new JLabel("Product Company:"));
        addFrame.add(companyField);
        addFrame.add(new JLabel("Product Name:"));
        addFrame.add(nameField);
        addFrame.add(new JLabel("Quantity:"));
        addFrame.add(qtyField);
        addFrame.add(new JLabel("Product Price:"));
        addFrame.add(priceField);
        addFrame.add(new JLabel("Tax:"));
        addFrame.add(taxCombo);
        addFrame.add(new JLabel("Our Price:"));
        addFrame.add(ourPriceField);
        addFrame.add(new JLabel("Category:"));
        addFrame.add(categoryField);
        addFrame.add(addBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> addFrame.dispose());
        addFrame.add(cancelBtn);

        addFrame.setVisible(true);
    }

    private void showUpdateItemPanel() {
        // Get selected row in the table
        int selectedRow = stocksTable.getSelectedRow();

        if (selectedRow != -1) {
            // Retrieve the values from the selected row
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            String company = (String) tableModel.getValueAt(selectedRow, 2);
            String name = (String) tableModel.getValueAt(selectedRow, 1);
            String qty = (String) tableModel.getValueAt(selectedRow, 3);
            String price = (String) tableModel.getValueAt(selectedRow, 4);
            String category = (String) tableModel.getValueAt(selectedRow, 5);

            // Create an update panel with text fields populated with the current values
            JFrame updateFrame = new JFrame("Update Item");
            updateFrame.setSize(400, 400);
            updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            updateFrame.setLayout(new GridLayout(7, 2, 10, 10));

            JTextField nameField = new JTextField(name);
            JTextField qtyField = new JTextField(qty);
            JTextField priceField = new JTextField(price);
            JTextField categoryField = new JTextField(category);

            JButton saveBtn = new JButton("Save Changes");
            saveBtn.addActionListener(e -> {
                // Update values in table model and in item list
                tableModel.setValueAt(nameField.getText(), selectedRow, 1);
                tableModel.setValueAt(qtyField.getText(), selectedRow, 3);
                tableModel.setValueAt(priceField.getText(), selectedRow, 4);
                tableModel.setValueAt(categoryField.getText(), selectedRow, 5);

                // Find the item in the list and update it
                Item updatedItem = items.get(selectedRow);
                updatedItem.name = nameField.getText();
                updatedItem.qty = qtyField.getText();
                updatedItem.price = priceField.getText();
                updatedItem.category = categoryField.getText();

                // Save the updated item to the inventory file (or overwrite the whole file)
                // For simplicity, we could reload items after updating, but ideally, you should update the file
                updatedItem.saveToInventoryFile();

                JOptionPane.showMessageDialog(updateFrame, "Item updated successfully.");
                updateFrame.dispose();
                refreshStocksTable();
            });

            updateFrame.add(new JLabel("Product Name:"));
            updateFrame.add(nameField);
            updateFrame.add(new JLabel("Quantity:"));
            updateFrame.add(qtyField);
            updateFrame.add(new JLabel("Price:"));
            updateFrame.add(priceField);
            updateFrame.add(new JLabel("Category:"));
            updateFrame.add(categoryField);
            updateFrame.add(saveBtn);

            updateFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRemoveItemPanel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

