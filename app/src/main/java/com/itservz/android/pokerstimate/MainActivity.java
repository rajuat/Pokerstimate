package com.itservz.android.pokerstimate;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.itservz.android.pokerstimate.fonts.MyFont;
import com.itservz.android.pokerstimate.sensor.ShakeDetector;

public class MainActivity extends FragmentActivity {
    private static final String TAG_LOG = "MainActivity";
    private static final String TAG_CARD_LIST_FRAGMENT = "CardListFragment";
    private static final String TAG_CARD_GRID_FRAGMENT = "CardGridFragment";
    private CardListFragment cardListFragment;
    private CardGridFragment cardGridFragment;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG_LOG, "onCreate");

        MyFont.initiazedFont(getApplicationContext());
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        initializeFragments();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(PreferenceManager.getDefaultSharedPreferences(
                getBaseContext()).getBoolean(Preferences.SHAKE.name(), false)){
            registerShake();
        }
    }

    void registerShake(){
        mSensorManager.registerListener(ShakeDetector.getInstance(), mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    void unregisterShake(){
        mSensorManager.unregisterListener(ShakeDetector.getInstance());
    }

    @Override
    public void onPause() {
        unregisterShake();
        super.onPause();
    }

    private void initializeFragments() {
        initializeListFragment();
        initializeGridFragment();
    }

    private void initializeListFragment() {
        cardListFragment = (CardListFragment)
                getSupportFragmentManager().findFragmentByTag(TAG_CARD_LIST_FRAGMENT);
        if (cardListFragment == null) {
            cardListFragment = CardListFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_layout, cardListFragment, TAG_CARD_LIST_FRAGMENT)
                    .show(cardListFragment)
                    .commit();
        }
    }

    private void initializeGridFragment() {
        cardGridFragment = (CardGridFragment)
                getSupportFragmentManager().findFragmentByTag(TAG_CARD_GRID_FRAGMENT);
        if (cardGridFragment == null) {
            cardGridFragment = CardGridFragment.newInstance();
        }
    }

    public void selectCard(int position) {
        cardListFragment.selectCard(position);
    }

    public void showListFragment() {
        getSupportFragmentManager().popBackStack();
    }

    public void showGridFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CARD_GRID_FRAGMENT);
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_layout, cardGridFragment, TAG_CARD_GRID_FRAGMENT)
                .show(cardGridFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        // When the user is at grid or drawer, simply close it - otherwise close the app in double back press
        final Fragment currentFragement = getSupportFragmentManager().findFragmentById(R.id.content_layout);
        if (currentFragement instanceof CardGridFragment) {
            super.onBackPressed();
        } else if (currentFragement instanceof CardListFragment) {
            if (cardListFragment.isNavDrawerOpen()) {
                cardListFragment.closeNavDrawer();
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }
}
