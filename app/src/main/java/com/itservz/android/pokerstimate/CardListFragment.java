package com.itservz.android.pokerstimate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
    private DrawerLayout drawerLayout = null;
    private NavigationView drawerNavigationView = null;
    private ViewPager viewPager;

    public static CardListFragment newInstance() {
        return new CardListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        //Admob
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("A0A3D2227CBAA74DAC3C250E4861EED3")
                .build();
        mAdView.loadAd(adRequest);

        //drawer starts
        final SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.drawer_settings);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerNavigationView = (NavigationView) getActivity().findViewById(R.id.left_drawer);
        View headerView = drawerNavigationView.getHeaderView(0);

        int deckPref = Integer.parseInt(mPreferences.getString(Preferences.DECK_PREFERENCE.name(), "0"));
        if (deckPref == 0) {
            drawerNavigationView.setCheckedItem(R.id.nav_standard_poker);
        } else if (deckPref == 1) {
            drawerNavigationView.setCheckedItem(R.id.nav_fibonacci_poker);
        } else if (deckPref == 2) {
            drawerNavigationView.setCheckedItem(R.id.nav_tshirt_poker);
        }

        if (mPreferences.getBoolean(Preferences.SHAKE.name(), false)) {
            drawerNavigationView.getMenu().findItem(R.id.nav_shake).setTitle(getString(R.string.shake_turn_off));
        } else {
            drawerNavigationView.getMenu().findItem(R.id.nav_shake).setTitle(getString(R.string.shake_turn_on));
        }

        final TextInputLayout company_name_wrapper = (TextInputLayout) headerView.findViewById(R.id.company_name_wrapper);
        final TextInputLayout team_name_wrapper = (TextInputLayout) headerView.findViewById(R.id.team_name_wrapper);
        company_name_wrapper.setHint("Company name");
        team_name_wrapper.setHint("Team Name");

        TextInputEditText companyName = (TextInputEditText) headerView.findViewById(R.id.company_name);
        companyName.setText(mPreferences.getString(Preferences.COMPANY_NAME.name(), ""));
        companyName.setOnFocusChangeListener(new FocusChangeListener());
        final TextInputEditText teamName = (TextInputEditText) headerView.findViewById(R.id.team_name);
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
        buttonSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showGridFragment();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        viewCreated();
    }

    private class FocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = mPreferences.edit();
            if (!hasFocus) {
                if (v.getId() == R.id.company_name) {
                    editor.putString(Preferences.COMPANY_NAME.name(), ((TextInputEditText) v).getText().toString());
                } else if (v.getId() == R.id.team_name) {
                    editor.putString(Preferences.TEAM_NAME.name(), ((TextInputEditText) v).getText().toString());
                }
                editor.commit();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
        final Dealer dealer = DealerFactory.newInstance(getActivity());
        CardsPagerAdapter cardsPagerAdapter = new CardsPagerAdapter(getActivity(), getFragmentManager(), dealer);
        this.viewPager.setAdapter(cardsPagerAdapter);
    }

    private void reload(int id) {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        drawerLayout.closeDrawer(drawerNavigationView);
        ((MainActivity) getActivity()).showGridFragment();
    }

    private class DrawerItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

            int id = item.getItemId();
            if (id == R.id.nav_standard_poker) {
                item.setChecked(true);
                drawerNavigationView.getMenu().findItem(R.id.nav_fibonacci_poker).setChecked(false);
                drawerNavigationView.getMenu().findItem(R.id.nav_tshirt_poker).setChecked(false);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(Preferences.DECK_PREFERENCE.name(), "0");
                editor.commit();
                reload(id);
            } else if (id == R.id.nav_fibonacci_poker) {
                item.setChecked(true);
                drawerNavigationView.getMenu().findItem(R.id.nav_standard_poker).setChecked(false);
                drawerNavigationView.getMenu().findItem(R.id.nav_tshirt_poker).setChecked(false);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(Preferences.DECK_PREFERENCE.name(), "1");
                editor.commit();
                reload(id);
            } else if (id == R.id.nav_tshirt_poker) {
                item.setChecked(true);
                drawerNavigationView.getMenu().findItem(R.id.nav_standard_poker).setChecked(false);
                drawerNavigationView.getMenu().findItem(R.id.nav_fibonacci_poker).setChecked(false);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString(Preferences.DECK_PREFERENCE.name(), "2");
                editor.commit();
                reload(id);
            } else if (id == R.id.nav_shake) {
                //was true before
                if (mPreferences.getBoolean(Preferences.SHAKE.name(), false)) {
                    ((MainActivity) getActivity()).unregisterShake();
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(Preferences.SHAKE.name(), false);
                    editor.commit();
                    item.setTitle(getString(R.string.shake_turn_on));
                } else {
                    ((MainActivity) getActivity()).registerShake();
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean(Preferences.SHAKE.name(), true);
                    editor.commit();
                    item.setTitle(getString(R.string.shake_turn_off));
                }
            } else if (id == R.id.nav_rate) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.itservz.android.mayekplay")));
            }
            return true;
        }
    }

    public void selectCard(int position) {
        viewPager.setCurrentItem(position, true);
    }

}
