package repository;

import model.Playmate;
import model.PlaymateDto;
import utils.Queries;

import java.sql.*;

public class PlaymateRepository {

    private final Connection connection;

    public PlaymateRepository(Connection connection){
        this.connection = connection;
    }

    /**
     * Registers a new Playmate in the database.
     *
     * @param request The Playmate object containing Playmate details to be registered.
     * @return A message indicating the registration status.
     */
    public String registerPlaymate(Playmate request) {
        int isPlaymateAdded = 0;
        try{
            PreparedStatement preparedStatement = null;
            ResultSet resultSet;
            if (request.getEmail() != null && request.getPhoneNumber() == null) {
                // Check if Playmate with the provided email already exists.
                preparedStatement = connection.prepareStatement(Queries.Get_User_Email);
                preparedStatement.setString(1, request.getEmail());
            } else if (request.getEmail() == null && request.getPhoneNumber() != null) {
                // Check if Playmate with the provided phone number already exists.
                preparedStatement = connection.prepareStatement(Queries.Get_User_PhoneNumber);
                preparedStatement.setString(1, request.getPhoneNumber());
            }
            assert preparedStatement != null;
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return "Playmate is already registered, Proceed to Login";
            } else {
                // Register the new Playmate in the database.
                PreparedStatement statement = connection.prepareStatement(Queries.Register_User);
                statement.setString(1, request.getName());
                statement.setString(2, request.getEmail());
                statement.setString(3, request.getPhoneNumber());
                statement.setDate(4, Date.valueOf(request.getDateOfBirth()));
                statement.setString(5, request.getPassword());
                statement.setBoolean(6, false);
                isPlaymateAdded = statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        closeConnection();
        if (isPlaymateAdded > 0) {
            return "Playmate Registered Successfully";
        }
        return "Playmate Registration Failed, Please Try again";
    }
    /**
     * Fetches a Playmate's details using email or phone number.
     *
     * @param email The email of the Playmate to be fetched.
     * @param phoneNumber The phone number of the Playmate to be fetched.
     * @return The PlaymateDto object with the retrieved Playmate details if successful, else null.
     */
    public PlaymateDto fetchPlaymate(String email, String phoneNumber) {
        try{
            PreparedStatement preparedStatement = null;
            ResultSet resultSet;
            if (email != null && phoneNumber == null) {
                // Check if Playmate with the provided email exists.
                preparedStatement = connection.prepareStatement(Queries.Get_User_Email);
                preparedStatement.setString(1, email);
            } else if (email == null && phoneNumber != null) {
                // Check if Playmate with the provided phone number exists.
                preparedStatement = connection.prepareStatement(Queries.Get_User_PhoneNumber);
                preparedStatement.setString(1, phoneNumber);
            }
            assert preparedStatement != null;
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new PlaymateDto(
                        resultSet.getInt("playmate_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number")
                );
            }
        } catch(SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        closeConnection();
        return null;
    }

    private void closeConnection(){
        try{
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
