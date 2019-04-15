package com.tensorflow.lite.examples.detection;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.tensorflow.lite.examples.detection.R;

public class MainActivity extends AppCompatActivity {

    ImageButton btnAuto, btnMan;
    private Vibrator vibe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAuto = findViewById(R.id.ibtn_auto);
        btnMan = findViewById(R.id.ibtn_man);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        btnAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , DetectorActivity.class);
                startActivity(i);
                vibe.vibrate(25);
                Toast.makeText(getApplicationContext() , "Automatic mode has been started" , Toast.LENGTH_SHORT).show();
            }
        });
        btnMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , ManualControler.class);
                startActivity(i);
                vibe.vibrate(25);
                Toast.makeText(getApplicationContext() , "Manual mode has been started" , Toast.LENGTH_SHORT).show();
            }
        });
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
}
