package com.itservz.android.pokerstimate;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
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
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.itservz.android.pokerstimate.core.Dealer;
import com.itservz.android.pokerstimate.core.DealerFactory;
import com.itservz.android.pokerstimate.sensor.ShakeDetector;

public class CardListFragment extends Fragment {
    private static final String TAG_LOG = "CardListFragment";
    private ShakeDetector mShakeDetector;
    private DrawerLayout drawerLayout = null;
    private NavigationView drawerNavigationView = null;
    private ViewPager viewPager;

    public static CardListFragment newInstance() {
        return new CardListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_LOG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        //Admob
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        mAdView.loadAd(adRequest);

        //drawer starts
        final SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.drawer_settings);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerNavigationView = (NavigationView) getActivity().findViewById(R.id.left_drawer);
        View headerView = drawerNavigationView.getHeaderView(0);

        int deckPref = Integer.parseInt(mPreferences.getString(Preferences.DECK_PREFERENCE.name(), "0"));
        if(deckPref == 0){
            drawerNavigationView.setCheckedItem(R.id.nav_standard_poker);
        } else if(deckPref == 1){
            drawerNavigationView.setCheckedItem(R.id.nav_fibonacci_poker);
        } else if(deckPref == 2){
            drawerNavigationView.setCheckedItem(R.id.nav_tshirt_poker);
        }

        if(mPreferences.getBoolean(Preferences.SHAKE.name(), false)){
            drawerNavigationView.setCheckedItem(R.id.nav_shake);
        }

        final TextInputLayout company_name_wrapper = (TextInputLayout) headerView.findViewById(R.id.company_name_wrapper);
        final TextInputLayout team_name_wrapper = (TextInputLayout) headerView.findViewById(R.id.team_name_wrapper);
        company_name_wrapper.setHint("Company name");
        team_name_wrapper.setHint("Team Name");

        TextInputEditText companyName = (TextInputEditText) headerView.findViewById(R.id.company_name);
        final TextInputEditText teamName = (TextInputEditText) headerView.findViewById(R.id.team_name);
        companyName.setText(mPreferences.getString(Preferences.COMPANY_NAME.name(), ""));
        teamName.setText(mPreferences.getString(Preferences.TEAM_NAME.name(), ""));

        teamName.setOnFocusChangeListener(new FocusChangeListener());

        drawerNavigationView.setNavigationItemSelectedListener(new DrawerItemSelectedListener());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerNavigationView);
            }
        });
        //drawer ends

        FloatingActionButton buttonSwap = (FloatingActionButton) view.findViewById(R.id.floating);
        buttonSwap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showGridFragment();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG_LOG, "onViewCreated");
        viewCreated();
    }

    private class FocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = mPreferences.edit();
            if(!hasFocus) {
                if(v.getId() == R.id.company_name) {
                    editor.putString(Preferences.COMPANY_NAME.name(), ((TextInputEditText) v).getText().toString());
                } else if(v.getId() == R.id.team_name){
                    editor.putString(Preferences.TEAM_NAME.name(), ((TextInputEditText) v).getText().toString());
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

    private void viewCreated() {
        Log.d(TAG_LOG, "viewCreated");
        final Dealer dealer = DealerFactory.newInstance(getActivity());
        CardsPagerAdapter cardsPagerAdapter = new CardsPagerAdapter(getActivity(), getFragmentManager(), dealer, mShakeDetector);
        this.viewPager.setAdapter(cardsPagerAdapter);
    }

    private void reload(int id) {
        drawerNavigationView.setCheckedItem(id);
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        drawerLayout.closeDrawer(drawerNavigationView);
        ((MainActivity) getActivity()).showGridFragment();
    }

    public void selectCard(int position) {
        viewPager.setCurrentItem(position, true);
        Log.d("CardListFragment", position + " :selectCard: ");
    }

    private class DrawerItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Log.d(TAG_LOG, "onNavigationItemSelected");
            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

            int id = item.getItemId();
            if (id == R.id.nav_standard_poker) {
                //item.setChecked(true);
                Log.d(TAG_LOG, "onNavigationItemSelected Standard");
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(Preferences.DECK_PREFERENCE.name(), "0");
                editor.commit();
                reload(id);
            } else if(id == R.id.nav_fibonacci_poker){
                //item.setChecked(true);
                Log.d(TAG_LOG, "onNavigationItemSelected Fibonacci");
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(Preferences.DECK_PREFERENCE.name(), "1");
                editor.commit();
                reload(id);
            } else if(id == R.id.nav_tshirt_poker){
                //item.setChecked(true);
                Log.d(TAG_LOG, "onNavigationItemSelected TShirt");
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(Preferences.DECK_PREFERENCE.name(), "2");
                editor.commit();
                reload(id);
            } else if(id == R.id.nav_shake){
                Log.d(TAG_LOG, "onNavigationItemSelected Shake");
                //was true before
                if(mPreferences.getBoolean(Preferences.SHAKE.name(), false)){
                    ((MainActivity) getActivity()).unregisterShake();
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(Preferences.SHAKE.name(), false);
                    editor.commit();
                    item.setChecked(false);
                } else {
                    ((MainActivity) getActivity()).registerShake();
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(Preferences.SHAKE.name(), true);
                    editor.commit();
                    item.setChecked(true);
                }
            } else if(id == R.id.nav_rate){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.itservz.android.mayekplay")));
            }
            return true;
        }
    }

    public void setShakeDetector(ShakeDetector mShakeDetector) {
        this.mShakeDetector = mShakeDetector;
    }

}
