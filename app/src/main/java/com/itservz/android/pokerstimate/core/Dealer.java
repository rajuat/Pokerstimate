package com.itservz.android.pokerstimate.core;

import com.itservz.android.pokerstimate.model.CardStatus;

import java.util.List;

public class Dealer {
    private final List<String> card;
    private CardStatus cardStatus;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    Dealer(List<String> card) {
        this.card = card;
        this.cardStatus = CardStatus.UPWARDS;
    }

    public int getDeckLength() {
        return card.size();
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public void flipDeck() {
        switch (cardStatus) {
            case UPWARDS:
                cardStatus = CardStatus.DOWNWARDS;
                break;
            case DOWNWARDS:
                cardStatus = CardStatus.UPWARDS;
                break;
        }
    }

    public String getCardAtPosition(int position) {
        return card.get(position);
    }


}
