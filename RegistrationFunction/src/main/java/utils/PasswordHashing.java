package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Class for hashing passwords using the SHA-512 algorithm.
 */
public class PasswordHashing {
    /**
     * Hashes the provided password using the SHA-512 algorithm.
     *
     * @param password The password to be hashed.
     * @return The hashed password as a hexadecimal string.
     */
    public String hashedPassword(String password) {
        StringBuilder sb = new StringBuilder();
        try {
            // Create a MessageDigest instance with the SHA-512 algorithm.
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

            // Update the MessageDigest with the bytes of the password.
            messageDigest.update(password.getBytes());

            // Compute the hash and get the resulting byte array.
            byte[] resultByteArray = messageDigest.digest();

            // Convert the byte array to a hexadecimal string representation.
            for (byte b : resultByteArray) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            // If the SHA-512 algorithm is not found, print an error message.
            System.out.println("No such algorithm was found: " + e.getMessage());
        }

        // If an error occurred or the algorithm is not found, return the original password as a fallback.
        return password;
    }
}
