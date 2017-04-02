package com.alleviate.eyescan;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class PreviewActivity extends AppCompatActivity {

    Bitmap bitmap;
    final int CROP_PIC_REQUEST_CODE = 1;
    ImageView img_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        img_view = (ImageView) findViewById(R.id.eye_view);
        Button scan = (Button) findViewById(R.id.button_scan);
        Button crop = (Button) findViewById(R.id.button_crop);

        SharedPreferences spf = getSharedPreferences("Iris_Path", Context.MODE_PRIVATE);

        String time_filename = spf.getString("File_Name", "");


        //File mFile = new File(getApplicationContext().getExternalFilesDir(null), time_filename);            // 3K X 4K

        Log.d("Path", ""+time_filename);

        bitmap = BitmapFactory.decodeFile(time_filename);

        Bitmap scaledBitmap = scale_bitmap(bitmap);
        // Apply the scaled bitmap
        img_view.setImageBitmap(scaledBitmap);

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri img_uri = getImageUri(getApplicationContext(), bitmap);
                doCrop(img_uri);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getResizedBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                Toast.makeText(getApplicationContext(),"File Compressed...",Toast.LENGTH_SHORT).show();

                Intent in = new Intent(getApplicationContext(), RequestActivity.class);
                startActivity(in);

                finish();
            }
        });

    }

    private Bitmap scale_bitmap(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) metrics.widthPixels) / width;
        float yScale = ((float) metrics.heightPixels) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return scaledBitmap;
    }

    private void doCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_PIC_REQUEST_CODE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);


        File file = new File(getExternalFilesDir(null), "scan_img.jpg");

        if (file.exists()){

            file.delete();
        }

        try {

            FileOutputStream out = new FileOutputStream(file);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        bm.recycle();
        return resizedBitmap;
    }

    // who=null, request=1, result=0, data=Intent


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CROP_PIC_REQUEST_CODE) {
            if (data != null) {
                Bundle extras = data.getExtras();

                if (extras != null){

                    bitmap = extras.getParcelable("data");
                    img_view.setImageBitmap(scale_bitmap(bitmap));
                }

            }
        }

    }

}
