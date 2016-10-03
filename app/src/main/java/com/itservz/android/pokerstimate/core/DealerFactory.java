package com.itservz.android.pokerstimate.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.itservz.android.pokerstimate.R;

import java.util.Arrays;
import java.util.List;

public class DealerFactory {

    private final Resources mResources;
    private SharedPreferences mPreferences;
    private Dealer mDealer;

    public static Dealer newInstance(Context context) {
        return new DealerFactory(context).mDealer;
    }

    public DealerFactory(Context context) {
        mResources = context.getResources();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mDealer = new Dealer(getCards());
    }


    private List<String> getCards() {
        int deckPref = Integer.parseInt(mPreferences.getString("deckPreference", "0"));
        DeckType selectedType = DeckType.values()[deckPref];
        return buildDeck(selectedType);
    }

    private List<String> buildDeck(DeckType type) {
        String[] cards = null;

        switch (type) {
            case STANDARD:
                cards = mResources.getStringArray(R.array.standard_cards);
                break;
            case FIBONACCI:
                cards = mResources.getStringArray(R.array.fibonacci_cards);
                break;
            case TSHIRT_SIZE:
                cards = mResources.getStringArray(R.array.tshirt_size_cards);
                break;
            default:
                break;
        }

        return Arrays.asList(cards);
    }
}
