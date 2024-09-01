package org.ryjan.telegram.controllers;

import org.ryjan.telegram.model.BankDatabase;
import org.ryjan.telegram.model.UserDatabase;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SetCoinsRest {
    @Autowired
    private UserService userService;

    public String execute(String username, BigDecimal amount) {
        UserDatabase user = userService.findUser(username);
        if (user == null) {
            return "User not found";
        }

        BankDatabase bank = user.getBank();
        bank.setCoins(amount);
        userService.update(user);
        return "Operation successful!";
    }
}
