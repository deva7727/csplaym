package com.dev.csplaym.game;

/**
 * @author deva7727
 * Created: 2024-12-24 05:49:33 UTC
 */
public enum GameMode {
    DEATHMATCH("Deathmatch"),
    TEAMBATTLE("Team Battle"),
    CTF("Capture The Flag");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}