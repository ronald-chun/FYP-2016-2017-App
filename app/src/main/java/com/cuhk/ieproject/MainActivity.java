package com.cuhk.ieproject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cuhk.ieproject.model.Card;
import com.cuhk.ieproject.model.User;
import com.cuhk.ieproject.model.iBeacon;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    LinearLayout firstLayout;
    LinearLayout secondLayout;
    LinearLayout cardArea;

    BeaconManager beaconManager;
    String nearestMacAddress;


    private static Toast toast;
    private static TextView toastText;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private static void makeTextAndShow(final Context context, final String text, final int duration) {
        if (toast == null) {
            //如果還沒有建立過Toast，才建立
            final ViewGroup toastView = new FrameLayout(context); // 用來裝toastText的容器
            final FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            final GradientDrawable background = new GradientDrawable();
            toastText = new TextView(context);
            toastText.setLayoutParams(flp);
            toastText.setSingleLine(false);
            toastText.setTextSize(30);
            toastText.setTextColor(Color.argb(0xAA, 0xFF, 0xFF, 0xFF)); // 設定文字顏色為有點透明的白色
            background.setColor(Color.argb(0xAA, 0x00, 0xAA, 0x00)); // 設定氣泡訊息顏色為有點透明的紅色
            background.setCornerRadius(50); // 設定氣泡訊息的圓角程度

            toastView.setPadding(60, 60, 60, 60); // 設定文字和邊界的距離
            toastView.addView(toastText);
            toastView.setBackgroundDrawable(background);

            toast = new Toast(context);
            toast.setView(toastView);
        }
        toastText.setText(text);
        toast.setDuration(duration);
        toast.show();
    }


    int height;
    int width;

    int MAX_VALUE = 2;
    int beaconSize;
    int location = Card.LOCATION_NULL;
    int tempLocation = Card.LOCATION_NULL;
    int tempSizeLevel = -1;

    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

    List<Card> cards = new ArrayList<>();
    List<iBeacon> ibeacons = new ArrayList<>();
    Integer[] locationScore;

    //    Dummy user for the sizeLevel: 0 = one row, 1 = two rows
//    User user = new User(1, "Chun", 1, 1);
    User user = new User(2, "Fai", 2, 0);

    private SimpleFingerGestures mySfg = new SimpleFingerGestures();
//    mySfg.setDebug(true);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupReferences();
        setupData();
        setupBeacon();
//        Toast.makeText(this, "Wellcome", Toast.LENGTH_LONG).show();


        // Register the background layout
        final LinearLayout background = (LinearLayout) findViewById(R.id.background);
        mySfg.setConsumeTouchEvents(true);
        mySfg.setOnFingerGestureListener(new SimpleFingerGestures.OnFingerGestureListener() {
            @Override
            public boolean onSwipeUp ( int fingers, long gestureDuration, double gestureDistance){
                // Change the picture size when 4 fingers swipe up
                if (fingers == 4) {
                    makeTextAndShow(getApplicationContext(), "變更圖片大小", Toast.LENGTH_LONG);
                    firstLayout.removeAllViews();
                    if (user.getSizeLevel() == 1) {
                        secondLayout.removeAllViews();
                    }
                    user.setSizeLevel((user.getSizeLevel() + 1) % 2);
                    startActivity(getIntent());
                    Log.d(TAG, "user.getSizeLevel(): " + user.getSizeLevel());
                    setupReferences();
                    setupData();
//                    setupBeacon();



                }
                return false;
            }

            @Override
            public boolean onSwipeDown ( int fingers, long gestureDuration, double gestureDistance){
                return false;
            }

            @Override
            public boolean onSwipeLeft ( int fingers, long gestureDuration, double gestureDistance){
                return false;
            }

            @Override
            public boolean onSwipeRight ( int fingers, long gestureDuration, double gestureDistance){
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupReferences() {
        firstLayout = (LinearLayout) findViewById(R.id.first_row);
        secondLayout = (LinearLayout) findViewById(R.id.second_row);
        cardArea = (LinearLayout) findViewById(R.id.card_area);
        // user level and view control
        if (user.getSizeLevel() == 0) {
            secondLayout.setVisibility(View.GONE);
        } else {
            secondLayout.setVisibility(View.VISIBLE);
        }

    }

    private void setupData() {
        Display display = getWindowManager().getDefaultDisplay();
        if (user.getSizeLevel() == 0) {
            height = (display.getHeight() * 90) / 100;
        } else {
            height = (display.getHeight() * 40) / 100;
        }
        width = height;
        cards = Card.dummy();
        ibeacons = iBeacon.dummy();
        beaconSize = ibeacons.size();
        locationScore = new Integer[beaconSize];
        for (int position = 0; position < beaconSize; position++) {
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
                            for (int position = 0; position < beaconSize; position++) {
                                iBeacon currentiBeacon = ibeacons.get(position);
                                if (nearestMacAddress.equals(currentiBeacon.getMacAddress())) {
                                    locationScore[position]++;
                                } else {
                                    locationScore[position] = 0;
                                }
                            }
                        }
                    }
                });

                for (int position = 0; position < beaconSize; position++) {
                    if (locationScore[position] >= MAX_VALUE) {
                        location = ibeacons.get(position).getLocationID();
                    }
                }

                if (location != Card.LOCATION_NULL) {
                    setupCards();
                }
            }
        });
    }

    private void setupCards() {
        if (tempLocation != location || tempSizeLevel != user.getSizeLevel()) {
            firstLayout.removeAllViews();
            if (user.getSizeLevel() == 1) {
                secondLayout.removeAllViews();
            }

            int count = 0;
            for (int i = 0; i < cards.size(); i++) {
                final Card currentCard = cards.get(i);

                if (currentCard.getLocation() == location) {
                    count++;
                    final ImageView imageView = new ImageView(this);
                    imageView.setImageResource(currentCard.getImagePath());
                    LinearLayout.LayoutParams layoutParms = new LinearLayout.LayoutParams(width, height);
                    layoutParms.setMargins(40, 0, 40, 0);
                    imageView.setLayoutParams(layoutParms);

                    if (user.getSizeLevel() == 0) {
                        firstLayout.addView(imageView);
                    } else {
                        if (count % 2 != 0) {
                            firstLayout.addView(imageView);
                        } else {
                            secondLayout.addView(imageView);
                        }
                    }

//                    imageView.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View view) {
//                            makeTextAndShow(getApplicationContext(), "你選擇的是 " + currentCard.getName(), Toast.LENGTH_LONG);
//                        }
//                    });


                    // change the image touch event 20161124 22:59
                    imageView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View arg0, MotionEvent arg1) {
                            switch (arg1.getAction()) {
                                case MotionEvent.ACTION_DOWN: {
                                    Drawable highlight = getResources().getDrawable(R.drawable.customborder);
                                    imageView.setBackgroundDrawable(highlight);
                                    makeTextAndShow(getApplicationContext(), "你選擇的是 " + currentCard.getName(), Toast.LENGTH_SHORT);
                                    break;
                                }
                                case MotionEvent.ACTION_UP: {
                                    imageView.setBackgroundDrawable(null);
                                    break;
                                }
                                case MotionEvent.ACTION_MOVE: {
                                    imageView.setBackgroundDrawable(null);
                                    break;
                                }
                            }
                            return true;
                        }
                    });
                }
            }
            tempLocation = location;
            tempSizeLevel = user.getSizeLevel();
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
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                connectToService();
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
        } catch (IllegalFormatException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }

        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
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


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
