
package form;


import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;



public class Employee_Form extends javax.swing.JPanel {

    /**
     * Creates new form Employee_Form
     */
    public Employee_Form() {
        initComponents();
        setupListeners();
        refreshTableData();
    }
    
    // Set up listeners for buttons
    private void setupListeners() {
        NewRadBtn.addActionListener(evt -> handleNewRadioBtn());
        AddBtn.addActionListener(evt -> handleAddBtn());
        UpdateBtn.addActionListener(evt -> handleUpdateBtn());
        DeleteBtn.addActionListener(evt -> handleDeleteBtn());
        ClearBtn1.addActionListener(evt -> clearFields());
        RefreshBtn.addActionListener(evt -> refreshTableData()); // Add this line
        employee_Table1.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                populateFieldsFromTable();
            }
        });
    }
    
    // Disable Employee ID field and generate new ID when NewRadBtn is selected
    private void handleNewRadioBtn() {
        if (NewRadBtn.isSelected()) {
            EmployeeId.setEnabled(false);
            EmployeeId.setText(generateEmployeeID());
        } else {
            EmployeeId.setEnabled(true);
            EmployeeId.setText("");
        }
    }
    
    // Update an existing employee in users.txt
    private void handleUpdateBtn() {
        String empID = EmployeeId.getText();
        String firstName = FirstName.getText();
        String lastName = LastName.getText();
        String gender = GenderComboBox.getSelectedItem().toString();
        String position = PositionComboBox.getSelectedItem().toString();
        String password = new String(PasswordField.getPassword());  // Get the password

        // Comprehensive validation
        if (empID.trim().isEmpty() || 
            firstName.trim().isEmpty() || 
            lastName.trim().isEmpty() || 
            gender.equals("Choose") || 
            position.equals("Choose")) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields correctly!");
            return;
        }
        
        
        // Validate password length if it's not empty
        if (!password.isEmpty() && password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.");
            return;
        }

        try {
            File file = new File("src/user/users.txt");
            List<String[]> users = readUsersFromFile(file);

            // Find and update the employee
            boolean found = false;
            for (String[] user : users) {
                if (user[0].equals(empID)) {
                    user[1] = firstName;
                    user[2] = lastName;
                    user[3] = gender;
                    user[4] = position;
                    if (!password.isEmpty()) {
                        user[5] = password;  // Update the password only if it's not empty
                    }
                    found = true;
                    break;
                }
            }

            if (found) {
                writeUsersToFile(file, users);
                refreshTableData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Employee ID not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    // Add new employee to users.txt
    private void handleAddBtn() {
       String firstName = FirstName.getText();
       String lastName = LastName.getText();
       String gender = GenderComboBox.getSelectedItem().toString();
       String position = PositionComboBox.getSelectedItem().toString();
       String password = new String(PasswordField.getPassword()); // Capture the password

       // Validate required fields
       if (firstName.trim().isEmpty() || 
           lastName.trim().isEmpty() || 
           gender.equals("Choose") || 
           position.equals("Choose") || 
           password.isEmpty()) {  // Check if password is empty
           JOptionPane.showMessageDialog(this, "Please fill in all fields correctly!");
           return;
       }



       // Validate password length
       if (password.length() < 6) {
           JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long.");
           return;
       }


       // Generate or use existing Employee ID
       String empID;
       if (NewRadBtn.isSelected() || EmployeeId.getText().trim().isEmpty()) {
           empID = generateEmployeeID();  // Generate Employee ID if "New" is selected
       } else {
           empID = EmployeeId.getText().trim();
       }

       // Rest of your add logic remains the same
       try {
           File file = new File("src/user/users.txt");
           List<String[]> users = readUsersFromFile(file);

           // Check for duplicate Employee ID
           if (users.stream().anyMatch(user -> user[0].equals(empID))) {
               JOptionPane.showMessageDialog(this, "Duplicate Employee ID detected.");
               return;
           }

           // Add the user (now with password included)
           users.add(new String[]{empID, firstName, lastName, gender, position, password});
           writeUsersToFile(file, users);

           // Refresh the table
           refreshTableData();
           clearFields();

           JOptionPane.showMessageDialog(this, "Employee added successfully!");
       } catch (IOException e) {
           JOptionPane.showMessageDialog(this, "Error accessing users file: " + e.getMessage());
           e.printStackTrace();
       }
   }



    
    private void refreshTableData() {
        try {
            File file = new File("src/user/users.txt");
            List<String[]> users = readUsersFromFile(file);

            // Get the table model directly from the table
            DefaultTableModel model = (DefaultTableModel) employee_Table1.getTable().getModel();

            // Clear existing rows
            model.setRowCount(0);

            // Add rows from the file
            for (String[] user : users) {
                model.addRow(user);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error refreshing table: " + e.getMessage());
        }
    }


    private void writeUsersToFile(File file, List<String[]> users) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (String[] user : users) {
            writer.write(String.join(",", user) + "\n");  // Writing each user as a comma-separated line
        }
        writer.close();
    }



    
    // Delete an employee from users.txt
    void handleDeleteBtn() {
        String empID = EmployeeId.getText();

        if (empID.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Employee ID to delete.");
            return;
        }

        try {
            File file = new File("src/user/users.txt");
            List<String[]> users = readUsersFromFile(file);

            // Remove the employee
            boolean removed = users.removeIf(user -> user[0].equals(empID));

            if (removed) {
                writeUsersToFile(file, users);
                refreshTableData();
                clearFields();
                JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
                refreshTableData(); 
            } else {
                JOptionPane.showMessageDialog(this, "Employee ID not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error deleting employee: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
        // Populate fields from the selected table row
    private void populateFieldsFromTable() {
        int selectedRow = employee_Table1.getTable().getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) employee_Table1.getTable().getModel();
            EmployeeId.setText(model.getValueAt(selectedRow, 0).toString());
            FirstName.setText(model.getValueAt(selectedRow, 1).toString());
            LastName.setText(model.getValueAt(selectedRow, 2).toString());
            GenderComboBox.setSelectedItem(model.getValueAt(selectedRow, 3).toString());
            PositionComboBox.setSelectedItem(model.getValueAt(selectedRow, 4).toString());

            // Set the password field (You should only display it when necessary)
            PasswordField.setText(model.getValueAt(selectedRow, 5).toString());
        }
    }


    // Clear all input fields
    private void clearFields() {
        EmployeeId.setText("");
        FirstName.setText("");
        LastName.setText("");
        GenderComboBox.setSelectedIndex(0);
        PositionComboBox.setSelectedIndex(0);
        NewRadBtn.setSelected(false);
        EmployeeId.setEnabled(true);
    }

    private String generateEmployeeID() {
        String position = PositionComboBox.getSelectedItem().toString();
        if (position.equals("Choose")) {
            return "";
        }
        String abbreviation = switch (position) {
            case "Manager" -> "MGR";
            case "Receptionist" -> "REC";
            case "Security" -> "SEC";
            case "Stockers" -> "STK";
            default -> "EMP";
        };

        // Use an array as a mutable container for uniqueID
        final String[] uniqueID = {abbreviation + (100000 + new Random().nextInt(900000))};
        File file = new File("src/user/users.txt");

        try {
            List<String[]> users = readUsersFromFile(file);

            // Ensure ID is unique
            while (users.stream().anyMatch(user -> user != null && user.length > 0 && user[0].equals(uniqueID[0]))) {
                uniqueID[0] = abbreviation + (100000 + new Random().nextInt(900000));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uniqueID[0];
    }


    // Read users from file
    private List<String[]> readUsersFromFile(File file) throws IOException {
        List<String[]> users = new ArrayList<>();
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                users.add(line.split(","));
            }
            reader.close();
        }
        return users;
    }

    // Validate input fields
    private boolean validateFields(String empID, String firstName, String lastName, String gender, String position) {
        // Skip Employee ID check if the field is disabled (meaning it's auto-generated)
        if (!EmployeeId.isEnabled() && (firstName.isEmpty() || lastName.isEmpty() || !validateComboBoxes(gender, position))) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return false;
        }

        // Validate Employee ID (if the field is enabled) and other fields
        if ((empID.isEmpty() && EmployeeId.isEnabled()) || firstName.isEmpty() || lastName.isEmpty() || !validateComboBoxes(gender, position)) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return false;
        }
        return true;
    }
    
    private boolean validateComboBoxes(String gender, String position) {
    if (gender.equals("Choose") || position.equals("Choose")) {
        JOptionPane.showMessageDialog(this, "Please select both Gender and Position!");
        return false;
    }
    return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        EmployeeIdLabel = new javax.swing.JLabel();
        FirstNamelabel = new javax.swing.JLabel();
        EmployeeId = new javax.swing.JTextField();
        FirstName = new javax.swing.JTextField();
        LastNameLabel = new javax.swing.JLabel();
        LastName = new javax.swing.JTextField();
        GenderLabel = new javax.swing.JLabel();
        GenderComboBox = new javax.swing.JComboBox<>();
        PositionLabel = new javax.swing.JLabel();
        PositionComboBox = new javax.swing.JComboBox<>();
        NewRadBtn = new javax.swing.JRadioButton();
        AddBtn = new javax.swing.JButton();
        ClearBtn1 = new javax.swing.JButton();
        DeleteBtn = new javax.swing.JButton();
        UpdateBtn = new javax.swing.JButton();
        RefreshBtn = new javax.swing.JButton();
        PasswordField = new javax.swing.JPasswordField();
        Passwordabel = new javax.swing.JLabel();
        employee_Table1 = new employee.employee_Table();

        setBackground(new java.awt.Color(237, 249, 255));
        setPreferredSize(new java.awt.Dimension(900, 600));

        jPanel2.setOpaque(false);

        EmployeeIdLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        EmployeeIdLabel.setForeground(new java.awt.Color(0, 0, 0));
        EmployeeIdLabel.setText("Employee ID:");

        FirstNamelabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        FirstNamelabel.setForeground(new java.awt.Color(0, 0, 0));
        FirstNamelabel.setText("First Name:");

        EmployeeId.setBackground(new java.awt.Color(255, 255, 255));
        EmployeeId.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        EmployeeId.setCaretColor(new java.awt.Color(255, 255, 255));
        EmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmployeeIdActionPerformed(evt);
            }
        });

        FirstName.setBackground(new java.awt.Color(255, 255, 255));
        FirstName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        FirstName.setCaretColor(new java.awt.Color(255, 255, 255));
        FirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FirstNameActionPerformed(evt);
            }
        });

        LastNameLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        LastNameLabel.setForeground(new java.awt.Color(0, 0, 0));
        LastNameLabel.setText("Last Name:");

        LastName.setBackground(new java.awt.Color(255, 255, 255));
        LastName.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        LastName.setCaretColor(new java.awt.Color(255, 255, 255));
        LastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LastNameActionPerformed(evt);
            }
        });

        GenderLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        GenderLabel.setForeground(new java.awt.Color(0, 0, 0));
        GenderLabel.setText("Gender:");

        GenderComboBox.setBackground(new java.awt.Color(255, 255, 255));
        GenderComboBox.setForeground(new java.awt.Color(0, 0, 0));
        GenderComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose", "Male", "Female" }));
        GenderComboBox.setToolTipText("");
        GenderComboBox.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        PositionLabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        PositionLabel.setForeground(new java.awt.Color(0, 0, 0));
        PositionLabel.setText("Position:");

        PositionComboBox.setBackground(new java.awt.Color(255, 255, 255));
        PositionComboBox.setForeground(new java.awt.Color(0, 0, 0));
        PositionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose", "Manager", "Receptionist", "Security", "Stockers", " " }));
        PositionComboBox.setToolTipText("");
        PositionComboBox.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        NewRadBtn.setForeground(new java.awt.Color(0, 0, 0));
        NewRadBtn.setText("New");
        NewRadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewRadBtnActionPerformed(evt);
            }
        });

        AddBtn.setBackground(new java.awt.Color(10, 104, 235));
        AddBtn.setFont(new java.awt.Font("Google Sans Display", 1, 18)); // NOI18N
        AddBtn.setForeground(new java.awt.Color(255, 255, 255));
        AddBtn.setText("Add");
        AddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddBtnActionPerformed(evt);
            }
        });

        ClearBtn1.setBackground(new java.awt.Color(186, 15, 82));
        ClearBtn1.setFont(new java.awt.Font("Google Sans Display", 1, 18)); // NOI18N
        ClearBtn1.setForeground(new java.awt.Color(255, 255, 255));
        ClearBtn1.setText("Clear");
        ClearBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearBtn1ActionPerformed(evt);
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

        RefreshBtn.setBackground(new java.awt.Color(0, 0, 204));
        RefreshBtn.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        RefreshBtn.setForeground(new java.awt.Color(255, 255, 255));
        RefreshBtn.setText("Refresh");
        RefreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshBtnActionPerformed(evt);
            }
        });

        PasswordField.setBackground(new java.awt.Color(255, 255, 255));
        PasswordField.setForeground(new java.awt.Color(0, 0, 0));
        PasswordField.setText("jPasswordField1");
        PasswordField.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordFieldActionPerformed(evt);
            }
        });

        Passwordabel.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        Passwordabel.setForeground(new java.awt.Color(0, 0, 0));
        Passwordabel.setText("Password:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(FirstNamelabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FirstName))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(EmployeeIdLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EmployeeId))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(LastNameLabel)
                            .addComponent(Passwordabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LastName, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(PasswordField))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(NewRadBtn)
                        .addGap(77, 77, 77)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(PositionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PositionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(GenderLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(GenderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(RefreshBtn)
                                .addGap(56, 56, 56))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(UpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(DeleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(ClearBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(AddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(173, 173, 173))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(EmployeeIdLabel)
                            .addComponent(EmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(GenderLabel)
                            .addComponent(GenderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NewRadBtn))
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(RefreshBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FirstNamelabel)
                    .addComponent(FirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PositionLabel)
                    .addComponent(PositionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LastNameLabel)
                    .addComponent(LastName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ClearBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DeleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Passwordabel)
                            .addComponent(PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(employee_Table1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(employee_Table1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void FirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FirstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FirstNameActionPerformed

    private void LastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LastNameActionPerformed

    private void EmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmployeeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmployeeIdActionPerformed

    private void NewRadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewRadBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewRadBtnActionPerformed

    private void AddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddBtnActionPerformed

    private void ClearBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearBtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ClearBtn1ActionPerformed

    private void DeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DeleteBtnActionPerformed

    private void UpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UpdateBtnActionPerformed

    private void RefreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RefreshBtnActionPerformed

    private void PasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasswordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PasswordFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddBtn;
    private javax.swing.JButton ClearBtn1;
    private javax.swing.JButton DeleteBtn;
    private javax.swing.JTextField EmployeeId;
    private javax.swing.JLabel EmployeeIdLabel;
    private javax.swing.JTextField FirstName;
    private javax.swing.JLabel FirstNamelabel;
    private javax.swing.JComboBox<String> GenderComboBox;
    private javax.swing.JLabel GenderLabel;
    private javax.swing.JTextField LastName;
    private javax.swing.JLabel LastNameLabel;
    private javax.swing.JRadioButton NewRadBtn;
    private javax.swing.JPasswordField PasswordField;
    private javax.swing.JLabel Passwordabel;
    private javax.swing.JComboBox<String> PositionComboBox;
    private javax.swing.JLabel PositionLabel;
    private javax.swing.JButton RefreshBtn;
    private javax.swing.JButton UpdateBtn;
    private employee.employee_Table employee_Table1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
