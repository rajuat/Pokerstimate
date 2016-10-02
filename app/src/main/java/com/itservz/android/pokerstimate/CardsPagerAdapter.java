package com.itservz.android.pokerstimate;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.itservz.android.pokerstimate.core.Card;
import com.itservz.android.pokerstimate.core.Dealer;
import com.itservz.android.pokerstimate.drawables.PokerDrawable;
import com.itservz.android.pokerstimate.model.CardViewModel;
import com.itservz.android.pokerstimate.sensor.ShakeDetector;

import java.util.ArrayList;
import java.util.List;

public class CardsPagerAdapter extends FragmentPagerAdapter {
    private final Dealer dealer;
    private final List<CardFragment> fragmentList;
    private final List<CardViewModel> cardViewModelList;
    private CardFragment.OnCardStatusChangeListener onCardFragmentStateChange;
    private Context context;
    private ShakeDetector mShakeDetector;

    public CardsPagerAdapter(Context context, FragmentManager fragmentManager, Dealer dealer, ShakeDetector mShakeDetector) {
        super(fragmentManager);
        this.dealer = dealer;
        this.context = context;
        this.mShakeDetector = mShakeDetector;
        this.fragmentList = new ArrayList<>(dealer.getDeckLength());
        this.cardViewModelList = new ArrayList<>(dealer.getDeckLength());
        initializeListener();
        initializeFragmentPool();
        initializeCardModelPool();
    }

    private void initializeListener() {
        onCardFragmentStateChange = new CardFragment.OnCardStatusChangeListener() {
            @Override
            public void onCardStatusChange(Fragment fragment, CardViewModel card, CardViewModel.CardStatus newStatus) {
                flip(newStatus);
            }
        };

        /*mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                flip(newStatus);
            }
        });*/
    }

    private void flip(CardViewModel.CardStatus newStatus){
        for(int index = 0; index < cardViewModelList.size(); index++) {
            cardViewModelList.get(index).setStatus(newStatus);
            fragmentList.get(index).setCardStatus(newStatus);
            dealer.flipDeck();
        }
    }

    private void initializeFragmentPool() {
        int count = dealer.getDeckLength();
        for(int index = 0; index < count; index++) {
            CardFragment fragment = new CardFragment();
            fragment.setOnCardStatusChangeListener(onCardFragmentStateChange);
            fragment.setShakeDetector(mShakeDetector);
            fragmentList.add(fragment);
        }
    }

    private void initializeCardModelPool() {
        int count = dealer.getDeckLength();
        for(int index = 0; index < count; index++) {
            CardViewModel cardViewModel = new CardViewModel(context, true);
            cardViewModel.setDownwardResourceId(R.drawable.cover_big);
            cardViewModel.setUpwardResourceId(new PokerDrawable(context, "", true));
            CardViewModel.CardStatus cardStatus = CardViewModel.CardStatus.UPWARDS;
            if (dealer.getDeckStatus() == Dealer.DeckStatus.DOWNWARDS) {
                cardStatus = CardViewModel.CardStatus.DOWNWARDS;
            }
            cardViewModel.setStatus(cardStatus);
            cardViewModelList.add(cardViewModel);
        }
    }

    @Override
    public Fragment getItem(int position) {
        CardFragment fragment = fragmentList.get(position);
        CardViewModel cardViewModel = cardViewModelList.get(position);
        cardViewModel.setUpwardResourceId(getUpwardResourceId(position));
        fragment.setCard(cardViewModel);
        return fragment;
    }

    private PokerDrawable getUpwardResourceId(int position) {
        Card card = dealer.getCardAtPosition(position);
        PokerDrawable upwardResourceId = new PokerDrawable(context, card.getValue(), true);
        return upwardResourceId;
    }

    @Override
    public int getCount() {
        return dealer.getDeckLength();
    }
}
