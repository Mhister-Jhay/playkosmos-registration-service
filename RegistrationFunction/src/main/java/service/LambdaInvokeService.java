package service;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import static utils.LambdaUtils.lambdaClient;

public class LambdaInvokeService {
    public String getResponse(String functionName, String messageBody, LambdaLogger logger){
        InvokeRequest request = InvokeRequest.builder()
                .functionName(functionName)
                .payload(SdkBytes.fromUtf8String(messageBody))
                .build();
        logger.log("Payload in Strings = "+ request.payload().asUtf8String());
        try {
            // Invoke the Lambda function and obtain the response
            InvokeResponse response = lambdaClient.invoke(request);

            // Extract and return the response payload (if successful)
            if (response.statusCode() == 200) {
                logger.log("Response = "+response.payload().asUtf8String());
                return response.payload().asUtf8String();
            } else {
                // Handle the error or non-successful response
                return "Error: " + response.statusCode() + " - " + response.functionError();
            }

        } catch (Exception e) {
            // Handle any exceptions that may occur during the Lambda function invocation
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
