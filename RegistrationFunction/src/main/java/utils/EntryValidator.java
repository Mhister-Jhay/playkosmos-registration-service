package utils;

import model.Playmate;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class EntryValidator {
    // Regular expression pattern for email validation.
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    /**
     * Asynchronously validates the name field.
     * @param name The name to validate.
     * @return A CompletableFuture containing the map of errors, if any.
     */
    public CompletableFuture<Map<String, String>> validateName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> errors = new HashMap<>();
            // Check if the name is null or doesn't contain at least a first name and a last name.
            if (name == null || name.trim().split("\\s+").length < 2) {
                errors.put("name", "Name must be Firstname and Lastname");
            } else {
                // Split the name into individual names using whitespace as the delimiter.
                String[] names = name.trim().split("\\s+");
                for (String anyName : names) {
                    // Check if any individual name has less than 2 characters.
                    if (anyName.length() < 2) {
                        errors.put("name", "Each name must be more than 2 characters");
                    }
                }
            }
            return errors;
        });
    }

    /**
     * Asynchronously validates the email field.
     * @param email The email to validate.
     * @return A CompletableFuture containing the map of errors, if any.
     */
    public CompletableFuture<Map<String, String>> validateEmail(String email) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> errors = new HashMap<>();
            // Check if the email is null or doesn't match the email pattern.
            if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
                errors.put("email", "Invalid email address");
            }
            return errors;
        });
    }

    /**
     * Asynchronously validates the phone number field.
     * @param phoneNumber The phone number to validate.
     * @return A CompletableFuture containing the map of errors, if any.
     */
    public CompletableFuture<Map<String, String>> validatePhoneNumber(String phoneNumber) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> errors = new HashMap<>();
            // Check if the phone number is null.
            if (phoneNumber == null) {
                errors.put("phoneNumber", "Phone number cannot be null");
            }
            return errors;
        });
    }

    /**
     * Asynchronously validates the date of birth field.
     * @param dateOfBirth The date of birth to validate.
     * @return A CompletableFuture containing the map of errors, if any.
     */
    public CompletableFuture<Map<String, String>> validateDateOfBirth(String dateOfBirth) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> errors = new HashMap<>();
            // Check if the date of birth is null.
            if (dateOfBirth == null) {
                errors.put("dateOfBirth", "Date of Birth cannot be null");
            } else {
                // Convert the date of birth string to a LocalDate object.
                LocalDate dob = Date.valueOf(dateOfBirth).toLocalDate();
                // Get the current date.
                LocalDate today = LocalDate.now();
                // Calculate the age using the Period class.
                Period age = Period.between(dob, today);
                // Check if the age is less than 16 years.
                if (age.getYears() < 16) {
                    errors.put("dateOfBirth", "Must be 16 years or older");
                }
            }
            return errors;
        });
    }

    /**
     * Asynchronously validates the password field.
     * @param request The PlaymateDTO containing the name and password to validate.
     * @return A CompletableFuture containing the map of errors, if any.
     */
    public CompletableFuture<Map<String, String>> validatePassword(Playmate request) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> errors = new HashMap<>();
            // Get the list of banned passwords from the PasswordChecker class.
            List<String> bannedPasswords = PasswordChecker.bannedPasswords;
            // Extract individual names from the Playmate's full name.
            String[] names = request.getName().trim().split("\\s+");
            // Add individual names to the list of banned passwords.
            bannedPasswords.addAll(List.of(names));
            // Check if the password field is null.
            if (request.getPassword() == null) {
                errors.put("password", "Password cannot be null");
            } else if (bannedPasswords.contains(request.getPassword())) {
                // Check if the password matches any of the banned passwords.
                errors.put("password", "Password is common, please choose a different password");
            }
            return errors;
        });
    }


    /**
     * Asynchronously validates all the fields in the PlaymateDTO.
     * @param request The PlaymateDTO to validate.
     * @return A CompletableFuture containing the map of errors, if any.
     */
    public CompletableFuture<Map<String, String>> validateAllFields(Playmate request) {
        CompletableFuture<Map<String, String>> nameValidation = validateName(request.getName());
        CompletableFuture<Map<String, String>> emailValidation = validateEmail(request.getEmail());
        CompletableFuture<Map<String, String>> phoneNumberValidation = validatePhoneNumber(request.getPhoneNumber());
        CompletableFuture<Map<String, String>> dateOfBirthValidation = validateDateOfBirth(request.getDateOfBirth());
        CompletableFuture<Map<String, String>> passwordValidation = validatePassword(request);

        if (request.getEmail() == null && request.getPhoneNumber() != null) {
            // When email is null but phoneNumber is provided, validate name, phoneNumber, dateOfBirth, and password.
            return CompletableFuture.allOf(
                    nameValidation,
                    phoneNumberValidation,
                    dateOfBirthValidation,
                    passwordValidation
            ).thenApplyAsync(v -> {
                Map<String, String> allErrors = new HashMap<>();
                try {
                    allErrors.putAll(nameValidation.get());
                    allErrors.putAll(phoneNumberValidation.get());
                    allErrors.putAll(dateOfBirthValidation.get());
                    allErrors.putAll(passwordValidation.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Error occurred in validating inputs: " + e.getMessage());
                }
                return allErrors;
            });
        } else if (request.getEmail() != null && request.getPhoneNumber() == null) {
            // When phoneNumber is null but email is provided, validate name, email, dateOfBirth, and password.
            return CompletableFuture.allOf(
                    nameValidation,
                    emailValidation,
                    dateOfBirthValidation,
                    passwordValidation
            ).thenApplyAsync(v -> {
                Map<String, String> allErrors = new HashMap<>();
                try {
                    allErrors.putAll(nameValidation.get());
                    allErrors.putAll(emailValidation.get());
                    allErrors.putAll(dateOfBirthValidation.get());
                    allErrors.putAll(passwordValidation.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Error occurred in validating inputs: " + e.getMessage());
                }
                return allErrors;
            });
        }

        // When both email and phoneNumber are provided, validate all fields.
        return CompletableFuture.allOf(
                nameValidation,
                emailValidation,
                phoneNumberValidation,
                dateOfBirthValidation,
                passwordValidation
        ).thenApplyAsync(v -> {
            Map<String, String> allErrors = new HashMap<>();
            try {
                allErrors.putAll(nameValidation.get());
                allErrors.putAll(emailValidation.get());
                allErrors.putAll(phoneNumberValidation.get());
                allErrors.putAll(dateOfBirthValidation.get());
                allErrors.putAll(passwordValidation.get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error occurred in validating inputs: " + e.getMessage());
            }
            return allErrors;
        });
    }
}
