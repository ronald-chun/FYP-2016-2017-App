package com.cuhk.ieproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Felix on 3/7/2017.
 */

public class ShowPhotoActivity extends AppCompatActivity {
    String path;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            path = bundle.getString("path");
        }
        setContentView(R.layout.activity_photo);
        imageView = (ImageView) findViewById(R.id.photo_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPhotoActivity.this.finish();
            }
        });
        Bitmap bitmap = decodeSampledBitmapFromFile(path, 1000, 700);
        imageView.setImageBitmap(bitmap);
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
}
