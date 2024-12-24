package com.dev.csplaym.weapons;

public enum WeaponType {
    PISTOL("Pistol", 1),
    RIFLE("Rifle", 2),
    SNIPER("Sniper", 3),
    SHOTGUN("Shotgun", 2);

    private final String name;
    private final int tier;

    WeaponType(String name, int tier) {
        this.name = name;
        this.tier = tier;
    }

    public String getName() {
        return name;
    }

    public int getTier() {
        return tier;
    }
}