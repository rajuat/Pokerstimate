package com.itservz.android.pokerstimate;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
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
    private ShakeDetector mShakeDetector;
    private boolean doubleBackToExitPressedOnce;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG_LOG, "onCreate");
        MyFont.initiazedFont(getApplicationContext());
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        initializeFragments();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        //mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        //mSensorManager.unregisterListener(mShakeDetector);
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
            cardListFragment.setShakeDetector(mShakeDetector);
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
        Log.d(TAG_LOG, "showListFragment " + getSupportFragmentManager().getBackStackEntryCount());
    }

    public void showGridFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_layout, cardGridFragment, TAG_CARD_GRID_FRAGMENT)
                .show(cardGridFragment)
                .addToBackStack(null)
                .commit();
        Log.d(TAG_LOG, "showGridFragment " + getSupportFragmentManager().getBackStackEntryCount());
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
