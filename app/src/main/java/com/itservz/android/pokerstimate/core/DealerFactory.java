package com.itservz.android.pokerstimate.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import com.itservz.android.pokerstimate.Preferences;
import com.itservz.android.pokerstimate.R;

import java.util.Arrays;
import java.util.List;

public class DealerFactory {

    private final Resources mResources;
    private SharedPreferences mPreferences;
    private Dealer mDealer;
    private String deckType;

    public static Dealer newInstance(Context context) {
        return new DealerFactory(context).mDealer;
    }

    public DealerFactory(Context context) {
        mResources = context.getResources();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mDealer = new Dealer(getCards());
        mDealer.setType(deckType);
    }


    private List<String> getCards() {
        int deckPref = Integer.parseInt(mPreferences.getString(Preferences.DECK_PREFERENCE.name(), "0"));
        DeckType selectedType = DeckType.values()[deckPref];
        return buildDeck(selectedType);
    }

    private List<String> buildDeck(DeckType type) {
        String[] cards = null;

        switch (type) {
            case STANDARD:
                cards = mResources.getStringArray(R.array.standard_cards);
                deckType = DeckType.STANDARD.name();
                break;
            case FIBONACCI:
                cards = mResources.getStringArray(R.array.fibonacci_cards);
                deckType = DeckType.FIBONACCI.name();
                break;
            case TSHIRT_SIZE:
                cards = mResources.getStringArray(R.array.tshirt_size_cards);
                deckType = DeckType.TSHIRT_SIZE.name();
                break;
            default:
                break;
        }
        List<String> strings = Arrays.asList(cards);
        Log.d("DealerFactory", "" + strings.toString());
        return strings;
    }
}
