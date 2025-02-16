package org.ryjan.telegram.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TelegramAuthValidator {

    private final String BOT_TOKEN;

    public TelegramAuthValidator(@Value("${bot.token}") String botToken) {
        if (botToken == null || botToken.isEmpty()) {
            throw new IllegalArgumentException("BOT_TOKEN is not configured(null)");
        }
        this.BOT_TOKEN = botToken;
    }

    public boolean validateTelegramData(Map<String, String> data) {
        // Извлекаем hash из данных
        System.out.println("BOT_TOKEN: " + BOT_TOKEN);
        String hash = data.remove("hash");
        System.out.println("Received hash: " + hash);

        // Формируем строку для проверки подписи
        String dataCheckString = data.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("\n"));
        System.out.println("Data check string: " + dataCheckString);

        byte[] secretKey = sha256();

        // Генерируем хэш HMAC-SHA256
        String generatedHash = hmacSha256ToHex(secretKey, dataCheckString);
        System.out.println("DatacheckString: " + dataCheckString.substring(dataCheckString.indexOf("=") + 1));
        System.out.println("Generated hash: " + generatedHash);
        System.out.println("Is valid: " + generatedHash.equals(hash));

        // Сравниваем хэши
        return hash.equals(generatedHash);
    }

    private byte[] sha256() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(BOT_TOKEN.getBytes(StandardCharsets.UTF_8));
            System.out.println("Generated secret key: " + Base64.getEncoder().encodeToString(hashBytes));
            return hashBytes;
        } catch (Exception e) {
            throw new RuntimeException("Error generating SHA-256", e);
        }
    }

    private String hmacSha256ToHex(byte[] key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
            mac.init(secretKey);
            byte[] hashByte = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashByte);
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC-SHA256", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
