package utils;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;

public class LambdaUtils {
    public static final LambdaClient lambdaClient;

    static{
        lambdaClient = LambdaClient.builder()
                .region(Region.of(ConfigReader.config.get("region")))
                .build();
    }
}
