package org.ryjan.telegram.authorization;

import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/telegram")
public class TelegramAuthController {

    private final TelegramAuthValidator validator;
    private final JwtService jwtService;
    private final UserService userService;

    public TelegramAuthController(JwtService jwtService, TelegramAuthValidator validator, UserService userService) {
        this.jwtService = jwtService;
        this.validator = validator;
        this.userService = userService;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> handleTelegramCallback(@RequestParam Map<String, String> params) {
        Map<String, String> decodedParams = params.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> URLDecoder.decode(entry.getValue(), StandardCharsets.UTF_8)
                ));

        System.out.println("Decoded Telegram data: " + decodedParams);

        if (!validator.validateTelegramData(decodedParams)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Telegram data");
        }

        Long userId = Long.valueOf(decodedParams.get("id"));

        User user = userService.findUser(userId);

        String token = jwtService.generateToken(userId, user.getUserGroup());

        return ResponseEntity.ok(Map.of("token", token));
    }
}
