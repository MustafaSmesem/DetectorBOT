# DetectorBOT Android App

### Overview
This is a camera app that continuously detects the objects that developed by Tensorflow Lite team I have edited it to fits the requirements of my project.
you can see the orginal project from [Here](https://github.com/tensorflow/examples/tree/master/lite/examples/object_detection/android).

The model files are already in the assests folder which trained on 1000 photo for 6 classes (screw , screw driver , scissor , pliers , battery , wrench). you can make your custom model and replace the assets [detect.tflit](https://github.com/MustafaSmesem/DetectorBOT/blob/master/android/DetectorBOTApp/app/src/main/assets/detect.tflite), [labelmap.pbtxt](https://github.com/MustafaSmesem/DetectorBOT/blob/master/android/DetectorBOTApp/app/src/main/assets/labelmap.txt) with your model files.

### Notes:
- the first label in the label map must be ???.
- the labelmap for DetectorBOT is written in the turkish language.
  1. screw => vida
  2. screw driver => tornavida
  3. scissor => makas
  4. plires => pense
  5. battery => pil
  6. wrench => ingliz anahtari
  
<!-- TODO(b/124116863): Add app screenshot. -->

## Build the App using Android Studio

### Prerequisites

* If you don't have already, install **[Android Studio](https://developer.android.com/studio/index.html)**, following the instructions on the website.

* You need an Android device and Android development environment with minimum API 21.
* Android Studio 3.2 or later.

### Building
* Open Android Studio, and from the Welcome screen, select Open an existing Android Studio project.

* From the Open File or Project window that appears, navigate to and select the tensorflow-lite/examples/object_detection/android directory from wherever you cloned the TensorFlow Lite sample GitHub repo. Click OK.

* If it asks you to do a Gradle Sync, click OK.

* You may also need to install various platforms and tools, if you get errors like "Failed to find target with hash string 'android-21'" and similar.
Click the Run button (the green arrow) or select Run > Run 'android' from the top menu. You may need to rebuild the project using Build > Rebuild Project.

* If it asks you to use Instant Run, click Proceed Without Instant Run.

* Also, you need to have an Android device plugged in with developer options enabled at this point. See **[here](https://developer.android.com/studio/run/device)** for more details on setting up developer devices.


### Training custom model
You can training the app for your own data set by following the instructions from [here](https://github.com/MustafaSmesem/DetectorBOT/tree/master/android/training_data_set).
