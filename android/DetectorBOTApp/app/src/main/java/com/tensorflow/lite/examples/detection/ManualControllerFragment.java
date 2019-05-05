package com.tensorflow.lite.examples.detection;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.examples.detection.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManualControllerFragment.OnFragmentSendListener} interface
 * to handle interaction events.
 * Use the {@link ManualControllerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManualControllerFragment extends Fragment implements BluetoothFragment.OnFragmentInteractionListener{


    private final String bluetoothFragmentTag = "android:switcher:" + R.id.toolbar_tabs_pager + ":" + 2;
    private static final int servo1Max = 225 , servo2Max = 200 , servo3Max = 200 , servo4Max = 250 , servoxMax = 200 ,servo1ValueR = 157 , servo2ValueR = 0 , servo3ValueR = 187 , servo4ValueR = 10 , servoxValueR=0 ;

    private int speedProgress=3 , angelProgress=2 , servo1Value = 157 , servo2Value = 0 , servo3Value = 187 , servo4Value = 10 , servoxValue=0;
    private ImageButton servo1Up,servo1Down,servo3Up,servo3Down,servo2Up,servo2Down,servo4Up,servo4Down,servoxUp,servoxDown;
    private SeekBar speedBar , servo1Bar , servo4Bar , servoxBar , angelBar;

    private TextView tvSpeedProgress, tvServo1Bar , tvServo4Bar , tvServoxBar , tvServo3 , tvServo2 , tvConnection , tvAngelBar;
    private ImageButton left, right, up, down, leftUp, rightUp, leftDown, rightDown, centerButton , btnMagnet;

    private boolean magnetFlag = false;
    private Vibrator vibe;












    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentSendListener mListener;

    public ManualControllerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManualControllerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManualControllerFragment newInstance(String param1, String param2) {
        ManualControllerFragment fragment = new ManualControllerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //onFragmentInteraction("c#");

        View view =  inflater.inflate(R.layout.fragment_manual_controller, container, false);

        btnMagnet =view.findViewById(R.id.btn_magnet);
        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        left = view.findViewById(R.id.left_btn);
        right = view.findViewById(R.id.right_btn);
        up = view.findViewById(R.id.up_btn);
        down = view.findViewById(R.id.down_btn);
        leftUp = view.findViewById(R.id.up_left_btn);
        leftDown = view.findViewById(R.id.down_left_btn);
        rightDown = view.findViewById(R.id.down_right_btn);
        rightUp = view.findViewById(R.id.up_right_btn);
        centerButton = view.findViewById(R.id.circle_btn);

        tvSpeedProgress = view.findViewById(R.id.tv_progress_speed);
        tvServo1Bar = view.findViewById(R.id.tv_servo1);
        tvServo4Bar = view.findViewById(R.id.tv_servo4);
        tvServo2 = view.findViewById(R.id.tv_servo2);
        tvServo3 = view.findViewById(R.id.tv_servo3);
        tvServoxBar = view.findViewById(R.id.tv_servox);
        tvAngelBar = view.findViewById(R.id.tv_progress_angel);

        speedBar = view.findViewById(R.id.speed_bar);
        speedBar.setProgress(speedProgress);
        tvSpeedProgress.setText(speedProgress + " Km/s");

        angelBar = view.findViewById(R.id.angel_bar);
        angelBar.setProgress(angelProgress);
        tvAngelBar.setText(angelProgress + " %");

        tvServo2.setText(servo2Value+"");
        tvServo3.setText(servo3Value+"");

        servo1Up   = view.findViewById(R.id.servo1_btn_plus);
        servo1Down = view.findViewById(R.id.servo1_btn_sub);
        servo2Up   = view.findViewById(R.id.servo2_btn_plus);
        servo2Down = view.findViewById(R.id.servo2_btn_sub);
        servo3Up   = view.findViewById(R.id.servo3_btn_plus);
        servo3Down = view.findViewById(R.id.servo3_btn_sub);
        servo4Up   = view.findViewById(R.id.servo4_btn_plus);
        servo4Down = view.findViewById(R.id.servo4_btn_sub);
        servoxUp   = view.findViewById(R.id.servox_btn_plus);
        servoxDown = view.findViewById(R.id.servox_btn_sub);

        servo1Bar  = view.findViewById(R.id.servo1_seek_bar);
        servo1Bar.setMax(servo1Max);
        servo1Bar.setProgress(servo1Value);
        tvServo1Bar.setText(String.valueOf(servo1Value));
        servo4Bar  = view.findViewById(R.id.servo4_seek_bar);
        servo4Bar.setMax(servo4Max);
        servo4Bar.setProgress(servo4Value);
        tvServo4Bar.setText(String.valueOf(servo4Value));
        servoxBar  = view.findViewById(R.id.servox_seek_bar);
        servoxBar.setMax(servoxMax);
        servoxBar.setProgress(servoxValue);
        tvServoxBar.setText(String.valueOf(servoxValue));

        servo1Up   = view.findViewById(R.id.servo1_btn_plus);
        servo1Down = view.findViewById(R.id.servo1_btn_sub);
        servo2Up   = view.findViewById(R.id.servo2_btn_plus);
        servo2Down = view.findViewById(R.id.servo2_btn_sub);
        servo3Up   = view.findViewById(R.id.servo3_btn_plus);
        servo3Down = view.findViewById(R.id.servo3_btn_sub);
        servo4Up   = view.findViewById(R.id.servo4_btn_plus);
        servo4Down = view.findViewById(R.id.servo4_btn_sub);
        servoxUp   = view.findViewById(R.id.servox_btn_plus);
        servoxDown = view.findViewById(R.id.servox_btn_sub);


        btnMagnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magnetTurn();
            }
        });

        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetApp();
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            onFragmentInteraction("l#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            onFragmentInteraction("r#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        });

        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            onFragmentInteraction("f#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        });

        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            onFragmentInteraction("b#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        });

        leftDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            onFragmentInteraction("lb#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        });

        leftUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            onFragmentInteraction("lf#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        });

        rightDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            onFragmentInteraction("rb#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        });

        rightUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            onFragmentInteraction("rf#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        });

        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedProgress.setText(progress +" Km/s");
                speedProgress = progress;
                if (progress == 10){
                    try {
                        onFragmentInteraction("x#");
                        vibe.vibrate(25);
                    }catch (Exception e){
                        Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    try {
                        onFragmentInteraction(progress+"#");
                        vibe.vibrate(25);
                    }catch (Exception e){
                        Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        angelBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAngelBar.setText(progress +" %");
                angelProgress = progress;
                try {
                    onFragmentInteraction("a"+progress+"#");
                    vibe.vibrate(25);
                }catch (Exception e){
                    Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    private void magnetTurn() {
        if (magnetFlag){
            btnMagnet.setImageResource(R.drawable.magnet_off);
            magnetFlag = false;
        }else{
            btnMagnet.setImageResource(R.drawable.magnet_on);
            magnetFlag = true;
        }
        try {
            onFragmentInteraction("mg#");
            vibe.vibrate(25);
        }catch (Exception e){
            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
        }
    }

    private void resetApp() {
        servo1Value = servo1ValueR;
        servo2Value = servo2ValueR;
        servo3Value = servo3ValueR;
        servo4Value = servo4ValueR;
        servoxValue = servoxValueR;

        servo1Bar.setProgress(servo1Value);
        servo4Bar.setProgress(servo4Value);
        servoxBar.setProgress(servoxValue);

        tvServo1Bar.setText(servo1Value+"");
        tvServo4Bar.setText(servo4Value+"");
        tvServoxBar.setText(servoxValue+"");

        tvServo2.setText(servo2Value+"");
        tvServo3.setText(servo3Value+"");
        try {
            onFragmentInteraction("c#");
            vibe.vibrate(25);
        }catch (Exception e){
            Toast.makeText(getContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String msg) {
        if (mListener != null) {
            mListener.onFragmentSend(msg);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSendListener) {
            mListener = (OnFragmentSendListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentInteraction(String msg) {
        BluetoothFragment btFragment = (BluetoothFragment) getActivity().getSupportFragmentManager().findFragmentByTag(bluetoothFragmentTag);
        btFragment.sendMsg(msg);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentSendListener {
        // TODO: Update argument type and name
        void onFragmentSend(String msg);
    }
}
