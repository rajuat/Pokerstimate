package com.itservz.android.pokerstimate;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.itservz.android.pokerstimate.core.Dealer;
import com.itservz.android.pokerstimate.core.DealerFactory;

public class CardGridFragment extends Fragment {

    private final View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int radius = (int) Math.hypot(right, bottom);
                Animator reveal = ViewAnimationUtils.createCircularReveal(v, right, bottom, 0, radius);
                reveal.setDuration(500);
                reveal.start();
            }
        }
    };

    private GridView gridView;

    public static CardGridFragment newInstance() {
        return new CardGridFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_grid, container, false);
        view.addOnLayoutChangeListener(onLayoutChangeListener);
        gridView = (GridView) view.findViewById(R.id.list);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getActivity()).selectCard(position);
                ((MainActivity)getActivity()).showListFragment();
            }
        });
        FloatingActionButton back = (FloatingActionButton) view.findViewById(R.id.floating_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showListFragment();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Dealer dealer = DealerFactory.newInstance(getActivity());
        CardsGridAdapter adapter = new CardsGridAdapter(getActivity(), dealer);
        gridView.setAdapter(adapter);
    }
}
