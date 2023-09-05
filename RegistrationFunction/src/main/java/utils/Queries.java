package utils;

public class Queries {
    // SQL query to register a new playmate in the database.
    public static final String Register_User = "INSERT INTO playmates(name, email, phone_number, date_of_birth, password, verified) VALUES(?,?,?,?,?,?)";

    // SQL query to retrieve a user's email from the database based on the email.
    public static final String Get_User_Email = "SELECT * FROM playmates WHERE email = ?";

    // SQL query to retrieve a user's email from the database based on the phone number.
    public static final String Get_User_PhoneNumber = "SELECT * FROM playmates WHERE phone_number = ?";

    public static final String Get_Playmate = "SELECT * FROM playmates WHERE playmate_id = ?";
    public static final String update_Playmate = "UPDATE playmates SET verified = ? WHERE playmate_id = ?";
}
