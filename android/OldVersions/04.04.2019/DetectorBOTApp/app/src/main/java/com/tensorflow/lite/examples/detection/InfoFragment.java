package com.tensorflow.lite.examples.detection;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tensorflow.lite.examples.detection.tflite.TFLiteObjectDetectionAPIModel;

import org.tensorflow.lite.examples.detection.R;

public class InfoFragment extends Fragment {




    public InfoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        return view;
    }


}
