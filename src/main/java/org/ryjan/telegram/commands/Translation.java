package org.ryjan.telegram.commands;

import org.ryjan.telegram.commands.user.UserGroup;

public class Translation {
    protected String userNotFound(String username) {
        return "Пользователь " + username + " не найден😥";
    }

    protected String wrongCommand(String example, String commandName) {
        return "Введена неверная команда!\nПример: " + commandName + " " + example;
    }

    protected String noPermission(String commandName, UserGroup userGroup) {
        return "У вас недостаточно прав для использования " + commandName + "\nВам нужны права: " + userGroup.getDisplayname();
    }

    protected String invalidAmount(String amount) {
        return "Введено неверное число😥";
    }
}
