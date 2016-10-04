package com.itservz.android.pokerstimate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.itservz.android.pokerstimate.core.Dealer;
import com.itservz.android.pokerstimate.core.DealerFactory;
import com.itservz.android.pokerstimate.sensor.ShakeDetector;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CardListFragment extends Fragment {
    private static final String TAG_LOG = "CardListFragment";
    final String COMPANY_NAME = "COMPANY_NAME";
    final String TEAM_NAME = "TEAM_NAME";
    final String DECK_PREFERENCE = "deckPreference";
    private ShakeDetector mShakeDetector;

    @InjectView(R.id.pager)
    ViewPager viewPager;

    public static CardListFragment newInstance() {
        return new CardListFragment();
    }

    private DrawerLayout drawerLayout = null;
    private NavigationView drawerNavigationView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_LOG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        ButterKnife.inject(this, view);
        final SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        ImageView button = (ImageView) view.findViewById(R.id.drawer_settings);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerNavigationView = (NavigationView) getActivity().findViewById(R.id.left_drawer);
        View headerView = drawerNavigationView.getHeaderView(0);

        int deckPref = Integer.parseInt(mPreferences.getString("deckPreference", "0"));
        if(deckPref == 0){
            drawerNavigationView.setCheckedItem(R.id.nav_standard_poker);
        } else if(deckPref == 1){
            drawerNavigationView.setCheckedItem(R.id.nav_fibonacci_poker);
        } else if(deckPref == 2){
            drawerNavigationView.setCheckedItem(R.id.nav_tshirt_poker);
        }


        final TextInputLayout company_name_wrapper = (TextInputLayout) headerView.findViewById(R.id.company_name_wrapper);
        final TextInputLayout team_name_wrapper = (TextInputLayout) headerView.findViewById(R.id.team_name_wrapper);
        company_name_wrapper.setHint("Company name");
        team_name_wrapper.setHint("Team Name");

        TextInputEditText companyName = (TextInputEditText) headerView.findViewById(R.id.company_name);
        final TextInputEditText teamName = (TextInputEditText) headerView.findViewById(R.id.team_name);
        companyName.setText(mPreferences.getString(COMPANY_NAME, ""));
        teamName.setText(mPreferences.getString(TEAM_NAME, ""));

        teamName.setOnFocusChangeListener(new FocusChangeListener());

        drawerNavigationView.setNavigationItemSelectedListener(new DrawerItemSelectedListener());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerNavigationView);
            }
        });
        return view;
    }

    private class FocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = mPreferences.edit();
            if(!hasFocus) {
                if(v.getId() == R.id.company_name) {
                    editor.putString(COMPANY_NAME, ((TextInputEditText) v).getText().toString());
                } else if(v.getId() == R.id.team_name){
                    editor.putString(TEAM_NAME, ((TextInputEditText) v).getText().toString());
                }
                editor.commit();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                /*InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);*/
            }
        }
    }

    protected boolean isNavDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG_LOG, "onViewCreated");
        viewCreated();
    }

    private void viewCreated() {
        final Dealer dealer = DealerFactory.newInstance(getContext());
        CardsPagerAdapter cardsPagerAdapter = new CardsPagerAdapter(getActivity(), getFragmentManager(), dealer, mShakeDetector);
        this.viewPager.setAdapter(cardsPagerAdapter);
    }

    @OnClick(R.id.floating)
    @SuppressWarnings("unused")
    protected void onFloatingClick() {
        ((MainActivity) getActivity()).showGridFragment();
    }

    public void selectCard(int position) {
        viewPager.setCurrentItem(position, true);
    }

    private class DrawerItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Log.d(TAG_LOG, "onNavigationItemSelected");
            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = mPreferences.edit();
            int id = item.getItemId();
            if (id == R.id.nav_standard_poker) {
                item.setChecked(true);
                Log.d(TAG_LOG, "onNavigationItemSelected Standard");
                editor.putString(DECK_PREFERENCE, "0");
                editor.commit();
                reload(id);
            } else if(id == R.id.nav_fibonacci_poker){
                item.setChecked(true);
                Log.d(TAG_LOG, "onNavigationItemSelected Fibonacci");
                editor.putString(DECK_PREFERENCE, "1");
                editor.commit();
                reload(id);
            } else if(id == R.id.nav_tshirt_poker){
                item.setChecked(true);
                Log.d(TAG_LOG, "onNavigationItemSelected TShirt");
                editor.putString(DECK_PREFERENCE, "2");
                editor.commit();
                reload(id);
            }
            return true;
        }
    }

    private void reload(int id) {
        drawerLayout.closeDrawer(drawerNavigationView);
        viewCreated();
        ((MainActivity) getActivity()).showGridFragment();
        drawerNavigationView.setCheckedItem(id);
    }

    public void setShakeDetector(ShakeDetector mShakeDetector) {
        this.mShakeDetector = mShakeDetector;
    }

}
