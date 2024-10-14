package org.ryjan.telegram.utils;

import org.ryjan.telegram.commands.users.user.UserPermissions;

public class Translation {
    protected String userNotFound(String username) {
        return "Пользователь " + username + " не найден😥";
    }

    protected String wrongCommand(String example, String commandName) {
        return "Введена неверная команда!\nПример: " + commandName + " " + example;
    }

    protected String noPermission(String commandName, UserPermissions userGroup) {
        return "У вас недостаточно прав для использования " + commandName + "\nВам нужны права: " + userGroup.getName();
    }

    protected String invalidAmount(String amount) {
        return "Введено неверное число😥";
    }
}
