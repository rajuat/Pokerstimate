package com.itservz.android.pokerstimate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.itservz.android.pokerstimate.core.Dealer;
import com.itservz.android.pokerstimate.core.DealerFactory;
import com.itservz.android.pokerstimate.sensor.ShakeDetector;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CardListFragment extends Fragment {
    private static final String TAG_LOG = "CardListFragment";
    private ShakeDetector mShakeDetector;

    @InjectView(R.id.pager)
    ViewPager viewPager;

    public static CardListFragment newInstance() {
        return new CardListFragment();
    }

    private DrawerLayout drawerLayout = null;
    private NavigationView drawerList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG_LOG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        ButterKnife.inject(this, view);

        ImageButton button = (ImageButton) view.findViewById(R.id.drawer_settings);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerList = (NavigationView) getActivity().findViewById(R.id.left_drawer);
        drawerList.setNavigationItemSelectedListener(new DrawerItemSelectedListener());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerList);
            }
        });
        return view;
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
        String poker = "deckPreference";

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Log.d(TAG_LOG, "onNavigationItemSelected");
            SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = mPreferences.edit();
            int id = item.getItemId();
            item.setChecked(true);
            if (id == R.id.nav_standard_poker) {
                Log.d(TAG_LOG, "onNavigationItemSelected Standard");
                editor.putString(poker, "0");
                editor.commit();
                reload(id);
            } else if(id == R.id.nav_fibonacci_poker){
                Log.d(TAG_LOG, "onNavigationItemSelected Fibonacci");
                editor.putString(poker, "1");
                editor.commit();
                reload(id);
            }
            return true;
        }
    }

    private void reload(int id) {
        drawerLayout.closeDrawer(drawerList);
        viewCreated();
        ((MainActivity) getActivity()).showGridFragment();
        drawerList.setCheckedItem(id);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Log.d(TAG_LOG, "selectItem");
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        });
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().commit();


        drawerList.setCheckedItem(position);
        drawerLayout.closeDrawer(drawerList);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d(TAG_LOG, "PlanetFragmwnt onCreateView");
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.planets_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);
            return rootView;
        }
    }

    public void setShakeDetector(ShakeDetector mShakeDetector) {
        this.mShakeDetector = mShakeDetector;
    }

}
