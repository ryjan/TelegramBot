package org.ryjan.telegram.utils;

public class SchemaContextHolder {

    private static final ThreadLocal<String> currentSchema = new ThreadLocal<>();

    public static void SetSchemaName(String schemaName) {
        currentSchema.set(schemaName);
    }

    public static String getSchemaName() {
        return currentSchema.get();
    }

    public static void clear() {
        currentSchema.remove();
    }
}
