package org.ryjan.telegram.builders;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardBuilder {
    private final ReplyKeyboardMarkup replyKeyboardMarkup;
    private final List<KeyboardRow> keyboardRows;

    public ReplyKeyboardBuilder() {
        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        keyboardRows = new ArrayList<>();
    }

    public ReplyKeyboardBuilder addRow(String... buttonLabels) {
        KeyboardRow keyboardRow = new KeyboardRow();
        for (String buttonLabel : buttonLabels) {
            keyboardRow.add(buttonLabel);
        }
        keyboardRows.add(keyboardRow);
        return this;
    }

    public ReplyKeyboardMarkup build() {
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
}
