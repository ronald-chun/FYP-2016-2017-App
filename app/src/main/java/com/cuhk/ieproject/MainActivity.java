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
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.cuhk.ieproject.model.Card;
import com.cuhk.ieproject.model.iBeacon;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

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

    int time = 1000;

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

    private static Toast toast;
    private static TextView toastText;

    int CAMERA_REQUEST;

    private static void makeTextAndShow(final Context context, final String text, final int duration) {
        if (toast == null) {
            final ViewGroup toastView = new FrameLayout(context);
            final FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            final GradientDrawable background = new GradientDrawable();
            toastText = new TextView(context);
            toastText.setLayoutParams(flp);
            toastText.setSingleLine(false);
            toastText.setTextSize(30);
            toastText.setTextColor(Color.argb(0xAA, 0xFF, 0xFF, 0xFF)); // white text
            background.setColor(Color.argb(0xAA, 0x00, 0xAA, 0x00)); // red toast
            background.setCornerRadius(50); // toast round corner
            toastView.setPadding(60, 60, 60, 60);
            toastView.addView(toastText);
            toastView.setBackgroundDrawable(background);
            toast = new Toast(context);
            toast.setView(toastView);
        }
        toastText.setText(text);
        toast.setDuration(duration);
        toast.show();
    }

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
        setupListener();
//        final TextView mTextView = (TextView) findViewById(R.id.textView2);
//
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="https://192.168.65.80:8080/admin/api/login?username=admin&password=admin";
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        mTextView.setText("Response is: "+ response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
//            }
//        });
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
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

                        Log.e(TAG, " " + nearestMacAddress);

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

    private void setupSize() {
        cardGV.setNumColumns(colNum);

        Display display = getWindowManager().getDefaultDisplay();
        height = display.getHeight();
        cardHeight = height * 3 / 2 / colNum;


        param = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                cardHeight);

    }

    private void setupCards() {
        if (tempLocation != location) {
            currentCards.clear();

            currentCards.add(new Card(-1, location, "相機", "camera_icon", -1, false));
            Log.e("cardSize", String.valueOf(cards.size()));

            for (int i = 0; i < cards.size(); i++) {
                Log.e("card", cards.get(i).toString());
                if (cards.get(i).getLocation() == location) {
                    currentCards.add(cards.get(i));
                }
            }

            cardAdapter = new CardListAdapter(location);
            cardGV.setAdapter(cardAdapter);
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
            public boolean onSwipeLeft(int fingers, long gestureDuration, double gestureDistance) {
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
            public boolean onSwipeRight(int fingers, long gestureDuration, double gestureDistance) {
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
            public boolean onPinch(int fingers, long gestureDuration, double gestureDistance) {
                return false;
            }

            @Override
            public boolean onUnpinch(int fingers, long gestureDuration, double gestureDistance) {
                return false;
            }

            @Override
            public boolean onDoubleTap(int fingers) {
                return false;
            }
        });
        background.setOnTouchListener(mySfg);
        background.setOnTouchListener(mySfg);
    }

    private void setupListener() {
        cardGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = getFile();
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    CAMERA_REQUEST = 9000 + tempLocation;
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    if (currentCards.get(position).isPhoto()) {
                        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                        intent.putExtra("path", currentCards.get(position).getImagePath());
                        startActivity(intent);
                    } else {
                        makeTextAndShow(getApplicationContext(), "你選擇的是 " + currentCards.get(position).getName(), Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }


    private class CardListAdapter extends ArrayAdapter<Card> {
        int location;


        public CardListAdapter(int location) {
            super(MainActivity.this, R.layout.card_item, currentCards);
            this.location = location;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.card_item, parent, false);
            }

            Card currentCard = currentCards.get(position);
            itemView.setLayoutParams(param);

            if (currentCard.isPhoto()) {
                Bitmap bitmap = decodeSampledBitmapFromFile(currentCard.getImagePath(), 1000, 700);
                ((ImageView) itemView.findViewById(R.id.card_image)).setImageBitmap(bitmap);
            } else {
                int resId = getResources().getIdentifier(currentCard.getImagePath(), "drawable", getPackageName());
                ((ImageView) itemView.findViewById(R.id.card_image)).setImageResource(resId);
            }

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
        } else {
            if (!bluetoothAdapter.isEnabled()) {
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
        } else if (requestCode >= 9000) {
            if (resultCode == Activity.RESULT_OK) {
                int size = cards.size() + 1;

                String filename = String.valueOf(size) + ".jpg";
                String path = "sdcard/ie_project/" + filename;
                int photoLocation = requestCode - 9000;
                Log.e("photoLocation", String.valueOf(photoLocation));
                cards.add(new Card(size, photoLocation, "", path, -1, true));
                setupCards();
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

    private File getFile() {
        File folder = new File("sdcard/ie_project");

        if (!folder.exists()) {
            folder.mkdir();
        }

        String filename = String.valueOf(cards.size() + 1) + ".jpg";

        File imageFile = new File(folder, filename);

        return imageFile;
    }

//    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH
//
//        //First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options);
//
//        // Calculate inSampleSize, Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        int inSampleSize = 1;
//
//        if (height > reqHeight) {
//            inSampleSize = Math.round((float) height / (float) reqHeight);
//        }
//        int expectedWidth = width / inSampleSize;
//
//        if (expectedWidth > reqWidth) {
//            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
//            inSampleSize = Math.round((float) width / (float) reqWidth);
//        }
//
//        options.inSampleSize = inSampleSize;
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//
//        return BitmapFactory.decodeFile(path, options);
//    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        int targetImageViewWidth = reqWidth;
        int targetImageViewHeight =  reqHeight;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(path, bmOptions);
        return photoReducedSizeBitmap;
    }

}
//28/2/2017/
//28/2/2017 kobe
//28/2/2017/ fai