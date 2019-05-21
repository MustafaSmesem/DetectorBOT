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
import android.os.Handler;
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
  public float MINIMUM_CONFIDENCE_TF_OD_API = 0.9f;
  private static final boolean MAINTAIN_ASPECT = false;
  private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
  private static final boolean SAVE_PREVIEW_BITMAP = false;
  private static final float TEXT_SIZE_DIP = 10;
  OverlayView trackingOverlay;
  private Integer sensorOrientation;

  private int armDistance = 0;

  private Classifier detector;

  private boolean isSearchState = false;

  private int positionX = 0, positionY = 0;
  private boolean isLabel = false;
  private String detectedLabel="";
  private float score=0;
  private int[] pos = new int[4];
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

  private final int servo4Reset = 375;
  private final int servo4Max = 520;
  private final int servo4Min = 270;
  private int servo4Value = servo4Reset;

  private final int servo1Reset = 415;
  private final int servo1Max = 550;
  private final int servo1Min = 100;
  private int servo1Value = servo1Reset;

  private final int servo2Reset = 160;
  private final int servo2Max = 550;
  private final int servo2Min = 150;
  private int servo2Value = servo2Reset;

  private final int servo3Reset = 525;
  private final int servo3Max = 550;
  private final int servo3Min = 150;
  private int servo3Value = servo3Reset;

  /*** End Servos ***/

  private boolean isDetected = true;
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

  /*** Tacking State variables ***/
    private int objectHeight = 0;
    private int objectWidth = 0;
    private final int xTolerance = 10;
    private final int yTolerance = 10;
    private final int screenTargetX = 180;
    private final int screenTargetY = 200;
    //private final int vidaArea = 550 , pilArea = 800 , makasArea = 4500 , tornavidaArea = 2800, wrenchArea = 2500 , penseArea = 3000;

    private boolean  xIsOk = false , yIsOk = false , trackingResetStates = false;

    private boolean magnetState = false;

    private char statesFlag = 't';
    private int servo1TrackingSpeed = 8;
    private int servo4TrackingSpeed = 8;
  /*** End Tacking State variables ***/


  /*** Catch State variables ***/
  private final int xC1Tolerance = 5;
  private final int yC1Tolerance = 5;
  private final int screenCatch1X = 200;
  private final int screenCatch1Y = 220;
  private boolean  xC1IsOk = false , yC1IsOk = false;

  private final int xC2Tolerance = 20;
  private final int yC2Tolerance = 5;
  private final int screenCatch2X = 230;
  private final int screenCatch2Y = 230;
  private boolean  xC2IsOk = false , yC2IsOk = false;

  private int servo1CatchSpeed = 4;
  private int servo4CatchSpeed = 4;

  private boolean isCatching = false;
  /*** END Catch State variables ***/

  private final String[] trackingLabels = {"Vida", "Tornavida", "Makas", "Pense", "Pil", "Ingliz anahtari"};
  private final String[] catchingLabels = {"Vida", "Tornavida ucu", "Makas ucu", "Pense ucu", "Pil", "Ingliz anahtari"};
  private final String[] boxLabels = {"Box front", "Box left", "Box right"};

  private boolean detectState = false;
  private String LAST_DETECTED_LABEL = "";

  private char searchCatchType = 'u';
  private int servoCatchCounter = 0;
  private final int catchSearchDelay = 7;
  private final int trackingDelay = 50;
  private int isCatchingCounter=0;


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
    resetServos();
    isDetected = false;
    isDetectedCounter =0;
    searchTypeFlag = true;
    searchArmType = 'u';
    searchMotorFlag = true;
    movementCounter = 0;
    xC1IsOk = false ;
    yC1IsOk = false ;
    xC2IsOk = false ;
    yC2IsOk = false ;
    xIsOk = false ;
    yIsOk = false ;
    statesFlag = 't';
    isCatching = false;
    trackingResetStates = false;

    motorStop();

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
            //LOGGER.i("Running detection on image " + currTimestamp);
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

            /*for (final Classifier.Recognition result : results){
              String label = result.getTitle();
              if (statesFlag == 'l'){
                if (isContent(boxLabels , label)){
                  detecting(result , canvas , minimumConfidence , paint , mappedRecognitions);
                  break;
                }
              }else if (statesFlag == 'k'){
                if (isContent(catchingLabels , label)){
                  detecting(result , canvas , minimumConfidence , paint , mappedRecognitions);
                  break;
                }
              }else{
                detecting(result , canvas , minimumConfidence , paint , mappedRecognitions);
                break;
              }
            }*/

            for (final Classifier.Recognition result : results){
              final RectF location = result.getLocation();
              if (location != null && result.getConfidence() >= minimumConfidence) {
                motorStop();

                positionX = (int) location.centerX();
                positionY = (int) location.centerY();
                objectWidth = (int) (location.right - location.left);
                objectHeight = (int) (location.bottom - location.top);

                score = result.getConfidence();
                score *= 100;
                scoreS = df.format(score);

                pos[0] = (int) location.left;
                pos[1] = (int) location.top;
                pos[2] = (int) location.right;
                pos[3] = (int) location.bottom;

                canvas.drawRect(location, paint);
                cropToFrameTransform.mapRect(location);
                result.setLocation(location);
                mappedRecognitions.add(result);

                detectedLabel = result.getTitle();
                if (statesFlag == 'l'){
                  trackingLeftState();
                  /*if (isContent(boxLabels , detectedLabel)){
                    detectState = true;
                    isDetected = true;
                    isDetectedCounter = 0;
                    trackingLeftState();
                  }else{
                    isDetected = false;
                    searchState();
                  }*/
                }else if(statesFlag == 'k'){
                  switch (LAST_DETECTED_LABEL){
                    case "Tornavida":
                      if (detectedLabel.equals("Tornavida ucu")){
                        detectState = true;
                        isCatching = true;
                        isCatchingCounter = 0;
                        trackingCatch2State();
                      }else{
                        isCatching = false;
                        catchSearch();
                      }
                      break;
                    case "Makas":
                      if (detectedLabel.equals("Makas ucu")){
                        detectState = true;
                        isCatching = true;
                        isCatchingCounter = 0;
                        trackingCatch2State();
                      }else{
                        isCatching = false;
                        catchSearch();
                      }
                      break;
                    case "Pense":
                      if (detectedLabel.equals("Pense ucu")){
                        detectState = true;
                        isCatching = true;
                        isCatchingCounter = 0;
                        trackingCatch2State();
                      }else{
                        isCatching = false;
                        catchSearch();
                      }
                      break;
                    case "Pil":
                      if (detectedLabel.equals("Pil")){
                        detectState = true;
                        isCatching = true;
                        isCatchingCounter = 0;
                        trackingCatch2State();
                      }else{
                        isCatching = false;
                        catchSearch();
                      }
                      break;
                    case "Ingliz anahtari":
                      if (detectedLabel.equals("Ingliz anahtari")) {
                        detectState = true;
                        isCatching = true;
                        isCatchingCounter = 0;
                        trackingCatch2State();
                      }else{
                        isCatching = false;
                        catchSearch();
                      }
                      break;
                    case "Vida":
                      if (detectedLabel.equals("Vida")){
                        detectState = true;
                        isCatching = true;
                        isCatchingCounter = 0;
                        trackingCatch2State();
                      }else{
                        isCatching = false;
                        catchSearch();
                      }
                      break;
                  }
                }else if(statesFlag == 'c'){
                  if (!isContent(boxLabels,detectedLabel)){
                    trackingCatch1State();
                    detectState = true;
                    isCatching = true;
                    isCatchingCounter = 0;
                    LAST_DETECTED_LABEL = detectedLabel;
                  }else{
                    isCatching = false;
                    catchSearch();
                  }
                }else{
                  if (!isContent(boxLabels,detectedLabel)){
                    trackingState();
                    detectState = true;
                    isDetected = true;
                    isDetectedCounter = 0;
                  }else{
                    isDetected = false;
                    searchState();
                  }
                }

              } else {
                if (isDetectedCounter >= detectedDelay ) {
                  isDetected = false;
                  xIsOk = false;
                  yIsOk = false;
                  trackingResetStates = false;
                }
                if (isCatchingCounter >= detectedDelay){
                  isCatching = false;
                  isCatchingCounter = 0;
                  trackingResetStates = false;
                  xC1IsOk = false;
                  yC1IsOk = false;
                  xC2IsOk = false;
                  yC2IsOk = false;
                }
                if (statesFlag == 't'){
                  if (isAuto && !isDetected) {
                    searchState();
                  } else {
                    isDetectedCounter++;
                  }
                }else if (statesFlag== 'c' || statesFlag == 'k'){
                  if (isAuto && !isCatching) {
                    catchSearch();
                  } else {
                    isCatchingCounter++;
                  }
                }

              }

              if (detectState)
                break;

              detectState = false;
            }


            tracker.trackResults(mappedRecognitions, luminanceCopy, currTimestamp);
            trackingOverlay.postInvalidate();

            computingDetection = false;

            runOnUiThread(
                    new Runnable() {
                      @Override
                      public void run() {
                        if (isAuto && isDetected){
                          tvObjectPosx.setText("X: "+positionX);
                          tvObjectPosy.setText("Y: "+positionY);
                          tvObjectWidth.setText("W: "+objectWidth);
                          tvObjectHeight.setText("H: "+objectHeight);
                          tvObjectLabel.setText("C: "+detectedLabel);
                          tvObjectLeft.setText("L: "+pos[0]);
                          tvObjectTop.setText("T: "+pos[1]);
                          tvObjectRight.setText("R: "+pos[2]);
                          tvObjectBottom.setText("B: "+pos[3]);
                        }else {
                          clearTv();
                        }
                      }
                    });

          }
        });

  }


  private boolean isContent(String[] labels, String label) {
    for (int i = 0; i < labels.length;i++){
      String newLabel = labels[i];
      if (label.equals(newLabel))
        return true;
    }
    return false;
  }

  private void trackingLeftState() {
    //delay(2000);
    leftState();
  }

  private void catch1(){
    motorStop();
    delay(500);
    try {
      onFragmentInteraction("f#");
    } catch (Exception e) { }
    delay(800);
    motorStop();
    try {
      onFragmentInteraction("c1#");
      servo4Value = 400;
      servo2Value = 430;
      servo3Value = 400;
      statesFlag = 'c';
    } catch (Exception e) { }
    delay(5000);
  }

  private void catch2(){
    delay(500);
    try {
      onFragmentInteraction("f#");
    } catch (Exception e) { }
    delay(250);
    motorStop();
    try {
      onFragmentInteraction("c2#");
      servo2Value = 500;
      servo3Value = 372;
      statesFlag = 'k';
    } catch (Exception e) { }
    delay(5000);
  }

  private void catch3(){
    if (!magnetState)
      magnetSwitch();
    delay(500);
    try {
      onFragmentInteraction("c3#");
      servo2Value = 550;
      servo3Value = 352;
      statesFlag = 'l';
    } catch (Exception e) { }
    delay(5000);
    resetCatch();
    delay(3000);
  }

  private void catchSearch() {

    if (searchCatchType == 'u'){
      servo4Value -= servo4SearchSpeed;
      servoCatchCounter++;
      try {
        onFragmentInteraction("s4u#");
      }catch (Exception e){ }
      if (servoCatchCounter >= catchSearchDelay){
        searchCatchType = 'd';
        servoCatchCounter = 0;
      }
    }else if (searchCatchType == 'd'){
      servo4Value += servo4SearchSpeed;
      servoCatchCounter++;
      try {
        onFragmentInteraction("s4d#");
      }catch (Exception e){      }
      if (servoCatchCounter >= catchSearchDelay){
        searchCatchType = 'r';
        servoCatchCounter = 0;
      }
    }


    else if (searchCatchType == 'r'){
      try {
        onFragmentInteraction("s1r#");
        servo1Value -= servo1SearchSpeed;
        servoCatchCounter++;
      }catch (Exception e){      }
      if (servoCatchCounter >= catchSearchDelay){
        searchCatchType = 'l';
        servoCatchCounter = 0;
      }
    }else if (searchCatchType == 'l'){
      try {
        onFragmentInteraction("s1l#");
        servo1Value += servo1SearchSpeed;
        servoCatchCounter++;
      }catch (Exception e){      }
      if (servoCatchCounter >= catchSearchDelay){
        searchCatchType = 'u';
        servoCatchCounter = 0;
      }
    }
    resetTheBase();
    delay(trackingDelay*2);
  }

  private void trackingCatch1State() {

    int xError = screenCatch1X - positionX;
    int yError = screenCatch1Y - positionY;
    if (abs(xError) <= xC1Tolerance ){xC1IsOk = true;}else{xC1IsOk = false;}
    if (abs(yError) <= yC1Tolerance ){yC1IsOk = true;}else{yC1IsOk = false;}
    if (xC1IsOk && yC1IsOk){
      catch2();
    }else{
      if(!xC1IsOk){
        if (xError > 0){
          try {
            onFragmentInteraction("s1l#");
            servo1Value += servo1SearchSpeed;
          }catch (Exception e){ }
        }else{
          try {
            onFragmentInteraction("s1r#");
            servo1Value -= servo1SearchSpeed;
          }catch (Exception e){ }
        }
        trackingResetStates = false;
      }else if (!yC1IsOk){
        if (yError > 0){
          try {
            onFragmentInteraction("s4f#");
            servo4Value -= servo4SearchSpeed;
          }catch (Exception e){ }
        }else{
          try {
            onFragmentInteraction("s4b#");
            servo4Value += servo4SearchSpeed;
          }catch (Exception e){ }
        }
        trackingResetStates = true;
      }

    }
    resetTheBase();
    delay(trackingDelay*2);

  }

  private void trackingCatch2State() {

    int xError = screenCatch2X - positionX;
    int yError = screenCatch2Y - positionY;
    if (abs(xError) <= xC2Tolerance ){xC2IsOk = true;}else{xC2IsOk = false;}
    if (abs(yError) <= yC2Tolerance ){yC2IsOk = true;}else{yC2IsOk = false;}
    if (xC2IsOk && yC2IsOk){
      catch3();
    }else{
      if(!xC2IsOk){
        if (xError > 0){
          try {
            onFragmentInteraction("s1l#");
            servo1Value += servo1SearchSpeed;
          }catch (Exception e){ }
        }else{
          try {
            onFragmentInteraction("s1r#");
            servo1Value -= servo1SearchSpeed;
          }catch (Exception e){ }
        }
        trackingResetStates = false;
      }else if (!yC2IsOk){
        if (yError > 0){
          try {
            onFragmentInteraction("s4f#");
            servo4Value -= servo4SearchSpeed;
          }catch (Exception e){ }
        }else{
          try {
            onFragmentInteraction("s4b#");
            servo4Value += servo4SearchSpeed;
          }catch (Exception e){ }
        }
        trackingResetStates = true;
      }

    }
    resetTheBase();
    delay(trackingDelay*2);
  }

  private void delay(int delay){
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void trackingState() {

    int xError = screenTargetX - positionX;
    int yError = screenTargetY - positionY;
    if (abs(xError) <= xTolerance ){xIsOk = true;}else{xIsOk = false;}
    if (abs(yError) <= yTolerance ){yIsOk = true;}else{yIsOk = false;}
    if (xIsOk && yIsOk){
      catch1();
    }else{
      if(!xIsOk){
        if (xError > 0){
          try {
            onFragmentInteraction("s1l#");
            servo1Value += servo1SearchSpeed;
          }catch (Exception e){ }
        }else{
          try {
            onFragmentInteraction("s1r#");
            servo1Value -= servo1SearchSpeed;
          }catch (Exception e){ }
        }
        trackingResetStates = false;
      }else if (!yIsOk){
        if (yError > 0){
          try {
            onFragmentInteraction("s4f#");
            servo4Value -= servo4SearchSpeed;
          }catch (Exception e){ }
        }else{
          try {
            onFragmentInteraction("s4b#");
            servo4Value += servo4SearchSpeed;
          }catch (Exception e){ }
        }
        trackingResetStates = true;
      }

    }
    resetTheBase();
    delay(trackingDelay);
  }

  private void resetTheBase() {

    if (!trackingResetStates){
      try {
        onFragmentInteraction("s1R#");
        servo1Value = servo1Reset;
      }catch (Exception e){ }
    }else {
      try {
        onFragmentInteraction("s4R#");
        servo4Value = servo4Reset;
      } catch (Exception e) { }
    }
  }

  private void magnetSwitch() {
    try {
      onFragmentInteraction("mg#");
      magnetState = !magnetState;
    } catch (Exception e) { }
  }

  private void leftState() {
    resetApp();
    if (magnetState)
      magnetSwitch();
  }

  private void resetServos() {
    try {
      onFragmentInteraction("c#");
      servo1Value = servo1Reset;
      servo2Value = servo2Reset;
      servo3Value = servo3Reset;
      servo4Value = servo4Reset;
    } catch (Exception e) { }
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      Toast.makeText(this, "cannot sleeping", Toast.LENGTH_SHORT).show();
    }
  }

  private void resetCatch() {
    try {
      onFragmentInteraction("c5#");
      servo1Value = servo1Reset;
      servo2Value = servo2Reset;
      servo3Value = servo3Reset;
      servo4Value = servo4Reset;
    } catch (Exception e) { }
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Toast.makeText(this, "cannot sleeping", Toast.LENGTH_SHORT).show();
    }
  }

  private int abs(int x) {
    if (x<0)
      return (x *= -1);
    else
      return x;
  }

  private void searchState() {
    if(searchTypeFlag)
      armCheck();
    else
      motorCheck();
    delay(trackingDelay);
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

  /*@Override
  public void bluetoothDistance(int d) {
    armDistance = d;
    armDistanceTv.setText(armDistance+" cm");
  }*/

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

  /*private void detecting(Classifier.Recognition result, Canvas canvas, float minimumConfidence, Paint paint, List<Classifier.Recognition> mappedRecognitions) {
    final RectF location = result.getLocation();
    if (location != null && result.getConfidence() >= minimumConfidence) {
      motorStop();
      isDetected = true;
      isDetectedCounter = 0;

      positionX = (int) location.centerX();
      positionY = (int) location.centerY();
      objectWidth = (int) (location.right - location.left);
      objectHeight = (int) (location.bottom - location.top);
      detectedLabel = result.getTitle();

      score = result.getConfidence();
      score *= 100;
      scoreS = df.format(score);

      pos[0] = (int) location.left;
      pos[1] = (int) location.top;
      pos[2] = (int) location.right;
      pos[3] = (int) location.bottom;

      canvas.drawRect(location, paint);
      cropToFrameTransform.mapRect(location);
      result.setLocation(location);
      mappedRecognitions.add(result);

      switch (statesFlag) {
        case 't':
          trackingState();
          break;
        case 'c':
          trackingCatchState();
          break;
        case 'k':
          catchState();
          break;
        case 'l':
          trackingLeftState();
          break;
      }


    } else {
      if (isDetectedCounter >= detectedDelay) {
        isDetected = false;
        xIsOk = false;
        yIsOk = false;
        trackingResetStates = false;
        if (statesFlag == 'c' || statesFlag == 'l' || statesFlag == 'k') {
          statesFlag = 't';
          resetServos();
          searchTypeFlag = true;
          searchArmType = 'u';
        }
      }
      if (isAuto && !isDetected) {
        searchState();
      } else {
        isDetectedCounter++;
      }
    }
  }*/
}