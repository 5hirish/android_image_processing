package com.alleviate.eyescan;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFragment extends Fragment {


    public ViewFragment() {
        // Required empty public constructor
    }

    public static ViewFragment newInstance() {

        Bundle args = new Bundle();

        ViewFragment fragment = new ViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Bitmap bitmap;
    ProgressBar upload_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_view, container, false);

        ImageView img_view = (ImageView) view.findViewById(R.id.eye_view);
        Button scan = (Button) view.findViewById(R.id.dummy_button);

        SharedPreferences spf = getActivity().getPreferences(Context.MODE_PRIVATE);

        String time_filename= spf.getString("File_Name", "");


        File mFile = new File(getActivity().getExternalFilesDir(null), time_filename+".jpg");            // 3K X 4K

        Log.d("Path", ""+mFile.getAbsolutePath());

        upload_bar = new ProgressBar(getActivity());
        upload_bar.setVisibility(View.INVISIBLE);

        bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


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
        final Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        // Apply the scaled bitmap
        img_view.setImageBitmap(scaledBitmap);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getResizedBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);

                Toast.makeText(getActivity(),"File Comperessed...",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
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


        File file = new File(getActivity().getExternalFilesDir(null), "scan_img.jpg");

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
}
