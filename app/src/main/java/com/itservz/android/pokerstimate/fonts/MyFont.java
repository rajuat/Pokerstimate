package com.itservz.android.pokerstimate.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;


public class MyFont {
    private static Typeface typeface;

    public static void initiazedFont(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/dinRegular.ttf");
    }

    public static void initiazedFontOld(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DejaVuSansCondensedBold.ttf");
    }

    public static Typeface getTypeface() {
        if (typeface != null) {
            Log.d("MyFont", typeface.toString());
            return typeface;
        } else {
            return Typeface.create("Arail", Typeface.BOLD);
        }
    }
}
