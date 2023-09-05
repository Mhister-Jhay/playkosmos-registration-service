package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/**
 * PasswordChecker is a utility class for checking whether a password is in the list of banned passwords.
 */
public class PasswordChecker {
    /**
     * List of banned passwords.
     */
    public static final List<String> bannedPasswords;

    // Static block for initializing the list of banned passwords.
    static {
        // File name of the CSV containing the banned passwords.
        String file = "100-most-common-passwords.csv";

        // Initialize the list of banned passwords as an ArrayList.
        bannedPasswords = new ArrayList<>();

        // Read the CSV file and populate the list with banned passwords.
        try (InputStream inputStream = PasswordChecker.class.getClassLoader().getResourceAsStream(file)) {
            // Check if the input stream is not null.
            assert inputStream != null;

            // Use a BufferedReader to read the lines from the CSV file.
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Add each line (password) to the list of banned passwords.
                    bannedPasswords.add(line);
                }
            }
        } catch (IOException e) {
            // Print the stack trace in case of any IO errors during initialization.
            e.printStackTrace();
        }
    }
}
