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
import android.widget.ImageView;

import java.io.File;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_view, container, false);

        ImageView img_view = (ImageView) view.findViewById(R.id.eye_view);

        SharedPreferences spf = getActivity().getPreferences(Context.MODE_PRIVATE);

        String time_filename= spf.getString("File_Name", "");


        File mFile = new File(getActivity().getExternalFilesDir(null), time_filename+".jpg");            // 3K X 4K

        Log.d("Path", ""+mFile.getAbsolutePath());

        String path = "/storage/emulated/0/Android/data/com.alleviate.eyescan/files/1491020474164.jpg";

                /*

                getFragmentManager().beginTransaction()
                            .replace(R.id.container, ViewFragment.newInstance())
                            .commit();
                 */

        Bitmap bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());
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
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        // Apply the scaled bitmap
        img_view.setImageBitmap(scaledBitmap);

        return view;
    }

}
