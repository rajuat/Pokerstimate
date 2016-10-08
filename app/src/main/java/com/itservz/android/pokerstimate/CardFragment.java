package com.itservz.android.pokerstimate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itservz.android.pokerstimate.model.CardStatus;
import com.itservz.android.pokerstimate.model.CardViewModel;
import com.itservz.android.pokerstimate.widgets.CardView;


public class CardFragment extends Fragment {

    public interface OnCardStatusChangeListener {
        void onCardStatusChange(Fragment fragment, CardViewModel card, CardStatus newStatus);
    }

    private CardView cardView;
    private OnCardStatusChangeListener listener;
    private CardView.OnCardStatusChangeListener cardStatusChangeListener;

    public void setCardViewModel(CardViewModel cardViewModel) {
        this.cardViewModel = cardViewModel;
    }

    private CardViewModel cardViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void setListeners() {
        cardStatusChangeListener = new CardView.OnCardStatusChangeListener() {
            @Override
            public void onCardStatusChange(CardView view, CardViewModel card,
                                           CardStatus newStatus) {
                if (listener != null) {
                    listener.onCardStatusChange(CardFragment.this, card, newStatus);
                }
            }
        };
    }


    public void setOnCardStatusChangeListener(OnCardStatusChangeListener listener) {
        this.listener = listener;
    }

    public void setCardStatus(CardStatus status) {
        if (cardView != null) {
            if (status == CardStatus.DOWNWARDS) {
                cardView.hideCard();
            } else {
                cardView.revealCard();
            }
        } else if (cardViewModel != null) {
            cardViewModel.setStatus(status);
        }
    }


}
