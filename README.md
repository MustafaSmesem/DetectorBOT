# DetectorBOT

A robot that use TensorFlow lite object-detection app to collect the different equipments

## DetectorBOT Components:
**DetectorBOT** has tow basic component:
- [**DetectorBOT** android app](https://github.com/MustafaSmesem/DetectorBOT/tree/master/android) that will detect the equipments around the robots and send the commands to the arduino via Bluetooth.
- [**DetectorBOT** arduino program](https://github.com/MustafaSmesem/DetectorBOT/tree/master/arduino) which will control the robot's motors and sensors according to the commands that's received from the android app.