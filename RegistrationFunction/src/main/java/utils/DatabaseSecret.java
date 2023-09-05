package utils;

import model.DbData;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

import static utils.GsonUtils.gson;

public class DatabaseSecret {
    // Declare a static instance of SecretsManagerClient to interact with AWS Secrets Manager.
    private static final SecretsManagerClient secretManagerClient;

    // Declare a constant field to store the retrieved database credentials.
    public static final DbData getDbData;

    static {
        // Initialize the SecretsManagerClient with the specified AWS region.
        secretManagerClient = SecretsManagerClient.builder()
                .region(Region.of(ConfigReader.config.get("region")))
                .build();

        // Create a request to retrieve the secret value using the configured secret ID.
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(ConfigReader.config.get("secret-id"))
                .build();
        GetSecretValueResponse getSecretValueResponse = null;

        try {
            // Retrieve the secret value using the SecretsManagerClient.
            getSecretValueResponse = secretManagerClient.getSecretValue(getSecretValueRequest);
        } catch (SecretsManagerException e) {
            e.printStackTrace(); // Print the exception details if an error occurs.
        }

        // Ensure that the secret value response is not null.
        assert getSecretValueResponse != null;

        // Extract the secret value (secrets) from the response.
        String secrets = getSecretValueResponse.secretString();

        // Deserialize the secrets into a DbData object using Gson.
        getDbData = gson.fromJson(secrets, DbData.class);
    }
}
