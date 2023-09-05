package utils;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class ApiResponse {

    public static APIGatewayProxyResponseEvent createErrorResponse(APIGatewayProxyResponseEvent response,
                                                                   int statusCode,
                                                                   String responseBody){
        response.setStatusCode(statusCode);
        response.setBody(responseBody);
        return response;
    }
}
