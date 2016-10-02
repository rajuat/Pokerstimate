package com.itservz.android.pokerstimate;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.itservz.android.pokerstimate.core.Card;
import com.itservz.android.pokerstimate.core.Dealer;
import com.itservz.android.pokerstimate.drawables.PokerDrawable;
import com.itservz.android.pokerstimate.model.CardViewModel;

import java.util.ArrayList;
import java.util.List;

public class CardsPagerAdapter extends FragmentPagerAdapter {
    private final Dealer dealer;
    private final List<CardFragment> fragmentList;
    private final List<CardViewModel> cardViewModelList;
    private CardFragment.OnCardStatusChangeListener onCardFragmentStateChange;
    private Context context;

    public CardsPagerAdapter(Context context, FragmentManager fragmentManager, Dealer dealer) {
        super(fragmentManager);
        this.dealer = dealer;
        this.context = context;
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
                for(int index = 0; index < cardViewModelList.size(); index++) {
                    cardViewModelList.get(index).setStatus(newStatus);
                    fragmentList.get(index).setCardStatus(newStatus);
                    dealer.flipDeck();
                }
            }
        };
    }

    private void initializeFragmentPool() {
        int count = dealer.getDeckLength();
        for(int index = 0; index < count; index++) {
            CardFragment fragment = new CardFragment();
            fragment.setOnCardStatusChangeListener(onCardFragmentStateChange);
            fragmentList.add(fragment);
        }
    }

    private void initializeCardModelPool() {
        int count = dealer.getDeckLength();
        for(int index = 0; index < count; index++) {
            CardViewModel cardViewModel = new CardViewModel(context, true);
            cardViewModel.setDownwardResourceId(R.drawable.cover_big);
            cardViewModel.setUpwardResourceId(new PokerDrawable(context, "TT", true));
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
        /*switch (card) {
            case ONE:
                upwardResourceId = R.drawable.card01_big;
                break;
            case TWO:
                upwardResourceId = R.drawable.card02_big;
                break;
            case THREE:
                upwardResourceId = R.drawable.card03_big;
                break;
            case FIVE:
                upwardResourceId = R.drawable.card04_big;
                break;
            case EIGHT:
                upwardResourceId = R.drawable.card05_big;
                break;
            case THIRTEEN:
                upwardResourceId = R.drawable.card06_big;
                break;
            case TWENTY:
                upwardResourceId = R.drawable.card07_big;
                break;
            case FORTY:
                upwardResourceId = R.drawable.card08_big;
                break;
            case HUNDRED:
                upwardResourceId = R.drawable.card09_big;
                break;
            case INFINITE:
                upwardResourceId = R.drawable.card10_big;
                break;
            case UNKNOWN:
                upwardResourceId = R.drawable.card11_big;
                break;
            case YAK_SHAVING:
                upwardResourceId = R.drawable.card12_big;
                break;
            case BROWN:
                upwardResourceId = R.drawable.card13_big;
                break;
            case PAUSE:
                upwardResourceId = R.drawable.card14_big;
                break;
        }*/
        return upwardResourceId;
    }

    @Override
    public int getCount() {
        return dealer.getDeckLength();
    }
}
