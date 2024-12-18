package stocktrack;

import user.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Homepage extends JFrame {

    public Homepage() {
        // Set frame properties
        setTitle("Homepage");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a list of managers
        ArrayList<Manager> managers = new ArrayList<>();
        managers.add(new Manager("John", "Doe", "123 Main St", "555-1234", 50000, "Manager", "Jo", "123"));
        managers.add(new Manager("Jane", "Smith", "456 Elm St", "555-5678", 60000, "Manager", "Ja", "1234"));
        managers.add(new Manager("Alice", "Johnson", "789 Oak St", "555-8765", 55000, "Manager", "Al", "12345"));

        // Create a StringBuilder to display manager info
        StringBuilder managerList = new StringBuilder("<html>");
        for (Manager manager : managers) {
            managerList.append(manager.toString()).append("<br>");
        }
        managerList.append("</html>");

        // Example content
        JLabel welcomeLabel = new JLabel("Welcome to StockTrack!", SwingConstants.CENTER);
        welcomeLabel.setFont(new java.awt.Font("Google Sans Display", java.awt.Font.BOLD, 24));

        JLabel managerInfoLabel = new JLabel(managerList.toString(), SwingConstants.LEFT);
        managerInfoLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));

        // Set layout and add components
        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.NORTH);
        add(managerInfoLabel, BorderLayout.CENTER);
    }
}
