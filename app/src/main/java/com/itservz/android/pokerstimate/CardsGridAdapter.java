package com.itservz.android.pokerstimate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.itservz.android.pokerstimate.core.Dealer;
import com.itservz.android.pokerstimate.drawables.PokerDrawable;
import com.itservz.android.pokerstimate.model.CardViewModel;

import java.util.ArrayList;
import java.util.List;

public class CardsGridAdapter extends BaseAdapter {
    private final Context context;
    private final Dealer dealer;
    private final List<CardViewModel> cardViewModelList;

    public CardsGridAdapter(Context context, Dealer dealer) {
        this.context = context;
        this.dealer = dealer;
        cardViewModelList = new ArrayList<>();
        initializeCardModelPool();
    }

    private void initializeCardModelPool() {
        int count = dealer.getDeckLength();
        for(int index = 0; index < count; index++) {
            PokerDrawable upwardResourceId = getUpwardResourceId(index);
            CardViewModel cardViewModel = new CardViewModel(context, false);
            cardViewModel.setDownwardResourceId(R.drawable.cover_big);
            cardViewModel.setUpwardResourceId(upwardResourceId);
            cardViewModel.setStatus(CardViewModel.CardStatus.UPWARDS);
            cardViewModelList.add(cardViewModel);
        }
    }

    @Override
    public int getCount() {
        return dealer.getDeckLength();
    }

    @Override
    public Object getItem(int position) {
        return dealer.getCardAtPosition(position);
    }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;
        if (convertView == null) {
            result = LayoutInflater
                            .from(context)
                                .inflate(R.layout.item_small_card, null);
        }
        CardViewModel card = cardViewModelList.get(position);
        ImageView cardView = (ImageView)result.findViewById(R.id.card);
        cardView.setImageDrawable(card.getUpwardResourceId());
        result.setTag(card);
        return result;
    }

    private PokerDrawable getUpwardResourceId(int position) {
        String card = dealer.getCardAtPosition(position);
        return new PokerDrawable(context, card, false);
    }
}
