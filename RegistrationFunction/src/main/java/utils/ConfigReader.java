package utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

    public static final Map<String, String> config;

    static{
        Dotenv dotenv = Dotenv.configure().filename("stage.env").load();
        config = new HashMap<>();
        config.put("region",dotenv.get("REGION"));
        config.put("secret-id",dotenv.get("DATABASE_SECRET_ID"));
        config.put("otp-function",dotenv.get("OTP_FUNCTION_NAME"));
    }
}
