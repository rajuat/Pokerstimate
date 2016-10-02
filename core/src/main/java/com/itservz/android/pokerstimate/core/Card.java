package com.itservz.android.pokerstimate.core;

public enum Card {
    ZERO("0"),
    HALF("1/2"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FIVE("5"),
    EIGHT("8"),
    THIRTEEN("13"),
    TWENTY("20"),
    FORTY("40"),
    HUNDRED("100"),
    INFINITE("-1"),
    UNKNOWN("?"),
    BREAK("-");

    private final String value;

    Card(final String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}