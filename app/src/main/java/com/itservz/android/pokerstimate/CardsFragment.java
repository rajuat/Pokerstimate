/*
package com.itservz.android.pokerstimate;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.itservz.android.pokerstimate.core.Dealer;
import com.itservz.android.pokerstimate.core.DealerFactory;
import com.itservz.android.pokerstimate.model.CardStatus;
import com.itservz.android.pokerstimate.model.CardViewModel;
import com.itservz.android.pokerstimate.widgets.CardView;

public class CardsFragment extends ListFragment {
    int mNum;
    private CardView cardView;
    private CardViewModel cardViewModel;
    private CardFragment.OnCardStatusChangeListener listener;
    private CardView.OnCardStatusChangeListener cardStatusChangeListener;
    private Dealer dealer;

    static CardsFragment newInstance(int num) {
        CardsFragment f = new CardsFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        setListeners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        cardView = (CardView) view.findViewById(R.id.card);
        cardView.setCard(cardViewModel);
        cardView.setOnCardViewStatusChangeListener(cardStatusChangeListener);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<CardView>(getActivity(),
                R.layout.fragment_card,
                DealerFactory.newInstance(getContext()).));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }

    private void setListeners() {
        cardStatusChangeListener = new CardView.OnCardStatusChangeListener() {
            @Override
            public void onCardStatusChange(CardView view, CardViewModel card, CardStatus newStatus) {
                if (listener != null) {
                    listener.onCardStatusChange(CardsFragment.this, card, newStatus);
                }
            }
        };
    }

    private void initializeFragmentPool() {
        int count = dealer.getDeckLength();
        for(int index = 0; index < count; index++) {
            cardView
            CardViewModel cardViewModel = new CardViewModel(getContext(), true, dealer.getCardAtPosition(index));
            cardViewModel.setStatus(dealer.getCardStatus());
            CardFragment fragment = new CardFragment();
            fragment.setCardViewModel(cardViewModel);
            fragment.setOnCardStatusChangeListener(onCardFragmentStateChange);
            fragmentList.add(fragment);
        }
    }

    public void setOnCardStatusChangeListener(CardFragment.OnCardStatusChangeListener listener) {
        this.listener = listener;
    }

    public void setCardViewModel(CardViewModel cardViewModel) {
        this.cardViewModel = cardViewModel;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }
}
*/
