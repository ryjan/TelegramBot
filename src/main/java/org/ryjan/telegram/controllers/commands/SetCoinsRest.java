package org.ryjan.telegram.controllers.commands;

import org.ryjan.telegram.model.users.Bank;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SetCoinsRest {
    @Autowired
    private UserService userService;

    public String execute(String username, BigDecimal amount) {
        User user = userService.findUser(username);
        if (user == null) {
            return "User not found";
        }

        Bank bank = user.getBanks();
        bank.setCoins(amount);
        userService.save(user);
        return "Operation successful!";
    }
}
