package util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UIDGenerator {

    private static int uidCounter = 0; // Static counter to track UIDs
    private static final String COUNTER_FILE = "src/util/uid_counter.txt"; // File to store the counter value

    static {
        // Load the counter from the file when the class is loaded
        loadCounterFromFile();
    }

    // Static method to generate a unique UID
    public static synchronized String generateUID(String role) {
        // Ensure the counter doesn't go beyond 9999 and wraps around
        if (uidCounter > 9999) {
            uidCounter = 0;
        }

        // Format the counter to always display 4 digits (e.g., 0001, 0010, ..., 9999)
        String formattedCounter = String.format("%04d", uidCounter);

        // Get the current timestamp in yyyyMMddHHmmss format (Year, Month, Day, Hour, Minute, Second)
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

        // Generate a unique UID combining role, timestamp, and formatted counter
        String uniqueID = role.substring(0, 3).toUpperCase() + timestamp + formattedCounter;

        // Increment the counter for the next UID generation
        uidCounter++;

        // Save the updated counter to the file
        saveCounterToFile();

        return uniqueID;
    }

    // Load the counter value from a file
    private static void loadCounterFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(COUNTER_FILE))) {
            String counterValue = reader.readLine();
            if (counterValue != null) {
                uidCounter = Integer.parseInt(counterValue);
            }
        } catch (IOException | NumberFormatException e) {
            // If file doesn't exist or there's an error, start with 0
            uidCounter = 0;
        }
    }

    // Save the current counter value to a file
    private static void saveCounterToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COUNTER_FILE))) {
            writer.write(String.valueOf(uidCounter));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
