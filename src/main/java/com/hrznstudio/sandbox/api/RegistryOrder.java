package com.hrznstudio.sandbox.api;

public enum RegistryOrder {
    GAMEMODE("gamemodes"),
    ITEM("items"),
    BLOCK("blocks"),
    TILE("tiles"),
    ENTITY("entities");

    private String folder;

    RegistryOrder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}
