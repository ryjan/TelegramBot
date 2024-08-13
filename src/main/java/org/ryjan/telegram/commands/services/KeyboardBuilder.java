package org.ryjan.telegram.commands.services;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardBuilder {

    public static class ButtonRow {
        private final List<InlineKeyboardButton> buttons = new ArrayList<>();

        public ButtonRow addButton(String text, String name) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(text);
            button.setCallbackData(name);
            buttons.add(button);
            return this;
        }

        public List<InlineKeyboardButton> build() {
            return buttons;
        }
    }

    public static class KeyboardLayer {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        public KeyboardLayer addRow(ButtonRow button) {
            rows.add(button.build());
            return this;
        }

        public List<List<InlineKeyboardButton>> build() {
            return rows;
        }
    }

    public static class Keyboard {
        public static final List<List<List<InlineKeyboardButton>>> layers = new ArrayList<>();

        public Keyboard addLayer(KeyboardLayer keyboardLayer) {
            layers.add(keyboardLayer.build());
            return this;
        }

        public List<List<List<InlineKeyboardButton>>> build() {
            return layers;
        }
    }
}
