/*
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.itservz.android.pokerstimate;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.ListView;

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
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
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
    }

    public void showGridFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_layout, cardGridFragment, TAG_CARD_GRID_FRAGMENT)
                .show(cardGridFragment)
                .addToBackStack(null)
                .commit();
    }
}
