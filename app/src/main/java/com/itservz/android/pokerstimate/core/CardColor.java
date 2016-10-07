package com.itservz.android.pokerstimate.core;

public class CardColor {

    private int light, dark;

    public CardColor(int dark, int light) {
        this.dark = dark;
        this.light = light;
    }

    public int getLight() {
        return light;
    }

    public int getDark() {
        return dark;
    }
}
