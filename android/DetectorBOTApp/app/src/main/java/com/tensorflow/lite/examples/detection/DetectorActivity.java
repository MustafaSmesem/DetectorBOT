/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tensorflow.lite.examples.detection;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.tensorflow.lite.examples.detection.customview.OverlayView;
import com.tensorflow.lite.examples.detection.env.BorderedText;
import com.tensorflow.lite.examples.detection.env.ImageUtils;
import com.tensorflow.lite.examples.detection.env.Logger;
import com.tensorflow.lite.examples.detection.tflite.Classifier;
import com.tensorflow.lite.examples.detection.tflite.TFLiteObjectDetectionAPIModel;
import com.tensorflow.lite.examples.detection.tracking.MultiBoxTracker;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.tensorflow.lite.examples.detection.R;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener {
  private static final Logger LOGGER = new Logger();

  // Configuration values for the prepackaged SSD model.
  private static final int TF_OD_API_INPUT_SIZE = 300;
  private static final boolean TF_OD_API_IS_QUANTIZED = false;
  private static final String TF_OD_API_MODEL_FILE = "detect.tflite";
  private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt";
  private static final DetectorMode MODE = DetectorMode.TF_OD_API;
  // Minimum detection confidence to track a detection.
  public float MINIMUM_CONFIDENCE_TF_OD_API = 0.90f;
  private static final boolean MAINTAIN_ASPECT = false;
  private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
  private static final boolean SAVE_PREVIEW_BITMAP = false;
  private static final float TEXT_SIZE_DIP = 10;
  OverlayView trackingOverlay;
  private Integer sensorOrientation;

  private Classifier detector;

  private boolean isSearchState = false;

  private float positionX = 0, positionY = 0;
  private boolean isLabel = false;
  private String detectedLabel="";
  private float score=0;
  private float[] pos = new float[4];
  DecimalFormat df = new DecimalFormat("##.##");
  String scoreS;

  //private long lastProcessingTimeMs;
  private Bitmap rgbFrameBitmap = null;
  private Bitmap croppedBitmap = null;
  private Bitmap cropCopyBitmap = null;

  private boolean computingDetection = false;

  private long timestamp = 0;

  private Matrix frameToCropTransform;
  private Matrix cropToFrameTransform;

  private MultiBoxTracker tracker;

  private byte[] luminanceCopy;

  private BorderedText borderedText;

  /*** Servos ***/

  private final int servo4Reset = 390;
  private final int servo4Max = 520;
  private final int servo4Min = 270;
  private int servo4Value = servo4Reset;

  private final int servo1Reset = 415;
  private final int servo1Max = 550;
  private final int servo1Min = 100;
  private int servo1Value = servo1Reset;

  /*** End Servos ***/

  private boolean isDetected = false;
  private int isDetectedCounter = 0;
  private int detectedDelay = 15;
  /*** SearchState Variables  ***/

    /***
     * searchTypeFlag define the search method{armSearch:true \\ motorSearch:false}
        ** Arm
          * searchArmType define the arm search movementDirection{armUp:'u' || armDown:'d' \\ armRight:'r' \\ armLeft:'l'}
        ** Motor
          * searchMotorFlag define the movementType{turn:true \\ straight:false}
     ***/

    private boolean searchTypeFlag = true;

    /*** Arm ***/
    private char searchArmType = 'u';
    private int servoWriteDelay = 20;

    private final int servo4SearchUp = 360;
    private final int servo4SearchDown = 400;
    private int servo4SearchSpeed = 5;

    private final int servo1SearchRight = 320;
    private final int servo1SearchLeft = 495;
    private int servo1SearchSpeed = 8;

    /*** End Arm ***/

    /*** Motors ***/
    private boolean searchMotorFlag = true;
    private boolean turnMotorFlag = true;
    private boolean straightMotorFlag = true;
    private int motorSpeed = 80;

    private int movementCounter = 0;
    private int turnAngle = 8;
    private int forwardDistance = 30;

    /*** End Motors ***/

    /*** Distance ***/

    /*** End Distance ***/
  /*** End SearchState Variables  ***/


  @Override
  public void onPreviewSizeChosen(final Size size, final int rotation) {

    final float textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    borderedText = new BorderedText(textSizePx);
    borderedText.setTypeface(Typeface.MONOSPACE);

    tracker = new MultiBoxTracker(this);

    int cropSize = TF_OD_API_INPUT_SIZE;

    try {
      detector =
          TFLiteObjectDetectionAPIModel.create(
              getAssets(),
              TF_OD_API_MODEL_FILE,
              TF_OD_API_LABELS_FILE,
              TF_OD_API_INPUT_SIZE,
              TF_OD_API_IS_QUANTIZED);
      cropSize = TF_OD_API_INPUT_SIZE;
    } catch (final IOException e) {
      e.printStackTrace();
      LOGGER.e("Exception initializing classifier!", e);
      Toast toast =
          Toast.makeText(
              getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
      toast.show();
      finish();
    }

    previewWidth = size.getWidth();
    previewHeight = size.getHeight();

    sensorOrientation = rotation - getScreenOrientation();
    LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

    LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
    rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
    croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

    frameToCropTransform =
        ImageUtils.getTransformationMatrix(
            previewWidth, previewHeight,
            cropSize, cropSize,
            sensorOrientation, MAINTAIN_ASPECT);

    cropToFrameTransform = new Matrix();
    frameToCropTransform.invert(cropToFrameTransform);

    trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
    trackingOverlay.addCallback(
        new OverlayView.DrawCallback() {
          @Override
          public void drawCallback(final Canvas canvas) {
            tracker.draw(canvas);
            if (isDebug()) {
              tracker.drawDebug(canvas);
            }
          }
        });
  }


  protected void resetApp() {
    try {
      onFragmentInteraction("R#");
      servo4Value = servo4Reset;
      servo1Value = servo1Reset;
      isDetected = false;
      isDetectedCounter =0;
      searchTypeFlag = true;
      searchArmType = 'u';
      searchMotorFlag = true;
      movementCounter = 0;
      motorStop();
    }catch (Exception e){      }

  }

  @Override
  protected void processImage() {
    ++timestamp;
    final long currTimestamp = timestamp;
    byte[] originalLuminance = getLuminance();
    tracker.onFrame(
        previewWidth,
        previewHeight,
        getLuminanceStride(),
        sensorOrientation,
        originalLuminance,
        timestamp);
    trackingOverlay.postInvalidate();

    // No mutex needed as this method is not reentrant.
    if (computingDetection) {
      readyForNextImage();
      return;
    }
    computingDetection = true;
    LOGGER.i("Preparing image " + currTimestamp + " for detection in bg thread.");

    rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

    if (luminanceCopy == null) {
      luminanceCopy = new byte[originalLuminance.length];
    }
    System.arraycopy(originalLuminance, 0, luminanceCopy, 0, originalLuminance.length);
    readyForNextImage();

    final Canvas canvas = new Canvas(croppedBitmap);
    canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
    // For examining the actual TF input.
    if (SAVE_PREVIEW_BITMAP) {
      ImageUtils.saveBitmap(croppedBitmap);
    }

    runInBackground(
        new Runnable() {
          @Override
          public void run() {
            if(isChanged){
              if (isAuto){
                try {
                  onFragmentInteraction("A#");
                  resetApp();
                }catch (Exception e){      }
              }else{
                try {
                  onFragmentInteraction("M#");
                }catch (Exception e){      }
              }
              isChanged = false;
            }
            LOGGER.i("Running detection on image " + currTimestamp);
            //final long startTime = SystemClock.uptimeMillis();
            final List<Classifier.Recognition> results = detector.recognizeImage(croppedBitmap);
            //lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

            cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
            final Canvas canvas = new Canvas(cropCopyBitmap);
            final Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(2.0f);

            float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
            switch (MODE) {
              case TF_OD_API:
                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                break;
            }

            final List<Classifier.Recognition> mappedRecognitions =
                new LinkedList<Classifier.Recognition>();

            for (final Classifier.Recognition result : results) {
              final RectF location = result.getLocation();
              if (location != null && result.getConfidence() >= minimumConfidence) {
                motorStop();
                isDetected = true;

                isDetectedCounter = 0;

                positionX = location.centerX();
                positionY = location.centerY();
                detectedLabel = result.getTitle();

                score = result.getConfidence();
                score *= 100;
                scoreS = df.format(score);

                /***/
                    pos[1] = location.left;
                    pos[0] = location.top;
                    pos[3] = location.right;
                    pos[2] = location.bottom;
                /***/

                canvas.drawPoint(positionX , positionY , paint);
                cropToFrameTransform.mapRect(location);
                result.setLocation(location);
                mappedRecognitions.add(result);

              }else{

                if (isDetectedCounter >= detectedDelay)
                  isDetected = false;

                if(isAuto && !isDetected) {
                  searchState();
                }else{
                  isDetectedCounter++;
                }
              }
            }

            tracker.trackResults(mappedRecognitions, luminanceCopy, currTimestamp);
            trackingOverlay.postInvalidate();

            computingDetection = false;

            runOnUiThread(
                new Runnable() {
                  @Override
                  public void run() {
                    /*
                      tv_positionX.setText(""+positionX);
                      tv_positionY.setText(""+positionY);
                      tv_detectedLabel.setText(""+detectedLabel);
                      tv_score.setText(scoreS+"%");
                      tv_object_center.setText("L: "+pos[1]+" ,R: "+pos[3]+" ,T: "+pos[0]+" ,B: "+pos[2]);
                      */
                  }
                });
          }
        });
  }


  private void searchState() {
    if(searchTypeFlag)
      armCheck();
    else
      motorCheck();
  }

  private void motorCheck() {
    if (searchMotorFlag){
      try {
        onFragmentInteraction("t#");
        movementCounter++;
        if (movementCounter >= turnAngle){
          searchMotorFlag = false;
          searchTypeFlag = true;
          movementCounter = 0;
          motorStop();
        }
      }catch (Exception e){      }
    }else{
      try {
        onFragmentInteraction("g#");
        movementCounter++;
        if (movementCounter >= forwardDistance){
          searchMotorFlag = true;
          searchTypeFlag = true;
          movementCounter = 0;
          motorStop();
        }
      }catch (Exception e){      }
    }
  }

  private void armCheck() {
    if (searchArmType == 'u'){
      servo4Value -= servo4SearchSpeed;
      try {
        onFragmentInteraction("s4u#");
      }catch (Exception e){ }
      if (servo4Value <= servo4SearchUp)
        searchArmType = 'd';
    }else if (searchArmType == 'd'){
      servo4Value += servo4SearchSpeed;
      try {
        onFragmentInteraction("s4d#");
      }catch (Exception e){      }
      if (servo4Value >= servo4SearchDown)
        searchArmType = '4';
    }else if (searchArmType == '4'){
      servo4Value -= servo4SearchSpeed;
      try {
        onFragmentInteraction("s4u#");
      }catch (Exception e){      }
      if (servo4Value <= servo4Reset)
        searchArmType = 'r';
    }


    else if (searchArmType == 'r'){
      try {
        onFragmentInteraction("s1r#");
        servo1Value -= servo1SearchSpeed;
      }catch (Exception e){      }
      if (servo1Value <= servo1SearchRight)
        searchArmType = 'l';
    }else if (searchArmType == 'l'){
      try {
        onFragmentInteraction("s1l#");
        servo1Value += servo1SearchSpeed;
      }catch (Exception e){      }
      if (servo1Value >= servo1SearchLeft)
        searchArmType = '1';
    }else if (searchArmType == '1'){
      try {
        onFragmentInteraction("s1R#");
        servo1Value = servo1Reset;
        searchArmType = 'u';
        searchTypeFlag = false;
      }catch (Exception e){      }
    }
  }

  void motorStop(){
    try {
      onFragmentInteraction("s#");
    }catch (Exception e){      }
  }

  @Override
  protected int getLayoutId() {
    return R.layout.camera_connection_fragment_tracking;
  }

  @Override
  protected Size getDesiredPreviewFrameSize() {
    return DESIRED_PREVIEW_SIZE;
  }

  @Override
  public void onClick(View v) {

  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

  }


  @Override
  public void onFragmentInteraction(String msg) {
    BluetoothFragment btFragment = (BluetoothFragment) getSupportFragmentManager().findFragmentByTag(bluetoothFragmentTag);
    btFragment.sendMsg(msg);
  }

  @Override
  public void onFragmentSend(String msg) {

  }


  // Which detection model to use: by default uses Tensorflow Object Detection API frozen
  // checkpoints.
  private enum DetectorMode {
    TF_OD_API;
  }

  @Override
  protected void setUseNNAPI(final boolean isChecked) {
    runInBackground(() -> detector.setUseNNAPI(isChecked));
  }

  @Override
  protected void setNumThreads(final int numThreads) {
    runInBackground(() -> detector.setNumThreads(numThreads));
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
