package org.ryjan.telegram.controllers;

import org.ryjan.telegram.controllers.commands.SetCoinsRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/economy")
public class EconomyController {
    @Autowired
    SetCoinsRest setCoinsRest;

    @PostMapping("/set")
    public ResponseEntity<String> setCoins(@RequestParam String userTag, @RequestParam BigDecimal amount) {
        String result = setCoinsRest.execute(userTag, amount);
        return ResponseEntity.ok(result);
    }
}

