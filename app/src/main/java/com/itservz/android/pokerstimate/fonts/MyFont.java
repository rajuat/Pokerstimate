package com.itservz.android.pokerstimate.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;


public class MyFont {
    private static Typeface dinTypeface;
    private static Typeface trenchTypeface;

    public static void initiazedFont(Context context) {
        dinTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/dinRegular.ttf");
        trenchTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/trench100free.ttf");
    }

    public static void initiazedFontOld(Context context) {
    }

    public static Typeface getDinTypeface() {
        if (dinTypeface != null) {
            return dinTypeface;
        } else {
            return Typeface.create("Arail", Typeface.BOLD);
        }
    }
    public static Typeface getTrenchTypeface() {
        if (trenchTypeface != null) {
            return trenchTypeface;
        } else {
            return Typeface.create("Arail", Typeface.BOLD);
        }
    }
}
