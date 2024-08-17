package org.ryjan.telegram.utils;

import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateContext {
    private static UpdateContext instance;
    private Update currentUpdate;

    private UpdateContext() {}

    public static UpdateContext getInstance() {
        if (instance == null) {
            instance = new UpdateContext();
        }
        return instance;
    }

    public void setUpdate(Update update) {
        this.currentUpdate = update;
    }

    public Update getUpdate() {
        return currentUpdate;
    }
}
