/*
package com.itservz.android.pokerstimate.drawables;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.itservz.android.pokerstimate.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardsFactory {

    private static final CardsFactory INSTANCE = new CardsFactory();
    private List<Integer> smallCards;
    private List<Integer> bigCards;
    private static List<Integer> pokerColors;

    public static Integer getRandomBigCard(){
        return INSTANCE.randomViewFromRest(INSTANCE.bigCards);
    }

    public static Integer getRandomSmallCard(){
        return INSTANCE.randomViewFromRest(INSTANCE.smallCards);
    }

    public static Integer getRandomPokerColors(Context context){
        if(pokerColors == null){
            INSTANCE.loadColorForPokers(context);
        }
        return INSTANCE.randomViewFromRest(pokerColors);
    }

    private Integer randomViewFromRest(List<Integer> integers) {
        Random randomizer = new Random();
        return integers.get(randomizer.nextInt(integers.size()));
    }

    private CardsFactory(){
        loadBigBitmaps();
        loadSmallBitmaps();
    }

    private void loadBigBitmaps(){
        bigCards = new ArrayList<>();
        bigCards.add(R.drawable.card01_big);
        bigCards.add(R.drawable.card02_big);
        bigCards.add(R.drawable.card03_big);
        bigCards.add(R.drawable.card04_big);
        bigCards.add(R.drawable.card05_big);
        bigCards.add(R.drawable.card06_big);
        bigCards.add(R.drawable.card07_big);
        bigCards.add(R.drawable.card08_big);
        bigCards.add(R.drawable.card09_big);
        bigCards.add(R.drawable.card10_big);
        bigCards.add(R.drawable.card12_big);
        bigCards.add(R.drawable.card14_big);
    }

    private void loadSmallBitmaps(){
        smallCards = new ArrayList<>();
        smallCards.add(R.drawable.card01_small);
        smallCards.add(R.drawable.card02_small);
        smallCards.add(R.drawable.card03_small);
        smallCards.add(R.drawable.card04_small);
        smallCards.add(R.drawable.card05_small);
        smallCards.add(R.drawable.card06_small);
        smallCards.add(R.drawable.card07_small);
        smallCards.add(R.drawable.card08_small);
        smallCards.add(R.drawable.card09_small);
        smallCards.add(R.drawable.card10_small);
        smallCards.add(R.drawable.card12_small);
        smallCards.add(R.drawable.card14_small);
    }

    private void loadColorForPokers(Context context){
        pokerColors = new ArrayList<>();
        pokerColors.add(ContextCompat.getColor(context, R.color.deep_purple_poker));
        pokerColors.add(ContextCompat.getColor(context, R.color.blue_grey_poker));
        pokerColors.add(ContextCompat.getColor(context, R.color.brown_poker));
        pokerColors.add(ContextCompat.getColor(context, R.color.grey_poker));
        pokerColors.add(ContextCompat.getColor(context, R.color.cyan_poker));
        pokerColors.add(ContextCompat.getColor(context, R.color.blue_poker));
        pokerColors.add(ContextCompat.getColor(context, R.color.red_poker));
        pokerColors.add(ContextCompat.getColor(context, R.color.purple_poker));
    }
}
*/
