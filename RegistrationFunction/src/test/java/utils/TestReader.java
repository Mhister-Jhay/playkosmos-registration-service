package utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.Map;

public class TestReader {
    public static final Map<String, String> config;

    static{
        Dotenv dotenv = Dotenv.configure().filename("dev.env").load();
        config = new HashMap<>();
        config.put("engine", dotenv.get("DATABASE_ENGINE"));
        config.put("host",dotenv.get("DATABASE_HOST"));
        config.put("port", dotenv.get("DATABASE_PORT"));
        config.put("db", dotenv.get("DATABASE"));
        config.put("username",dotenv.get("DATABASE_USERNAME"));
        config.put("password", dotenv.get("DATABASE_PASSWORD"));
    }
}
