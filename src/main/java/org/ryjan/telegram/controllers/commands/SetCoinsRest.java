package org.ryjan.telegram.controllers.commands;

import org.ryjan.telegram.model.users.BankDatabase;
import org.ryjan.telegram.model.users.UserDatabase;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
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
