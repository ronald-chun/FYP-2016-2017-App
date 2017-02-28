package com.cuhk.ieproject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.cuhk.ieproject.model.Card;
import com.cuhk.ieproject.model.User;
import com.cuhk.ieproject.model.iBeacon;
import com.cuhk.ieproject.util.RequestWorker;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.UUID;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

import static com.cuhk.ieproject.R.drawable.user;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    LinearLayout firstLayout;
    LinearLayout secondLayout;

    BeaconManager beaconManager;
    String nearestMacAddress;

    GridView cardGV;

    int height;
    int width;
    int cardHeight;
    int colNum = 4;

    int MAX_VALUE = 2;
    int beaconSize;
    int location = Card.LOCATION_NULL;
    int tempLocation = Card.LOCATION_NULL;

    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

    ArrayList<Card> cards = new ArrayList<>();
    ArrayList<Card> currentCards = new ArrayList<>();

    ArrayAdapter<Card> cardAdapter;

    ArrayList<iBeacon> ibeacons = new ArrayList<>();
    Integer[] locationScore;

    AbsListView.LayoutParams param;

    private SimpleFingerGestures mySfg = new SimpleFingerGestures();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        test();
        setContentView(R.layout.activity_main);

        setupReferences();
        setupGesture();
        setupSize();
        setupData();
        setupBeacon();
    }

//    private void test() {
//
//        RequestWorker.getInstance(MainActivity.this).addToRequestQueue(new User.TestRequest(MainActivity.this, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e("result", response.toString());
//
//            }
//        }, new RequestWorker.ErrorListener() {
//            @Override
//            public void onErrorResponse(Error error) {
//                Log.e("err","err");
//            }
//        }));
//    }

    private void setupReferences() {
        cardGV = (GridView) findViewById(R.id.card_list);
    }

    private void setupData() {

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
                        // Note that beacons reported here are already sorted by estimated
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

                        Log.e(TAG, " " +nearestMacAddress);

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

    private void setupSize() {
        cardGV.setNumColumns(colNum);

        Display display = getWindowManager().getDefaultDisplay();
        height = display.getHeight();
        cardHeight = height*3/2/colNum;


        param = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                cardHeight);

    }

    private void setupCards() {
        if (tempLocation != location) {
            currentCards.clear();

            for (int i=0; i<cards.size(); i++){
                if (cards.get(i).getLocation() == location){
                    currentCards.add(cards.get(i));
                }
            }

            cardAdapter = new CardListAdapter(location);
            cardGV.setAdapter(cardAdapter);
            // display card info while click card
            cardGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "This is "+ view.getTag(), Toast.LENGTH_SHORT).show();
                }
            });
            tempLocation = location;
        }
        location = Card.LOCATION_NULL;

    }

    private void setupGesture() {
        final LinearLayout background = (LinearLayout) findViewById(R.id.background);
        mySfg.setConsumeTouchEvents(true);
        mySfg.setOnFingerGestureListener(new SimpleFingerGestures.OnFingerGestureListener() {


            @Override
            public boolean onSwipeUp(int fingers, long gestureDuration, double gestureDistance) {
                //cardGV.scrollTo(500,1000);
                return false;
            }

            @Override
            public boolean onSwipeDown(int fingers, long gestureDuration, double gestureDistance) {
                //cardGV.scrollTo(700,200);
                return false;
            }

            @Override
            public boolean onSwipeLeft ( int fingers, long gestureDuration, double gestureDistance){
                Log.e("Test", "LEFT");
                if (fingers >= 2) {
                    if (colNum >= 2) {
                        colNum = colNum - 1;
                        setupSize();
                        setupCards();
                    }

                }
                return false;
            }

            @Override
            public boolean onSwipeRight ( int fingers, long gestureDuration, double gestureDistance){
                Log.e("Test", "RIGHT");
                if (fingers >= 2) {
                    if (colNum <= 8) {
                        colNum = colNum + 1;
                        setupSize();
                        setupCards();
                    }

                }
                return false;
            }

            @Override
            public boolean onPinch ( int fingers, long gestureDuration, double gestureDistance){
                return false;
            }

            @Override
            public boolean onUnpinch ( int fingers, long gestureDuration, double gestureDistance){
                return false;
            }

            @Override
            public boolean onDoubleTap ( int fingers){
                return false;
            }
        });
        background.setOnTouchListener(mySfg);
        background.setOnTouchListener(mySfg);
    }

    private class CardListAdapter extends ArrayAdapter<Card> {
        int location;

        public CardListAdapter(int location) {
            super(MainActivity.this, R.layout.card_item, currentCards);
            this.location = location;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.e("request", "YES");
            Card currentCard = currentCards.get(position);
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.card_item, parent, false);

            }

            itemView.setLayoutParams(param);

            ((ImageView) itemView.findViewById(R.id.card_image)).setImageResource(currentCard.getImagePath());
            // return card info to toast display
            itemView.setTag(currentCards.get(position).getName());


            return itemView;
        }
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
