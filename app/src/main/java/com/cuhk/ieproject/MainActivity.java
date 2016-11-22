package com.cuhk.ieproject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cuhk.ieproject.model.Card;
import com.cuhk.ieproject.model.User;
import com.cuhk.ieproject.model.iBeacon;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    LinearLayout firstLayout;
    LinearLayout secondLayout;

    BeaconManager beaconManager;
    String nearestMacAddress;

    int height;
    int width;

    int MAX_VALUE = 2;
    int beaconSize;
    int location = Card.LOCATION_NULL;
    int tempLocation = Card.LOCATION_NULL;

    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

    List<Card> cards = new ArrayList<>();
    List<iBeacon> ibeacons = new ArrayList<>();

    Integer[] locationScore;

//    Dummy user for the sizeLevel
//    User user = new User(1, "Chun", 1, 2);
    User user = new User(2, "Fai", 2 , 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupReferences();
        setupData();
        setupBeacon();
//        Toast.makeText(this, "Wellcome", Toast.LENGTH_LONG).show();

    }

    private void setupReferences() {
        firstLayout = (LinearLayout) findViewById(R.id.first_row);
        secondLayout = (LinearLayout) findViewById(R.id.second_row);

        if (user.getSizeLevel() == 1) {
            secondLayout.setVisibility(View.GONE);
        } else {
            secondLayout.setVisibility(View.VISIBLE);
        }

    }

    private void setupData() {
        Display display = getWindowManager().getDefaultDisplay();
        if (user.getSizeLevel() == 1) {
            height = (display.getHeight() * 90) / 100;
        } else {
            height = (display.getHeight() * 40)/100;
        }
        width = height;
        cards = Card.dummy();
        ibeacons = iBeacon.dummy();
        beaconSize = ibeacons.size();
        locationScore = new Integer[beaconSize];
        for (int position = 0; position < beaconSize; position++){
            locationScore[position] = 0;
        }
    }

    private void setupBeacon() {
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // NotSystem.out.print(...)e that beacons reported here are already sorted by estimated
                        // distance between device and beacon.
                        Beacon nearestBeacon = null;

                        if (beacons.size() > 0) {
                            for (Beacon rangedBeacon : beacons) {
                                if (nearestBeacon == null)
                                    nearestBeacon = rangedBeacon;
                                if (Utils.computeAccuracy(rangedBeacon) < Utils.computeAccuracy(nearestBeacon))
                                    nearestBeacon = rangedBeacon;
                            }
                            nearestMacAddress = String.valueOf(nearestBeacon.getMacAddress());

                        }

                        if (nearestMacAddress != null) {
                            for (int position = 0; position < beaconSize; position++){
                                iBeacon currentiBeacon = ibeacons.get(position);
                                if (nearestMacAddress.equals(currentiBeacon.getMacAddress())){
                                    locationScore[position]++;
                                }else{
                                    locationScore[position] = 0;
                                }
                            }
                        }
                    }
                });

                for (int position = 0; position < beaconSize; position++){
                    if (locationScore[position] >= MAX_VALUE){
                        location = ibeacons.get(position).getLocationID();
                    }
                }

                if (location!= Card.LOCATION_NULL){
                    setupCards();
                }
            }
        });
    }

    private void setupCards() {
        if (tempLocation != location){
            firstLayout.removeAllViews();
            if(user.getSizeLevel() == 2) {
                secondLayout.removeAllViews();
            }

            int count = 0;
            for (int i = 0; i < cards.size(); i++) {
                final Card currentCard = cards.get(i);

                if (currentCard.getLocation() == location) {
                    count++;
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(currentCard.getImagePath());
                    LinearLayout.LayoutParams layoutParms = new LinearLayout.LayoutParams(width,height);
                    layoutParms.setMargins(40, 0, 40, 0);
                    imageView.setLayoutParams(layoutParms);

                    if(user.getSizeLevel() == 1) {
                        firstLayout.addView(imageView);
                    } else {
                        if (count%2 != 0) {
                            firstLayout.addView(imageView);
                        } else {
                            secondLayout.addView(imageView);
                        }
                    }

                    imageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(), "你選擇的是 " + currentCard.getName(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }
            tempLocation = location;
        }
        location = Card.LOCATION_NULL;
    }

    @Override
    protected void onDestroy() {
        beaconManager.disconnect();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
        }else{
            if (!bluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                connectToService();
            }
        }
    }

    @Override
    protected void onStop() {
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
        } catch (IllegalFormatException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                connectToService();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connectToService() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
                } catch (IllegalFormatException e) {
                    Toast.makeText(MainActivity.this, "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }
}
