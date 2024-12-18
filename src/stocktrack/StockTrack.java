package stocktrack;

import reception.ReceptionistDashboard;
import homepage.Homepage;
import login.Login;

/**
 *
 * @author Joy De Castro
 */

public class StockTrack {
    public static void main(String[] args) {
        // Create and show the Login frame
        Login loginFrame = new Login();
        loginFrame.setVisible(true);

        // Wait until Login frame is closed
        while (loginFrame.isDisplayable()) {
            try {
                Thread.sleep(100); // Small delay to avoid busy-waiting
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Check login status and role after the login frame closes
        if (loginFrame.isLoginSuccessful()) {
            String role = loginFrame.getRole();

            // Based on the role, navigate to the correct screen
            java.awt.EventQueue.invokeLater(() -> {
                if (null == role) {
                    System.out.println("Invalid role. Exiting program.");
                } else switch (role) {
                    case "manager" -> new Homepage().setVisible(true); // Open the PanelBorder for Admin        // Check login status and role after the login frame closes

                    case "receptionist" -> new ReceptionistDashboard(); // Open Reception for Receptionist
                    default -> System.out.println("Invalid role. Exiting program.");
                }
            });
        } else {
            System.out.println("Login failed. Exiting program.");
        }
    }
}