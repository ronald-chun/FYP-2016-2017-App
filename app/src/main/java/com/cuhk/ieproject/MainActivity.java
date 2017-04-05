package com.cuhk.ieproject;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cuhk.ieproject.model.Card;
import com.cuhk.ieproject.model.iBeacon;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

//import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = "MainActivity";

    LinearLayout firstLayout;
    LinearLayout secondLayout;

    BeaconManager beaconManager;
    String nearestMacAddress;
    String nearestMajor;
    String nearestMinor;
    String nearestMeasuredPower;
    String nearestRssi;
    String response;
    String api_loc = "loc/B9407F30-F5F8-466E-AFF9-25556B57FE6D/";

    GridView cardGV;
    Context context = MainActivity.this;
    int height;
    int width;
    int cardHeight;
    int colNum = 4;
    int doubleCK = 0;

    TextToSpeech textToSpeech;

    private Session session;
    private Button btnSetting;

    private Setting mySetting;

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

    ImageView imageView;
    private static Toast toast;
    private static TextView toastText;

    int CAMERA_REQUEST;
    private static final int SETTING_REQUEST = 8000;

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

        File cacheDir = StorageUtils.getCacheDirectory(context);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(1000)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .defaultDisplayImageOptions(options) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

        textToSpeech = new TextToSpeech(MainActivity.this, MainActivity.this);
//        test();
        session = new Session(this);
        mySetting = new Setting(this);
        if(!session.loggedin()){
            logout();
        }
        setContentView(R.layout.activity_main);
        setupSettingBtn();
        setupReferences();
        setupGesture();
        mySetting.setCardSize(mySetting.getCardSize());
        setupSize();
        setupData();
        setupBeacon();
        HttpsTrustManager.allowAllSSL();

        // Create global configuration and initialize ImageLoader with this config
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
//        ImageLoader.getInstance().init(config);
//        String url = "https://192.168.65.80:8080/uploads/Desert.jpg";

//        imageView = (ImageView) findViewById(R.id.imageView);
//        Picasso.with(getBaseContext()).load("https://192.168.65.80:8080/uploads/hot_dog.png").into(imageView);
//        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
//        imageLoader.displayImage(url, imageView);

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




//        btnSetting = (Button)findViewById(R.id.btnSetting);
//        btnSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doubleCK++;
//
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(doubleCK == 1){
//                            Toast.makeText(MainActivity.this,"Single  Click",Toast.LENGTH_SHORT).show();
//                        }else if(doubleCK == 2){
//                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
//                        }
//                        doubleCK = 0;
//                    }
//                },500);
//
//            }
//        });
    }

    private void setupSettingBtn(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View setView) {
                doubleCK++;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(doubleCK == 2){
                            Snackbar.make(setView, "Go to setting page?", Snackbar.LENGTH_LONG)
                                    .setAction("Confirm", new View.OnClickListener(){
                                        @Override
                                        public void onClick(View view) {
                                            Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                                            startActivityForResult(settingIntent, SETTING_REQUEST);
                                        }
                                    }).show();

                        }
                        doubleCK = 0;

                    }
                },500);
            }
        });
    }

    private void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
        //cardGV.setBackgroundColor(Color.YELLOW);
        cardGV.setVerticalSpacing(20);
        cardGV.setHorizontalSpacing(20);
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
                            nearestMajor = String.valueOf(nearestBeacon.getMajor());
                            nearestMinor = String.valueOf(nearestBeacon.getMinor());
                            nearestMeasuredPower = String.valueOf(nearestBeacon.getMeasuredPower());
                            nearestRssi = String.valueOf(nearestBeacon.getRssi());


                        }

                        requestLocationCards postLoc = (requestLocationCards) new requestLocationCards();
                        postLoc.execute(api_loc, nearestMajor);

                        Log.e(TAG, " " + nearestMacAddress + " " + nearestMajor + ":" + nearestMinor + " " + nearestMeasuredPower + " " + nearestRssi);

                        if (nearestMajor != null) {
                            for (int position = 0; position < beaconSize; position++) {
                                iBeacon currentiBeacon = ibeacons.get(position);
                                if (nearestMajor.equals(currentiBeacon.getMajor())) {
                                    locationScore[position]++;
                                } else {
                                    locationScore[position] = 0;
                                }
                            }
                        }
                    }
                });

//                for (int position = 0; position < beaconSize; position++) {
//                    if (locationScore[position] >= MAX_VALUE) {
//                        location = ibeacons.get(position).getLocationID();
//                    }
//                }
//
//                if (location != Card.LOCATION_NULL) {
//                    setupCards();
//                }
            }
        });
    }

    private void setupSize() {
//        cardGV.setNumColumns(colNum);
        cardGV.setNumColumns(mySetting.getCardSize());
        Display display = getWindowManager().getDefaultDisplay();
        height = display.getHeight();
//        cardHeight = height * 3 / 2 / colNum;
        cardHeight = height * 3 / 2 / mySetting.getCardSize();


        param = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                cardHeight);

    }

    @Override
    public void onInit(int i) {

    }


    private class CardListAdapter extends ArrayAdapter<Card> {
        int location;



        public CardListAdapter(int location) {
            super(MainActivity.this, R.layout.card_item, currentCards);
            this.location = location;
        }
        public class ViewHolder{
            ImageView IV;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View itemView = convertView;
//             if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.card_item, parent, false);
                 holder = new ViewHolder();
                 holder.IV = (ImageView)itemView.findViewById(R.id.card_image);
                 itemView.setTag(holder);
                //itemView.setBackgroundColor(Color.YELLOW);
//             }else{
                 holder = (ViewHolder)itemView.getTag();
//             }

            Card currentCard = currentCards.get(position);
            itemView.setLayoutParams(param);

            Log.e("isPhoto()", String.valueOf(currentCard.isPhoto()));

//            ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

            if (currentCard.isPhoto()) {
                Bitmap bitmap = decodeSampledBitmapFromFile(currentCard.getImagePath(), 1000, 700);
                ((ImageView) itemView.findViewById(R.id.card_image)).setImageBitmap(bitmap);
            } else if ((currentCard.getImagePath().substring(0, 4)).equals("http")) {
                ImageLoader.getInstance().displayImage(currentCard.getImagePath(), ((ImageView) itemView.findViewById(R.id.card_image))); // Incoming options will be used
//                imageLoader.displayImage(currentCard.getImagePath(), ((ImageView) itemView.findViewById(R.id.card_image)));
            } else {
                int resId = getResources().getIdentifier(currentCard.getImagePath(), "drawable", getPackageName());
                ((ImageView) itemView.findViewById(R.id.card_image)).setImageResource(resId);
            }

            return itemView;
        }

//        @Override public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            View itemView = convertView;
//             if (itemView == null) {
//                itemView = getLayoutInflater().inflate(R.layout.card_item, parent, false);
//                 holder = new ViewHolder();
//                 holder.IV = (ImageView)itemView.findViewById(R.id.card_image);
//                 itemView.setTag(holder);
//                //itemView.setBackgroundColor(Color.YELLOW);
//             }else{
//                 holder = (ViewHolder)itemView.getTag();
//             }
//
//            Card currentCard = currentCards.get(position);
//            itemView.setLayoutParams(param);
//
//            Log.e("isPhoto()", String.valueOf(currentCard.isPhoto()));
//
//            if (currentCard.isPhoto()) {
////                Bitmap bitmap = decodeSampledBitmapFromFile(currentCard.getImagePath(), 1000, 700);
////                ((ImageView) itemView.findViewById(R.id.card_image)).setImageBitmap(bitmap);
//
//                Picasso.with(MainActivity.this).load(currentCard.getImagePath()).into(((ImageView) itemView.findViewById(R.id.card_image)));
//
//            } else if ((currentCard.getImagePath().substring(0, 4)).equals("http")) {
////                ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
////                imageLoader.displayImage(currentCard.getImagePath(), ((ImageView) itemView.findViewById(R.id.card_image)));
//                Log.e("path", currentCard.getImagePath());
//                Picasso.with(MainActivity.this).load(currentCard.getImagePath()).into(((ImageView) itemView.findViewById(R.id.card_image)));
//
//            } else {
//                int resId = getResources().getIdentifier(currentCard.getImagePath(), "drawable", getPackageName());
////                ((ImageView) itemView.findViewById(R.id.card_image)).setImageResource(resId);
//
//                Picasso.with(MainActivity.this).load(resId).into(((ImageView) itemView.findViewById(R.id.card_image)));
//            }
//
//            return itemView;
//        }
    }

    private void setupCards(int location) {
        if (tempLocation != location) {

            currentCards.clear();

            if(mySetting.camera()){
                currentCards.add(new Card(location, -1, "相機", "camera_icon", "/uploads/test.mp3", "camera icon", false));
            }

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
//        location = Card.LOCATION_NULL;
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
//                Log.e("Test", "LEFT");
//                if (fingers >= 2) {
//                    if (colNum >= 2) {
//                        colNum = colNum - 1;
//                        setupSize();
//                        setupCards();
//                    }
//
//                }
                return false;
            }

            @Override
            public boolean onSwipeRight(int fingers, long gestureDuration, double gestureDistance) {
//                Log.e("Test", "RIGHT");
//                if (fingers >= 2) {
//                    if (colNum <= 8) {
//                        colNum = colNum + 1;
//                        setupSize();
//                        setupCards();
//                    }
//
//                }
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
        //background.setOnTouchListener(mySfg);
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

    private void setupListener() {
        cardGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mySetting.camera() && position == 0) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = getFile();
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    CAMERA_REQUEST = 9000 + tempLocation;
                    requestCardClick cardClick = (requestCardClick) new requestCardClick();
                    cardClick.execute("stat/", String.valueOf(mySetting.getUID()), "/", String.valueOf(currentCards.get(position).getId()), "/", String.valueOf(location));
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                } else {
                    if (currentCards.get(position).isPhoto()) {
                        //TODO log photo card click to server
                        //...
                        Intent intent = new Intent(MainActivity.this, ShowPhotoActivity.class);
                        intent.putExtra("path", currentCards.get(position).getImagePath());
                        startActivity(intent);
                    } else {
                        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(100);
                        makeTextAndShow(getApplicationContext(), "你選擇的是 " + currentCards.get(position).getName(), Toast.LENGTH_SHORT);
                        if(!textToSpeech.isSpeaking()){
                            HashMap<String,String> stringStringHashMap = new HashMap<String,String>();
                            textToSpeech.speak(currentCards.get(position).getName(), TextToSpeech.QUEUE_ADD, stringStringHashMap);
                        }else{
                            textToSpeech.stop();
                        }

                        requestCardClick cardClick = (requestCardClick) new requestCardClick();
                        cardClick.execute("stat/", String.valueOf(mySetting.getUID()), "/", String.valueOf(currentCards.get(position).getId()), "/", String.valueOf(location));
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        beaconManager.disconnect();
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech=null;
        }
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
        } else if(requestCode == 8000){
            if (resultCode == Activity.RESULT_OK) {
                setupSize();
                setupCards(-1);
            }
        } else if (requestCode >= 9000) {
            if (resultCode == Activity.RESULT_OK) {
                int size = cards.size() + 1;

                String filename = String.valueOf(size) + ".jpg";
                String path = "sdcard/ie_project/" + filename;
                int photoLocation = requestCode - 9000;
                Log.e("photoLocation", String.valueOf(photoLocation));
                cards.add(new Card(photoLocation, size, "", path, "/uploads/test.mp3", "", true));
                setupCards(-1);
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

    private void connectToInternet() {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);;
        wifi.setWifiEnabled(true);
    }

    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            //Toast.makeText(this, " WIFI Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this, " Connecting WIFI ", Toast.LENGTH_LONG).show();
            connectToInternet();
            return false;
        }
        return false;
    }

    private class requestLocationCards extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            final StringBuilder responseOutput = new StringBuilder();
            try {
//                final TextView outputView = (TextView) findViewById(output);

//                URL url = new URL("https://192.168.65.99:8080/app/api/loc/B9407F30-F5F8-466E-AFF9-25556B57FE6D/1234/1234");
//                URL url = new URL("https://192.168.65.99:8080/app/api/pic/count");
                String tmp = "https://192.168.65.99:8080/app/api/";
                for (String p: params) {
                    tmp += p;
                }
                Log.e("tmp", tmp);
//                URL url = new URL("https://192.168.65.99:8080/app/api/" + params[0]);
                URL url = new URL(tmp);

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                String urlParameters = "";
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
//                response = String.valueOf(responseOutput);
//                Log.d("xxxxx", String.valueOf(output));

                MainActivity.this.runOnUiThread(new Runnable() {


                    @Override
                    public void run() {
//                        outputView.setText(output);

                    }
                });
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return responseOutput.toString();
        }

        public void onPostExecute(String result) {
            response = result;
//            Log.e("Response", response);
            try {
                JSONObject jsonObj = new JSONObject(response);
//                System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(response)));

//                String loudScreaming = jsonObj.getJSONArray("Location");
//                Log.e("-----JSON result", String.valueOf(josnArr));
//                JSONArray josnArr  = jsonObj.getJSONArray("Picture");
                JSONArray josnArr  = jsonObj.getJSONArray("Location");
                cards.clear();
                for (int i = 0; i < josnArr.length(); i++) {
                    JSONObject picture = josnArr.getJSONObject(i);
                    int lid = picture.getInt("lid");
                    int pid = picture.getInt("pid");
                    String pname = picture.getString("pname");
                    String imgurl = "https://192.168.65.99:8080" + picture.getString("imgurl");
                    //                    Log.d("IMAGE URL", imgurl);
                    String soundurl = picture.getString("soundurl");
                    String description = picture.getString("description");
                    cards.add(new Card(lid, pid, pname, imgurl, soundurl, description, false));
                    location = lid;
                }
                setupCards(location);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class requestCardClick extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            final StringBuilder responseOutput = new StringBuilder();
            try {
                String tmp = "https://192.168.65.99:8080/app/api/";
                for (String p: params) {
                    tmp += p;
                }
                Log.e("tmp", tmp);
//                URL url = new URL("https://192.168.65.99:8080/app/api/" + params[0]);
                URL url = new URL(tmp);

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                String urlParameters = "";
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
//                response = String.valueOf(responseOutput);
//                Log.d("xxxxx", String.valueOf(output));

//                MainActivity.this.runOnUiThread(new Runnable() {
//
//
//                    @Override
//                    public void run() {
////                        outputView.setText(output);
//
//                    }
//                });
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return responseOutput.toString();
        }

        public void onPostExecute(String result) {
            response = result;
            try {
                JSONObject jsonObj = new JSONObject(response);
                String status  = jsonObj.getString("Update Sucess!");
//                if(status.equals("Update Sucess!")){
//                    //Toast.makeText(SettingActivity.this, "Timer Start", Toast.LENGTH_LONG).show();
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}

