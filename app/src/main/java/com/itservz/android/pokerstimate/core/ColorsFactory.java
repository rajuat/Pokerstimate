package com.itservz.android.pokerstimate.core;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.itservz.android.pokerstimate.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorsFactory {

    private static ColorsFactory INSTANCE = null;
    private List<CardColor> pokerColors = null;

    private ColorsFactory(){
    }

    public static ColorsFactory getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = new ColorsFactory();
            INSTANCE.loadColorForPokers(context);
        }
        return INSTANCE;
    }

    public CardColor getRandomPokerColors(){
        return INSTANCE.randomViewFromRest(pokerColors);
    }

    private CardColor randomViewFromRest(List<CardColor> integers) {
        Random randomizer = new Random();
        return integers.get(randomizer.nextInt(integers.size()));
    }


    private void loadColorForPokers(Context context){
        pokerColors = new ArrayList<>();
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_deep_purple), ContextCompat.getColor(context, R.color.light_deep_purple)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_red), ContextCompat.getColor(context, R.color.light_red)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_purple), ContextCompat.getColor(context, R.color.light_purple)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_pink), ContextCompat.getColor(context, R.color.light_pink)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_indigo), ContextCompat.getColor(context, R.color.light_indigo)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_blue), ContextCompat.getColor(context, R.color.light_blue)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_light_blue), ContextCompat.getColor(context, R.color.light_light_blue)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_cyan), ContextCompat.getColor(context, R.color.light_cyan)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_teal), ContextCompat.getColor(context, R.color.light_teal)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_green), ContextCompat.getColor(context, R.color.light_green)));
        //pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_light_green), ContextCompat.getColor(context, R.color.light_light_green)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_lime), ContextCompat.getColor(context, R.color.light_lime)));
        //pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_yellow), ContextCompat.getColor(context, R.color.light_yellow)));
        //pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_amber), ContextCompat.getColor(context, R.color.light_amber)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_deep_orange), ContextCompat.getColor(context, R.color.light_deep_orange)));
        pokerColors.add(new CardColor(ContextCompat.getColor(context, R.color.dark_orange), ContextCompat.getColor(context, R.color.light_orange)));
    }


}
