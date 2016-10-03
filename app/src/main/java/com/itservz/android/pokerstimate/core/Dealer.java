package com.itservz.android.pokerstimate.core;

import java.util.List;

public class Dealer {
    private final List<String> deck;
    private DeckStatus deckStatus;

    Dealer(List<String> deck) {
        this.deck = deck;
        this.deckStatus = DeckStatus.UPWARDS;
    }

    public int getDeckLength() {
        return deck.size();
    }

    public DeckStatus getDeckStatus() {
        return deckStatus;
    }

    public void flipDeck() {
        switch (deckStatus) {
            case UPWARDS:
                deckStatus = DeckStatus.DOWNWARDS;
                break;
            case DOWNWARDS:
                deckStatus = DeckStatus.UPWARDS;
                break;
        }
    }

    public String getCardAtPosition(int position) {
        return deck.get(position);
    }

    public enum DeckStatus {
        UPWARDS,
        DOWNWARDS
    }

}
