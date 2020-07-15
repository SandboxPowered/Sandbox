package org.sandboxpowered.sandbox.fabric.internal;

public enum RegistryOrder {
    GAMEMODE("gamemodes"),
    ITEM("items"),
    BLOCK("blocks"),
    TILE("tiles"),
    ENTITY("entities");

    private final String folder;

    RegistryOrder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}
