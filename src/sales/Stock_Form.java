
package sales;

/**
 *
 * @author Joy De Castro
 */
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;



public class Stock_Form extends javax.swing.JPanel {

    private static final String INVENTORY_FILE_PATH = "src/reception/inventory.txt";

    public Stock_Form() {
        initComponents();
        setupListeners();
        refreshTableData();
    }
    
    // Set up listeners for buttons
    private void setupListeners() {
        AddBtn.addActionListener(evt -> handleAddBtn());
        UpdateBtn.addActionListener(evt -> handleUpdateBtn());
        DeleteBtn.addActionListener(evt -> handleDeleteBtn());
        ClearBtn.addActionListener(evt -> clearFields());
        RefreshBtn.addActionListener(evt -> refreshTableData());
        NewRadBtn.addActionListener(evt -> handleNewRadBtn());
        stocks_Table1.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                populateFieldsFromTable();
            }
        });
    }
    
    // Read inventory from file
    private List<String[]> readInventoryFromFile() throws IOException {
        List<String[]> inventory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // Ensure consistent data format
                if (data.length >= 5) {
                    inventory.add(data);
                }
            }
        }
        return inventory;
    }
    
   
    
    // Write inventory to file
    private void writeInventoryToFile(List<String[]> inventory) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTORY_FILE_PATH))) {
            for (String[] product : inventory) {
                writer.write(String.join(",", product) + "\n");
            }
        }
    }
    
    // Validate if a string is numeric (int or float)
    private boolean isNumeric(String value) {
        try {
            Double.parseDouble(value); // Checks for both integers and floats
            return true;
        } catch (NumberFormatException e) {
            return false; // Not a valid number
        }
    }

    // Validate fields for numeric values
    private boolean validateNumericFields() {
        if (!isNumeric(ProductPriceTxtField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Product Price must be a valid number.");
            return false;
        }
        if (!isNumeric(ProductOurPriceTxtField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Our Price must be a valid number.");
            return false;
        }
        if (!isNumeric(QuantityTxtField.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Quantity must be a valid number.");
            return false;
        }
        return true;
    }
    
    
    // Handle Update Product Button
    private void handleUpdateBtn() {
        try {
            // Validate numeric fields
            if (!validateNumericFields()) {
                return; // If validation fails, stop further processing
            }

            String productID = ProductIDTxtField.getText().trim();
            String productName = ProductNameTxtField.getText().trim();
            String productPrice = ProductPriceTxtField.getText().trim(); // Treat as String
            String ourPrice = ProductOurPriceTxtField.getText().trim(); // Treat as String
            String quantity = QuantityTxtField.getText().trim(); // Treat as String
            String company = CompanyTxtField.getText().trim();

            if (productID.isEmpty() || productName.isEmpty() || company.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                return;
            }

            List<String[]> inventory = readInventoryFromFile();
            boolean found = false;

            for (String[] product : inventory) {
                if (product[0].equals(productID)) {
                    product[1] = productName;
                    product[2] = productPrice;
                    product[3] = ourPrice;
                    product[4] = quantity;
                    product[5] = company;
                    found = true;
                    break;
                }
            }

            if (found) {
                writeInventoryToFile(inventory);
                refreshTableData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Product ID not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage());
        }
    }
    
    // Handle Add Product Button
    private void handleAddBtn() {
        try {
            // Validate numeric fields
            if (!validateNumericFields()) {
                return; // If validation fails, stop further processing
            }

            String productName = ProductNameTxtField.getText().trim();
            String productPrice = ProductPriceTxtField.getText().trim(); // Treat as String
            String ourPrice = ProductOurPriceTxtField.getText().trim(); // Treat as String
            String quantity = QuantityTxtField.getText().trim(); // Treat as String
            String company = CompanyTxtField.getText().trim();

            if (productName.isEmpty() || company.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!");
                return;
            }

            List<String[]> inventory = readInventoryFromFile();
            String productID = ProductIDTxtField.getText().trim();

            // Check for duplicate ProductID
            for (String[] product : inventory) {
                if (product[0].equals(productID)) {
                    JOptionPane.showMessageDialog(this, "Duplicate Product ID found!");
                    NewRadBtn.setSelected(true); // Trigger new ProductID generation
                    handleNewRadBtn();
                    return;
                }
            }

            String[] newProduct = {
                productID,
                productName,
                productPrice,
                ourPrice,
                quantity,
                company
            };

            inventory.add(newProduct);
            writeInventoryToFile(inventory);
            refreshTableData();
            clearFields();

            JOptionPane.showMessageDialog(this, "Product added successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage());
        }
    }


    
    // Handle delete product button
    private void handleDeleteBtn() {
        try {
            String productID = ProductIDTxtField.getText().trim();

            if (productID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Product ID to delete.");
                return;
            }

            List<String[]> inventory = readInventoryFromFile();
            boolean removed = inventory.removeIf(product -> product[0].equals(productID));

            if (removed) {
                writeInventoryToFile(inventory);
                refreshTableData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Product ID not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage());
        }
    }
    
    
    // Populate fields from selected table row
    private void populateFieldsFromTable() {
        int selectedRow = stocks_Table1.getTable().getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) stocks_Table1.getTable().getModel();
            ProductIDTxtField.setText(model.getValueAt(selectedRow, 0).toString());
            ProductNameTxtField.setText(model.getValueAt(selectedRow, 1).toString());
            ProductPriceTxtField.setText(model.getValueAt(selectedRow, 2).toString());
            ProductOurPriceTxtField.setText(model.getValueAt(selectedRow, 3).toString());
            QuantityTxtField.setText(model.getValueAt(selectedRow, 4).toString());
            CompanyTxtField.setText(model.getValueAt(selectedRow, 5).toString());
        }
    }
    
    
    // Clear all input fields
    private void clearFields() {
        ProductIDTxtField.setText("");
        ProductNameTxtField.setText("");
        ProductPriceTxtField.setText("");
        ProductOurPriceTxtField.setText("");
        QuantityTxtField.setText("");
        CompanyTxtField.setText("");
    }

    // Refresh table data from inventory file
    private void refreshTableData() {
        try {
            List<String[]> inventory = readInventoryFromFile();
            DefaultTableModel model = (DefaultTableModel) stocks_Table1.getTable().getModel();
            model.setRowCount(0);

            for (String[] product : inventory) {
                model.addRow(product);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error refreshing table: " + e.getMessage());
        }
    }

    // New Radio Button Logic
    private void handleNewRadBtn() {
        try {
            List<String[]> inventory = readInventoryFromFile();

            if (NewRadBtn.isSelected()) {
                // If ProductID field has a value, clear it first
                ProductIDTxtField.setText("");
                String newProductID = generateProductID(inventory);
                ProductIDTxtField.setText(newProductID); // Show the new ProductID in the text field
                ProductIDTxtField.setEditable(false); // Prevent manual editing
            } else {
                ProductIDTxtField.setEditable(true); // Allow manual editing if unselected
                ProductIDTxtField.setText(""); // Clear the text field
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error generating Product ID: " + e.getMessage());
        }
    }

    // Generate unique Product ID
    private String generateProductID(List<String[]> inventory) {
        Random random = new Random();
        String productID;
        boolean isDuplicate;

        do {
            productID = String.valueOf(100000 + random.nextInt(900000)); // Generate 6-digit random ID
            isDuplicate = false;

            // Check for duplicate in inventory
            for (String[] product : inventory) {
                if (product.length > 0 && product[0].trim().equals(productID.trim())) {
                    isDuplicate = true;
                    break;
                }
            }
        } while (isDuplicate);

        return productID;
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        ProductLabel = new javax.swing.JLabel();
        ProductNameLabel = new javax.swing.JLabel();
        ProductIDTxtField = new javax.swing.JTextField();
        ProductNameTxtField = new javax.swing.JTextField();
        PriceLabel = new javax.swing.JLabel();
        ProductOurPriceTxtField = new javax.swing.JTextField();
        CompanyLabel = new javax.swing.JLabel();
        AddBtn = new javax.swing.JButton();
        ClearBtn = new javax.swing.JButton();
        DeleteBtn = new javax.swing.JButton();
        UpdateBtn = new javax.swing.JButton();
        RefreshBtn = new javax.swing.JButton();
        OurPriceLabel = new javax.swing.JLabel();
        ProductPriceTxtField = new javax.swing.JTextField();
        QuantityLabel = new javax.swing.JLabel();
        QuantityTxtField = new javax.swing.JTextField();
        CompanyTxtField = new javax.swing.JTextField();
        NewRadBtn = new javax.swing.JRadioButton();
        stocks_Table1 = new sales.stocks_Table();

        setBackground(new java.awt.Color(237, 249, 255));
        setPreferredSize(new java.awt.Dimension(900, 600));

        jPanel2.setOpaque(false);

        ProductLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        ProductLabel.setForeground(new java.awt.Color(0, 0, 0));
        ProductLabel.setText("Product ID:");

        ProductNameLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        ProductNameLabel.setForeground(new java.awt.Color(0, 0, 0));
        ProductNameLabel.setText("Product Name:");

        ProductIDTxtField.setBackground(new java.awt.Color(255, 255, 255));
        ProductIDTxtField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ProductIDTxtField.setCaretColor(new java.awt.Color(255, 255, 255));
        ProductIDTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductIDTxtFieldActionPerformed(evt);
            }
        });

        ProductNameTxtField.setBackground(new java.awt.Color(255, 255, 255));
        ProductNameTxtField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ProductNameTxtField.setCaretColor(new java.awt.Color(255, 255, 255));
        ProductNameTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductNameTxtFieldActionPerformed(evt);
            }
        });

        PriceLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        PriceLabel.setForeground(new java.awt.Color(0, 0, 0));
        PriceLabel.setText("Product Price:");

        ProductOurPriceTxtField.setBackground(new java.awt.Color(255, 255, 255));
        ProductOurPriceTxtField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ProductOurPriceTxtField.setCaretColor(new java.awt.Color(255, 255, 255));
        ProductOurPriceTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductOurPriceTxtFieldActionPerformed(evt);
            }
        });

        CompanyLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        CompanyLabel.setForeground(new java.awt.Color(0, 0, 0));
        CompanyLabel.setText("Product Company:");

        AddBtn.setBackground(new java.awt.Color(10, 104, 235));
        AddBtn.setFont(new java.awt.Font("Google Sans Display", 1, 18)); // NOI18N
        AddBtn.setForeground(new java.awt.Color(255, 255, 255));
        AddBtn.setText("Add");
        AddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddBtnActionPerformed(evt);
            }
        });

        ClearBtn.setBackground(new java.awt.Color(186, 15, 82));
        ClearBtn.setFont(new java.awt.Font("Google Sans Display", 1, 18)); // NOI18N
        ClearBtn.setForeground(new java.awt.Color(255, 255, 255));
        ClearBtn.setText("Clear");
        ClearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearBtnActionPerformed(evt);
            }
        });

        DeleteBtn.setBackground(new java.awt.Color(186, 15, 167));
        DeleteBtn.setFont(new java.awt.Font("Google Sans Display", 1, 18)); // NOI18N
        DeleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        DeleteBtn.setText("Delete");
        DeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteBtnActionPerformed(evt);
            }
        });

        UpdateBtn.setBackground(new java.awt.Color(82, 186, 15));
        UpdateBtn.setFont(new java.awt.Font("Google Sans Display", 1, 18)); // NOI18N
        UpdateBtn.setForeground(new java.awt.Color(255, 255, 255));
        UpdateBtn.setText("Update");
        UpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateBtnActionPerformed(evt);
            }
        });

        RefreshBtn.setBackground(new java.awt.Color(17, 45, 90));
        RefreshBtn.setFont(new java.awt.Font("Google Sans Display", 1, 18)); // NOI18N
        RefreshBtn.setForeground(new java.awt.Color(255, 255, 255));
        RefreshBtn.setText("Refresh");
        RefreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshBtnActionPerformed(evt);
            }
        });

        OurPriceLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        OurPriceLabel.setForeground(new java.awt.Color(0, 0, 0));
        OurPriceLabel.setText("Our Price: ");

        ProductPriceTxtField.setBackground(new java.awt.Color(255, 255, 255));
        ProductPriceTxtField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ProductPriceTxtField.setCaretColor(new java.awt.Color(255, 255, 255));
        ProductPriceTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductPriceTxtFieldActionPerformed(evt);
            }
        });

        QuantityLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        QuantityLabel.setForeground(new java.awt.Color(0, 0, 0));
        QuantityLabel.setText("Quantity:");

        QuantityTxtField.setBackground(new java.awt.Color(255, 255, 255));
        QuantityTxtField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        QuantityTxtField.setCaretColor(new java.awt.Color(255, 255, 255));
        QuantityTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuantityTxtFieldActionPerformed(evt);
            }
        });

        CompanyTxtField.setBackground(new java.awt.Color(255, 255, 255));
        CompanyTxtField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        CompanyTxtField.setCaretColor(new java.awt.Color(255, 255, 255));
        CompanyTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CompanyTxtFieldActionPerformed(evt);
            }
        });

        NewRadBtn.setFont(new java.awt.Font("Google Sans Display", 0, 12)); // NOI18N
        NewRadBtn.setForeground(new java.awt.Color(0, 0, 0));
        NewRadBtn.setText("New");
        NewRadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewRadBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(ProductLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ProductIDTxtField))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(PriceLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ProductPriceTxtField))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(ProductNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ProductNameTxtField))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(OurPriceLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ProductOurPriceTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(NewRadBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ClearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(UpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CompanyLabel))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(AddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(DeleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addComponent(RefreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(CompanyTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(QuantityLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(QuantityTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(62, 62, 62))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ProductLabel)
                    .addComponent(ProductIDTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(QuantityTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(QuantityLabel)
                    .addComponent(NewRadBtn))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ProductNameTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ProductNameLabel)
                    .addComponent(CompanyTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CompanyLabel))
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PriceLabel)
                    .addComponent(ProductPriceTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ClearBtn)
                    .addComponent(AddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(OurPriceLabel)
                            .addComponent(ProductOurPriceTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(UpdateBtn)
                            .addComponent(DeleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RefreshBtn))))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(stocks_Table1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stocks_Table1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ProductNameTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductNameTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductNameTxtFieldActionPerformed

    private void ProductOurPriceTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductOurPriceTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductOurPriceTxtFieldActionPerformed

    private void ProductIDTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductIDTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductIDTxtFieldActionPerformed

    private void AddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddBtnActionPerformed

    private void ClearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ClearBtnActionPerformed

    private void DeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DeleteBtnActionPerformed

    private void UpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UpdateBtnActionPerformed

    private void RefreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RefreshBtnActionPerformed

    private void ProductPriceTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductPriceTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductPriceTxtFieldActionPerformed

    private void QuantityTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuantityTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_QuantityTxtFieldActionPerformed

    private void CompanyTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CompanyTxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CompanyTxtFieldActionPerformed

    private void NewRadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewRadBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewRadBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddBtn;
    private javax.swing.JButton ClearBtn;
    private javax.swing.JLabel CompanyLabel;
    private javax.swing.JTextField CompanyTxtField;
    private javax.swing.JButton DeleteBtn;
    private javax.swing.JRadioButton NewRadBtn;
    private javax.swing.JLabel OurPriceLabel;
    private javax.swing.JLabel PriceLabel;
    private javax.swing.JTextField ProductIDTxtField;
    private javax.swing.JLabel ProductLabel;
    private javax.swing.JLabel ProductNameLabel;
    private javax.swing.JTextField ProductNameTxtField;
    private javax.swing.JTextField ProductOurPriceTxtField;
    private javax.swing.JTextField ProductPriceTxtField;
    private javax.swing.JLabel QuantityLabel;
    private javax.swing.JTextField QuantityTxtField;
    private javax.swing.JButton RefreshBtn;
    private javax.swing.JButton UpdateBtn;
    private javax.swing.JPanel jPanel2;
    private sales.stocks_Table stocks_Table1;
    // End of variables declaration//GEN-END:variables
}
