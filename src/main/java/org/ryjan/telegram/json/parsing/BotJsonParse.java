package org.ryjan.telegram.json.parsing;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BotJsonParse {

    private static String getStringFromJson(String path, String object, String str) {
        ClassLoader classLoader = DatabaseJsonParse.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
            if (inputStream == null) {
                System.out.println("No config.json found");
            }
            JsonReader jsonReader = Json.createReader(new InputStreamReader(inputStream));
            JsonObject jsonObject = jsonReader.readObject();
            return jsonObject.getJsonObject(object).getString(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getBotName() {
        return getStringFromJson("botSettings/config.json", "Bot", "name");
    }

    public static String getBotToken() {
        return getStringFromJson("botSettings/config.json", "Bot", "token");
    }

    public static String getOwner() {
        return getStringFromJson("botSettings/config.json", "Bot", "owner");
    }

    public static String getChatGPTToken() {
        return getStringFromJson("botSettings/config.json", "ChatGPT", "token");
    }

    public static String getChatGPTUrl() {
        return getStringFromJson("botSettings/config.json", "ChatGPT", "url");
    }
}
