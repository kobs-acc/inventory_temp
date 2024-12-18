package employee;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Employees {

    public static void main(String[] args) {
        // Starting the application with MainMenu
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}

class MainMenu extends JFrame {
    MainMenu() {
        setTitle("Employees");
        this.setResizable(false);
        setSize(500, 300);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JButton btnManageReceptionist = new JButton("Manage Employees");
        btnManageReceptionist.setBounds(150, 50, 200, 40);
        btnManageReceptionist.addActionListener(e -> {
            dispose(); // Close the main menu
            new ManageEmployeesMenu(); // Open the Manage Receptionist menu
        });

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(150, 120, 200, 40);
        btnLogout.addActionListener(e -> System.exit(0));

        add(btnManageReceptionist);
        add(btnLogout);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

class ManageEmployeesMenu extends JFrame {
    ManageEmployeesMenu() {
        setTitle("Manage Emplyoees");
        this.setResizable(false);
        setSize(500, 430);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Add Employees");
        btnAdd.setBounds(150, 30, 200, 40);
        btnAdd.addActionListener(e -> {
            dispose();
            new AddEmployeesPanel();
        });

        JButton btnUpdate = new JButton("Update Employees");
        btnUpdate.setBounds(150, 90, 200, 40);
        btnUpdate.addActionListener(e -> {
            // Placeholder functionality for Update
            dispose();
            new UpdateEmployeesPanel();
        });

        JButton btnRemove = new JButton("Remove Employees");
        btnRemove.setBounds(150, 150, 200, 40);
        btnRemove.addActionListener(e -> {
            // Placeholder functionality for Remove
            dispose();
            new RemoveEmployeesPanel();
        });

        JButton btnView = new JButton("View Employees");
        btnView.setBounds(150, 210, 200, 40);
        btnView.addActionListener(e -> {
            // Placeholder for view functionality
            dispose();
            new ViewEmployeesPanel();
            
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(150, 270, 200, 40);
        btnBack.addActionListener(e -> {
            dispose();
            new MainMenu();
        });

        add(btnAdd);
        add(btnUpdate);
        add(btnRemove);
        add(btnView);
        add(btnBack);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}


class AddEmployeesPanel extends JFrame {
    AddEmployeesPanel() {
        setTitle("Add Employees");
        this.setResizable(false);
        setSize(700, 500);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Add Employees Panel");
        lblTitle.setForeground(Color.black);
        lblTitle.setBounds(300, 10, 200, 30);
        add(lblTitle);

        JLabel lblDetails = new JLabel("Employees Details");
        lblDetails.setForeground(Color.BLACK);
        lblDetails.setBounds(30, 50, 150, 30);
        add(lblDetails);

        JLabel lblEmpID = new JLabel("Employee ID:");
        lblEmpID.setForeground(Color.BLACK);
        lblEmpID.setBounds(30, 100, 100, 30);
        add(lblEmpID);

        JTextField txtEmpID = new JTextField(generateID());
        txtEmpID.setBounds(250, 100, 150, 30);
        txtEmpID.setEditable(false);
        add(txtEmpID);

        JLabel lblEmpName = new JLabel("Employee Name:");
        lblEmpName.setForeground(Color.BLACK);
        lblEmpName.setBounds(30, 165, 120, 30);
        add(lblEmpName);

        JTextField txtEmpName = new JTextField();
        txtEmpName.setBounds(250, 165, 150, 30);
        add(txtEmpName);

        JLabel lblUserID = new JLabel("User ID:");
        lblUserID.setForeground(Color.BLACK);
        lblUserID.setBounds(30, 225, 100, 30);
        add(lblUserID);

        JTextField txtUserID = new JTextField();
        txtUserID.setBounds(250, 225, 150, 30);
        add(txtUserID);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBounds(30, 285, 100, 30);
        add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(250, 285, 150, 30);
        add(txtPassword);

        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setForeground(Color.BLACK);
        lblConfirmPassword.setBounds(30, 350, 150, 30);
        add(lblConfirmPassword);

        JPasswordField txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(250, 350, 150, 30);
        add(txtConfirmPassword);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBounds(540, 380, 100, 30);
        btnAdd.addActionListener(e -> {
            String empID = txtEmpID.getText();
            String name = txtEmpName.getText();
            String userID = txtUserID.getText();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
        
            if (name.isEmpty() || userID.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }
        
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }
        
            // Write the receptionist data to the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("employeesDatabase.txt", true))) {
                String job = "Employee"; // Default job title
                String salary = "1500";    // Example salary, can be dynamic
                bw.write(empID + "," + name + "," + userID + "," + job + "," + salary);
                bw.newLine();
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving employee data: " + ex.getMessage());
            }
        
            // Clear the input fields after adding
            txtEmpName.setText("");
            txtUserID.setText("");
            txtPassword.setText("");
            txtConfirmPassword.setText("");
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(540, 50, 100, 30);
        btnBack.addActionListener(e -> {
            dispose();
            new ManageEmployeesMenu();
        });

        add(btnAdd);
        add(btnBack);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    

    private String generateID() {
        return "EMP" + (new Random().nextInt(9000) + 1000); // To ensure the ID is always four digits long
    }
}

class UpdateEmployeesPanel extends JFrame {
    UpdateEmployeesPanel() {
        setTitle("Update Receptionist");
        this.setResizable(false);
        setSize(700, 500);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Update Employees Panel");
        lblTitle.setBounds(300, 10, 200, 30);
        add(lblTitle);

        JLabel lblEmpID = new JLabel("Employee ID:");
        lblEmpID.setBounds(30, 100, 150, 30);
        add(lblEmpID);

        JComboBox<String> cmbEmpID = new JComboBox<>();
        cmbEmpID.setBounds(250, 100, 150, 30);
        loadEmployeeIDs(cmbEmpID);
        add(cmbEmpID);

        JLabel lblEmpName = new JLabel("Employee Name:");
        lblEmpName.setBounds(30, 165, 150, 30);
        add(lblEmpName);

        JTextField txtEmpName = new JTextField();
        txtEmpName.setBounds(250, 165, 150, 30);
        txtEmpName.setEditable(false); // Display only
        add(txtEmpName);

        JLabel lblUserID = new JLabel("User ID:");
        lblUserID.setBounds(30, 225, 150, 30);
        add(lblUserID);

        JTextField txtUserID = new JTextField();
        txtUserID.setBounds(250, 225, 150, 30);
        txtUserID.setEditable(false); // Display only
        add(txtUserID);

        JLabel lblNewPassword = new JLabel("New Password:");
        lblNewPassword.setBounds(30, 285, 150, 30);
        add(lblNewPassword);

        JPasswordField txtNewPassword = new JPasswordField();
        txtNewPassword.setBounds(250, 285, 150, 30);
        add(txtNewPassword);

        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setBounds(30, 350, 150, 30);
        add(lblConfirmPassword);

        JPasswordField txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(250, 350, 150, 30);
        add(txtConfirmPassword);

        cmbEmpID.addActionListener(e -> loadReceptionistDetails((String) cmbEmpID.getSelectedItem(), txtEmpName, txtUserID));

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(540, 380, 100, 30);
        btnUpdate.addActionListener(e -> {
            String selectedEmpID = (String) cmbEmpID.getSelectedItem();
            String newPassword = new String(txtNewPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password fields cannot be empty.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            if (updatePassword(selectedEmpID, newPassword)) {
                JOptionPane.showMessageDialog(this, "Password updated successfully!");
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error updating password.");
            }
        });

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(540, 50, 100, 30);
        btnBack.addActionListener(e -> {
            dispose();
            new ManageEmployeesMenu();
        });

        add(btnUpdate);
        add(btnBack);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadEmployeeIDs(JComboBox<String> comboBox) {
        File file = new File("employeesDatabase.txt");

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No employees data found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 1) {
                    comboBox.addItem(data[0]); // Assuming Employee ID is the first field
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading Employee IDs: " + e.getMessage());
        }
    }

    private void loadReceptionistDetails(String empID, JTextField nameField, JTextField userIDField) {
        File file = new File("employeesDatabase.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].equals(empID)) {
                    nameField.setText(data[1]); // Employee Name
                    userIDField.setText(data[2]); // User ID
                    return;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading details: " + e.getMessage());
        }
    }

    private boolean updatePassword(String empID, String newPassword) {
        File file = new File("employeesDatabase.txt");
        File tempFile = new File("employeesDatabase_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean updated = false;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(empID)) {
                    // Update the password (assuming password is at index 4, adjust if needed)
                    data[4] = newPassword;
                    updated = true;
                }
                bw.write(String.join(",", data));
                bw.newLine();
            }

            if (!updated) {
                return false;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating password: " + e.getMessage());
            return false;
        }

        // Replace original file with updated file
        if (!file.delete() || !tempFile.renameTo(file)) {
            JOptionPane.showMessageDialog(this, "Error replacing file.");
            return false;
        }

        return true;
    }
}

class RemoveEmployeesPanel extends JFrame {
    private JComboBox<String> comboEmpID;
    private JTextField txtEmpName;

    RemoveEmployeesPanel() {
        setTitle("Remove Employees");
        this.setResizable(false);
        setSize(500, 300);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblEmpID = new JLabel("Employee ID:");
        lblEmpID.setBounds(30, 50, 150, 30);
        add(lblEmpID);

        // ComboBox to select Employee ID
        comboEmpID = new JComboBox<>();
        comboEmpID.setBounds(200, 50, 150, 30);
        loadEmployeeIDs(); // Load Employee IDs into the ComboBox
        comboEmpID.addActionListener(e -> fillEmployeeName());
        add(comboEmpID);

        JLabel lblEmpName = new JLabel("Employee Name:");
        lblEmpName.setBounds(30, 100, 150, 30);
        add(lblEmpName);

        txtEmpName = new JTextField();
        txtEmpName.setBounds(200, 100, 150, 30);
        txtEmpName.setEditable(false); // Don't allow editing the name
        add(txtEmpName);

        JButton btnRemove = new JButton("Remove");
        btnRemove.setBounds(50, 200, 100, 30);
        btnRemove.addActionListener(e -> removeEmployees());
        add(btnRemove);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(200, 200, 100, 30);
        btnBack.addActionListener(e -> {
            dispose();
            new ManageEmployeesMenu();
        });
        add(btnBack);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Load Employee IDs into the ComboBox from the file
    private void loadEmployeeIDs() {
        File file = new File("employeesDatabase.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    comboEmpID.addItem(data[0]); // Add Employee ID to the ComboBox
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage());
        }
    }

    // Fill Employee Name based on selected Employee ID
    private void fillEmployeeName() {
        String selectedID = (String) comboEmpID.getSelectedItem();
        if (selectedID == null) return;

        File file = new File("employeesDatabase.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5 && data[0].equals(selectedID)) {
                    txtEmpName.setText(data[1]); // Set the Employee Name
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage());
        }
    }

    // Logic to remove the selected receptionist
    // so pag remove sa employees
    private void removeEmployees() {
        String selectedID = (String) comboEmpID.getSelectedItem();
        String name = txtEmpName.getText();
        
        if (selectedID == null || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a valid receptionist.");
            return;
        }

        File file = new File("employeesDatabase.txt");
        File tempFile = new File("employeesDatabase_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean removed = false;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5 && data[0].equals(selectedID)) {
                    removed = true; // Mark as removed
                    continue; // Skip writing this line (removes the receptionist)
                }
                bw.write(line);
                bw.newLine();
            }

            if (!removed) {
                JOptionPane.showMessageDialog(this, "Employee not found.");
                return;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error removing employee: " + e.getMessage());
            return;
        }

        // Replace the old file with the new one
        if (!file.delete() || !tempFile.renameTo(file)) {
            JOptionPane.showMessageDialog(this, "Error replacing file.");
        } else {
            JOptionPane.showMessageDialog(this, "Employee removed successfully!");
            dispose();
            new ManageEmployeesMenu(); // Go back to the Manage Receptionist menu
        }
    }
}

class ViewEmployeesPanel extends JFrame {
    ViewEmployeesPanel() {
        setTitle("View Employees");
        setSize(700, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        String[] columns = {"Employee ID", "Employee Name", "User ID", "Job", "Salary"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        // Load receptionist data from the file
        loadReceptionistData(model);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> {
            dispose();
            new ManageEmployeesMenu();
        });
        add(btnBack, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    //Database kang Emplyoees
    private void loadReceptionistData(DefaultTableModel model) {
        File file = new File("employeesDatabase.txt");
    
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No receptionist data found.");
            return;
        }
    
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) { // Ensure the data format is correct
                    model.addRow(data);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}
