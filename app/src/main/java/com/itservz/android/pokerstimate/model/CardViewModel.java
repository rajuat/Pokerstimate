package com.itservz.android.pokerstimate.model;

import android.content.Context;

import com.itservz.android.pokerstimate.R;
import com.itservz.android.pokerstimate.drawables.PokerDrawable;

import java.io.Serializable;

public class CardViewModel implements Serializable {
    private PokerDrawable upwardResourceId;
    private int downwardResourceId;
    private CardStatus status;

    public CardViewModel(Context context, boolean fullScreen) {
        this.status = CardStatus.UPWARDS;
        this.downwardResourceId = R.drawable.cover_big;
        this.upwardResourceId = new PokerDrawable(context, "1", fullScreen);
    }

    public int getDownwardResourceId() { return downwardResourceId; }

    public PokerDrawable getUpwardResourceId() { return upwardResourceId; }

    public CardStatus getStatus() { return status; }

    public void setUpwardResourceId(PokerDrawable upwardResourceId) {
        this.upwardResourceId = upwardResourceId;
    }

    public void setDownwardResourceId(int downwardResourceId) {
        this.downwardResourceId = downwardResourceId;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public enum CardStatus {
        UPWARDS,
        DOWNWARDS
    }
}
