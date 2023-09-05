package controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.JsonParseException;
import model.Playmate;
import service.RegistrationService;
import utils.ApiResponse;

import java.util.HashMap;
import java.util.Map;
import static utils.GsonUtils.gson;

public class RegisterController implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    /**
     * The main method for handling the incoming APIGatewayProxyRequestEvent related to registration.
     * It parses the request body, processes the registration using RegisterPlaymate class, and returns the response.
     *
     * @param input   The APIGatewayProxyRequestEvent containing the request data.
     * @param context The AWS Lambda Context object.
     * @return An APIGatewayProxyResponseEvent with the registration response.
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        // Set the headers for the response.
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        LambdaLogger logger = context.getLogger();
        Playmate playmate = null;
        try {
            // Parse the request body and convert it to a PlaymateDTO object using Gson.
            String requestBody = input.getBody();
            playmate = gson.fromJson(requestBody, Playmate.class);

        } catch (JsonParseException e) {
            // Handle JSON parsing errors.
            return ApiResponse.createErrorResponse(response,400,"Invalid request format");
        } catch (NullPointerException e) {
            // Handle null request body.
            return ApiResponse.createErrorResponse(response, 400, "Missing request body");
        } catch (Exception e) {
            // Handle other exceptions.
            return ApiResponse.createErrorResponse(response, 500, "Error occurred in processing request");
        }
        // Initialize RegisterPlaymate class and call the registerNewPlaymate method to process the registration request.
        RegistrationService registrationService = new RegistrationService();
        return registrationService.registerNewPlaymate(playmate, response,logger);
    }

}
