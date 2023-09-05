package service;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import connection.DbConnection;
import model.*;
import repository.PlaymateRepository;
import utils.ApiResponse;
import utils.ConfigReader;
import utils.EntryValidator;
import utils.PasswordHashing;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static utils.GsonUtils.gson;


public class RegistrationService {
    /**
     * Registers a new Playmate and returns the response event.
     *
     * @param request       The PlaymateDTO object containing Playmate details to be registered.
     * @param responseEvent The APIGatewayProxyResponseEvent object to build and return the response.
     * @return The APIGatewayProxyResponseEvent object with the registration status.
     */
    public APIGatewayProxyResponseEvent registerNewPlaymate(Playmate request, APIGatewayProxyResponseEvent responseEvent, LambdaLogger logger) {
        // Initialize required objects and services.
        EntryValidator entryValidator = new EntryValidator();
        PlaymateRepository playmateRepository;
        PasswordHashing passwordHashing = new PasswordHashing();
        playmateRepository = new PlaymateRepository(DbConnection.connection);
        // Validate all Playmate fields using CompletableFuture and wait for completion.
        CompletableFuture<Map<String, String>> errorsFuture = entryValidator.validateAllFields(request);
        Map<String, String> errors = errorsFuture.join();
        if (!errors.isEmpty()) {
            // If there are validation errors, set the response status and body with the errors.
            return ApiResponse.createErrorResponse(responseEvent,400, errors.toString());
        }

        // Hash the Playmate's password before saving it to the database.
        String hashedPassword = passwordHashing.hashedPassword(request.getPassword());
        request.setPassword(hashedPassword);

        // Register the Playmate in the repository.
        String result = playmateRepository.registerPlaymate(request);

        LambdaInvokeService lambdaInvokeService = new LambdaInvokeService();

        if (result.equals("Playmate Registered Successfully")) {
            MessageModel messageModel;

            // Fetch the Playmate's details from the repository
            PlaymateDto playmateDto = playmateRepository.fetchPlaymate(request.getEmail(), request.getPhoneNumber());
            messageModel = new MessageModel(playmateDto.getEmail(), playmateDto.getPhoneNumber());
            // Convert message model to JSON
            String messageBody = gson.toJson(messageModel);

            // Invoke the Lambda function for sending messages
            String messageResponse = lambdaInvokeService.getResponse(ConfigReader.config.get("otp-function"), messageBody, logger);

            // Log the response obtained from invoking the otp generation function
            logger.log("Response obtained from invoking the otp generation function");
            logger.log("Response obtained: " + messageResponse);

            // Set response status and body
            return ApiResponse.createErrorResponse(responseEvent, 201,messageResponse);
        } else {
            // Set response status and body for registration failure
            return ApiResponse.createErrorResponse(responseEvent, 400, result);
        }
    }
}
