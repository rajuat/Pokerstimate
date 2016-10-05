package com.itservz.android.pokerstimate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itservz.android.pokerstimate.model.CardStatus;
import com.itservz.android.pokerstimate.model.CardViewModel;
import com.itservz.android.pokerstimate.sensor.ShakeDetector;
import com.itservz.android.pokerstimate.widgets.CardView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CardFragment extends Fragment {
    private static final String KEY_INITIAL_MODEL = "com.itservz.android.pokerstimate.KEY_INITIAL_MODEL";

    public interface OnCardStatusChangeListener {
        void onCardStatusChange(Fragment fragment, CardViewModel card, CardStatus newStatus);
    }

    @InjectView(R.id.card)
    CardView cardView;
    private OnCardStatusChangeListener listener;
    private CardView.OnCardStatusChangeListener cardStatusChangeListener;
    private ShakeDetector mShakeDetector;
    private CardViewModel cardViewModel;

    public ShakeDetector getShakeDetector() {
        return mShakeDetector;
    }

    public void setShakeDetector(ShakeDetector mShakeDetector) {
        this.mShakeDetector = mShakeDetector;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        cardViewModel = (CardViewModel) this.getArguments().getSerializable("CardViewModel");
        super.onCreate(savedInstanceState);
        setListeners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.inject(this, view);
        //Bundle bundle = this.getArguments();
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

    public CardViewModel getCardViewModel() {
        return cardViewModel;
    }

    /*public void setCardViewModel(CardViewModel cardViewModel) {
        this.cardViewModel = cardViewModel;
    }*/

}
