package org.ryjan.telegram.misc;


import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Misc {

    public static String getConnectionString() {
        ClassLoader classLoader = Misc.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream("databaseJsons/config.json")) {
            if (inputStream == null) {
                System.out.println("No config.json found");
            }
            JsonReader jsonReader = Json.createReader(new InputStreamReader(inputStream));
            JsonObject jsonObject = jsonReader.readObject();
            return jsonObject.getJsonObject("Database").getString("connectionString");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}