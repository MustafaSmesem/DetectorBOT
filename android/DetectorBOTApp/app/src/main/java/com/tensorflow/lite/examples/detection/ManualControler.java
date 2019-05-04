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

    private BluetoothAdapter myBT;
    private ImageButton btnBT , btnMagnet;
    private ImageButton left, right, up, down, leftUp, rightUp, leftDown, rightDown, centerButton;
    private ImageButton servo1Up,servo1Down,servo3Up,servo3Down,servo2Up,servo2Down,servo4Up,servo4Down,servoxUp,servoxDown;
    private SeekBar speedBar , servo1Bar , servo4Bar , servoxBar , angelBar;
    private int speedProgress=3 , angelProgress=2 , servo1Value = 157 , servo2Value = 0 , servo3Value = 187 , servo4Value = 10 , servoxValue=0;
    private TextView tvSpeedProgress, tvServo1Bar , tvServo4Bar , tvServoxBar , tvServo3 , tvServo2 , tvConnection , tvAngelBar;
    private static final int servo1Max = 225 , servo2Max = 200 , servo3Max = 200 , servo4Max = 250 , servoxMax = 200 ,servo1ValueR = 157 , servo2ValueR = 0 , servo3ValueR = 187 , servo4ValueR = 10 , servoxValueR=0 ;
    private RelativeLayout view;
    private ViewGroup.LayoutParams manLayoutPar;
    private boolean btFlag = true , magnetFlag = false;
    private String connectionMsg = "";
    private Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_manual_controler);

        myBT = BluetoothAdapter.getDefaultAdapter();
        btnBT = findViewById(R.id.btn_bluetooth_man);
        view = findViewById(R.id.man_layout);
        manLayoutPar = view.getLayoutParams();
        tvConnection = findViewById(R.id.tv_bt_connection);
        btnMagnet =findViewById(R.id.btn_magnet);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        left = findViewById(R.id.left_btn);
        right = findViewById(R.id.right_btn);
        up = findViewById(R.id.up_btn);
        down = findViewById(R.id.down_btn);
        leftUp = findViewById(R.id.up_left_btn);
        leftDown = findViewById(R.id.down_left_btn);
        rightDown = findViewById(R.id.down_right_btn);
        rightUp = findViewById(R.id.up_right_btn);
        centerButton = findViewById(R.id.circle_btn);

        tvSpeedProgress = findViewById(R.id.tv_progress_speed);
        tvServo1Bar = findViewById(R.id.tv_servo1);
        tvServo4Bar = findViewById(R.id.tv_servo4);
        tvServo2 = findViewById(R.id.tv_servo2_value);
        tvServo3 = findViewById(R.id.tv_servo3_value);
        tvServoxBar = findViewById(R.id.tv_servox);
        tvAngelBar = findViewById(R.id.tv_progress_angel);

        speedBar = findViewById(R.id.speed_bar);
        speedBar.setProgress(speedProgress);
        tvSpeedProgress.setText(speedProgress + " Km/s");

        angelBar = findViewById(R.id.angel_bar);
        angelBar.setProgress(angelProgress);
        tvAngelBar.setText(angelProgress + " %");

        tvServo2.setText(servo2Value+"");
        tvServo3.setText(servo3Value+"");

        servo1Up = findViewById(R.id.servo1_btn_plus);
        servo1Down = findViewById(R.id.servo1_btn_sub);
        servo2Up = findViewById(R.id.servo2_btn_plus);
        servo2Down = findViewById(R.id.servo2_btn_sub);
        servo3Up = findViewById(R.id.servo3_btn_plus);
        servo3Down = findViewById(R.id.servo3_btn_sub);
        servo4Up = findViewById(R.id.servo4_btn_plus);
        servo4Down = findViewById(R.id.servo4_btn_sub);
        servoxUp = findViewById(R.id.servox_btn_plus);
        servoxDown = findViewById(R.id.servox_btn_sub);

        servo1Bar = findViewById(R.id.servo1_seek_bar);
        servo1Bar.setMax(servo1Max);
        servo1Bar.setProgress(servo1Value);
        tvServo1Bar.setText(String.valueOf(servo1Value));
        servo4Bar = findViewById(R.id.servo4_seek_bar);
        servo4Bar.setMax(servo4Max);
        servo4Bar.setProgress(servo4Value);
        tvServo4Bar.setText(String.valueOf(servo4Value));
        servoxBar = findViewById(R.id.servox_seek_bar);
        servoxBar.setMax(servoxMax);
        servoxBar.setProgress(servoxValue);
        tvServoxBar.setText(String.valueOf(servoxValue));

        servo1Up = findViewById(R.id.servo1_btn_plus);
        servo1Down = findViewById(R.id.servo1_btn_sub);
        servo2Up = findViewById(R.id.servo2_btn_plus);
        servo2Down = findViewById(R.id.servo2_btn_sub);
        servo3Up = findViewById(R.id.servo3_btn_plus);
        servo3Down = findViewById(R.id.servo3_btn_sub);
        servo4Up = findViewById(R.id.servo4_btn_plus);
        servo4Down = findViewById(R.id.servo4_btn_sub);
        servoxUp = findViewById(R.id.servox_btn_plus);
        servoxDown = findViewById(R.id.servox_btn_sub);


        btnMagnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (magnetFlag){
                    btnMagnet.setImageResource(R.drawable.magnet_off);
                    magnetFlag = false;
                }else{
                    btnMagnet.setImageResource(R.drawable.magnet_on);
                    magnetFlag = true;
                }
                vibe.vibrate(25);
                try {
                    onFragmentInteraction("mg#");
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                }
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
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                            vibe.vibrate(25);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        try {
                            onFragmentInteraction("s#");
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                }
                return false;
            }
        });

        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        servo1Up.setOnTouchListener(new View.OnTouchListener() {
           /*
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        while(){
                            if(event.getAction() == MotionEvent.ACTION_UP){
                                vibe.vibrate(25);
                                return true;
                            }
                            servo1Value ++;
                            tvServo1Bar.setText(String.valueOf(servo1Value));
                            servo1Bar.setProgress(servo1Value);
                            try {
                                onFragmentInteraction("p1u#");
                                vibe.vibrate(2);
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        return true;
                }
                return false;
            }*/
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
                        Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    try {
                        onFragmentInteraction(progress+"#");
                        vibe.vibrate(25);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(),"Error: check bluetooth connection.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

        btnBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothStatusCheck();
                vibe.vibrate(25);
                if(btFlag){
                    manLayoutPar.width = 1450;
                    btFlag = false;
                    BluetoothFragment fragment1 = new BluetoothFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container_bt , fragment1 , "bluetoothFragment").commit();
                }else{
                    manLayoutPar.width = 0;
                    btFlag = true;
                }
                tvConnection.setText(connectionMsg);
            }
        });
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        bluetoothStatusCheck();
    }

    public void bluetoothStatusCheck(){
        if(myBT.isEnabled())
            btnBT.setImageResource(R.drawable.bluetooth_on);
        else
            btnBT.setImageResource(R.drawable.bluetooth_off);
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
