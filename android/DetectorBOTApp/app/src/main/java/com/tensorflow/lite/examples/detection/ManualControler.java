package com.tensorflow.lite.examples.detection;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.examples.detection.R;

import java.io.IOException;

public class ManualControler extends AppCompatActivity implements BluetoothFragment.OnFragmentInteractionListener{





    private RelativeLayout view;
    private ViewGroup.LayoutParams manLayoutPar;

    private String connectionMsg = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_manual_controler);


        view = findViewById(R.id.man_layout);
        manLayoutPar = view.getLayoutParams();




/*

        servo1Up.setOnTouchListener(new View.OnTouchListener() {

           private Handler mHandler;
            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        if(servo1Value < servo1Max){
                            mHandler = new Handler();
                            mHandler.postDelayed(mAction, 5);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    servo1Value ++;
                    tvServo1Bar.setText(String.valueOf(servo1Value));
                    servo1Bar.setProgress(servo1Value);
                    vibe.vibrate(2);
                    try {
                        onFragmentInteraction("p1u#");
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                    }
                    mHandler.postDelayed(this, 5);
                }
            };
        });

        servo1Down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(servo1Value > 0){
                            servo1Value --;
                            tvServo1Bar.setText(String.valueOf(servo1Value));
                            servo1Bar.setProgress(servo1Value);
                            try {
                                onFragmentInteraction("p1d#");
                                vibe.vibrate(25);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        vibe.vibrate(25);
                        return true;
                }
                return false;
            }
        });

        servo2Up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(servo2Value < servo2Max){
                            servo2Value ++;
                            tvServo2.setText(String.valueOf(servo2Value));
                            try {
                                onFragmentInteraction("p2u#");
                                vibe.vibrate(25);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        vibe.vibrate(25);
                        return true;
                }
                return false;
            }
        });

        servo2Down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(servo2Value > 0){
                            servo2Value --;
                            tvServo2.setText(String.valueOf(servo2Value));
                            try {
                                onFragmentInteraction("p2d#");
                                vibe.vibrate(25);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        vibe.vibrate(25);
                        return true;
                }
                return false;
            }
        });

        servo3Up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(servo3Value < servo3Max){
                            servo3Value ++;
                            tvServo3.setText(String.valueOf(servo3Value));
                            try {
                                onFragmentInteraction("p3u#");
                                vibe.vibrate(25);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        vibe.vibrate(25);
                        return true;
                }
                return false;
            }
        });

        servo3Down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(servo3Value > 0){
                            servo3Value --;
                            tvServo3.setText(String.valueOf(servo3Value));
                            try {
                                onFragmentInteraction("p3d#");
                                vibe.vibrate(25);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        vibe.vibrate(25);
                        return true;
                }
                return false;
            }
        });

        servo4Up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(servo4Value < servo4Max){
                            servo4Value ++;
                            tvServo4Bar.setText(String.valueOf(servo4Value));
                            servo4Bar.setProgress(servo4Value);
                            try {
                                onFragmentInteraction("p4u#");
                                vibe.vibrate(25);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        vibe.vibrate(25);
                        return true;
                }
                return false;
            }
        });

        servo4Down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(servo4Value > 0){
                            servo4Value --;
                            tvServo4Bar.setText(String.valueOf(servo4Value));
                            servo4Bar.setProgress(servo4Value);
                            try {
                                onFragmentInteraction("p4d#");
                                vibe.vibrate(25);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        vibe.vibrate(25);
                        return true;
                }
                return false;
            }
        });

        servoxUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(servoxValue < servoxMax){
                            servoxValue ++;
                            tvServoxBar.setText(String.valueOf(servoxValue));
                            servoxBar.setProgress(servoxValue);
                            try {
                                onFragmentInteraction("pxu#");
                                vibe.vibrate(25);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        vibe.vibrate(25);
                        return true;
                }
                return false;
            }
        });

        servoxDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(servoxValue > 0){
                            servoxValue --;
                            tvServoxBar.setText(String.valueOf(servoxValue));
                            servoxBar.setProgress(servoxValue);
                            try {
                                onFragmentInteraction("pxd#");
                                vibe.vibrate(25);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        vibe.vibrate(25);
                        return true;
                }
                return false;
            }
        });



        servo1Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvServo1Bar.setText(String.valueOf(progress));
                servo1Value = progress;
                try{
                    onFragmentInteraction("p1$"+servo1Value+"#");
                }catch (Exception e){}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                vibe.vibrate(10);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vibe.vibrate(10);
            }
        });

        servo4Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvServo4Bar.setText(String.valueOf(progress));
                servo4Value = progress;
                try{
                    onFragmentInteraction("p4$"+servo4Value+"#");
                }catch (Exception e){}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                vibe.vibrate(10);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vibe.vibrate(10);
            }
        });

        servoxBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvServoxBar.setText(String.valueOf(progress));
                servoxValue = progress;
                servo2Value = progress;
                servo3Value = 200 - progress;
                tvServo2.setText(servo2Value+"");
                tvServo3.setText(servo3Value+"");
                try{
                    onFragmentInteraction("p5$"+servoxValue+"#");
                }catch (Exception e){}
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                vibe.vibrate(10);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vibe.vibrate(10);
            }
        });
*/

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();

        if (hasFocus) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    @Override
    public void onFragmentInteraction(String msg) {
        BluetoothFragment fragment1 = (BluetoothFragment) getSupportFragmentManager().findFragmentByTag("bluetoothFragment");
        fragment1.sendMsg(msg);
    }

}
