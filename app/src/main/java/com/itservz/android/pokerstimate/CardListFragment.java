package com.itservz.android.pokerstimate;

import android.app.Activity;
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
import android.widget.ImageView;

import com.itservz.android.pokerstimate.core.Dealer;
import com.itservz.android.pokerstimate.core.DealerFactory;
import com.itservz.android.pokerstimate.sensor.ShakeDetector;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CardListFragment extends Fragment {
    private static final String TAG_LOG = "CardListFragment";
    private ShakeDetector mShakeDetector;
    private DrawerLayout drawerLayout = null;
    private NavigationView drawerNavigationView = null;
    ViewPager viewPager;

    //@InjectView(R.id.pager)

    public static CardListFragment newInstance() {
        return new CardListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG_LOG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        ButterKnife.inject(this, view);

        //drawer starts
        final SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        ImageView button = (ImageView) view.findViewById(R.id.drawer_settings);
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

    @OnClick(R.id.floating)
    @SuppressWarnings("unused")
    protected void onFloatingClick() {
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
            SharedPreferences.Editor editor = mPreferences.edit();
            int id = item.getItemId();
            if (id == R.id.nav_standard_poker) {
                //item.setChecked(true);
                Log.d(TAG_LOG, "onNavigationItemSelected Standard");
                editor.putString(Preferences.DECK_PREFERENCE.name(), "0");
                editor.commit();
                reload(id);
            } else if(id == R.id.nav_fibonacci_poker){
                //item.setChecked(true);
                Log.d(TAG_LOG, "onNavigationItemSelected Fibonacci");
                editor.putString(Preferences.DECK_PREFERENCE.name(), "1");
                editor.commit();
                reload(id);
            } else if(id == R.id.nav_tshirt_poker){
                //item.setChecked(true);
                Log.d(TAG_LOG, "onNavigationItemSelected TShirt");
                editor.putString(Preferences.DECK_PREFERENCE.name(), "2");
                editor.commit();
                reload(id);
            }
            return true;
        }
    }

    public void setShakeDetector(ShakeDetector mShakeDetector) {
        this.mShakeDetector = mShakeDetector;
    }

}
