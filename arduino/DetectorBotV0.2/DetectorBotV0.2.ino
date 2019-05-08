///**************Libraries****************///
#include <NewPing.h>
#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
///************End Libraries**************///

///*****************UltraSonic**********************///
#define ultraVCC1 45
#define ultraTRIG1 47
#define ultraECHO1 49
#define ultraVCC2 39
#define ultraTRIG2 41
#define ultraECHO2 43
#define ultraVCC3 33
#define ultraTRIG3 35
#define ultraECHO3 37
#define ultraVCC4 36
#define ultraTRIG4 38
#define ultraECHO4 34
#define ultraGND4 32
#define max_distance 400
NewPing sonar1(ultraTRIG1, ultraECHO1, max_distance);
NewPing sonar2(ultraTRIG2, ultraECHO2, max_distance);
NewPing sonar3(ultraTRIG3, ultraECHO3, max_distance);
NewPing sonar4(ultraTRIG4, ultraECHO4, max_distance);

int minDistance = 30;



#define bluetoothVCC 28
#define bluetoothGND 30
///****************End UltraSonic**********************///


///************Time Counters****************///
unsigned long previousMillis = 0;
unsigned long distanceMillis = 0;
unsigned long movementTime = 0;
unsigned long leftTime = 0;
unsigned long rightTime = 0;
unsigned long currentTime = 0;

const long interval = 250;
const long distanceInterval = 100;
///**********End Time Counters*************///

///********Triggers********///
boolean movementFlag = true;
boolean isAuto = false;
///******End Triggers******///

///***Movement Motors***///
int rb=11;
int rf=10;
int lb=9;
int lf=8;
int motorSpeed = 100;
int motorAngel = 120;
int turnSpeedInc = 30;
///***End Movement Motors***///

///********Megnatis*********///
int magnet = 46;
boolean magnetState = false;
///******End Megnatis*******///

///*********MSG*********///
char msg;
String cmdValue="";
boolean msgEnd = false;
///******End MSG********///

///***********Servos************///
#define pulselen1Max 550
#define pulselen1Min 100
#define pulselen1Rst 415
#define pulselen2Max 550
#define pulselen2Min 150
#define pulselen2Rst 160
#define pulselen3Max 550
#define pulselen3Min 150
#define pulselen3Rst 525
#define pulselen4Max 520
#define pulselen4Min 270
#define pulselen4Rst 350

int s1 = 0;
int s2 = 2;
int s3 = 4;
int s4 = 6;
int servoSpeed = 1;

uint16_t pulselen1;  
uint16_t pulselen2;
uint16_t pulselen3;
uint16_t pulselen4;

int servoManDelay = 3;
int servoWriteDelay = servoManDelay;
int servoAutoDelay = 20;
int servo1SearchSpeed = 8;
int servo4SearchSpeed = 5;

int servo1ResetDelay = 10;
int servo4ResetDelay = 20;

Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();
///***********End Servos************///

///**************Time Variables*******************///
boolean isRightTurn = true;
boolean isInTurn = false;
boolean forwardFlag = true;
///************End Time Variables*****************///

///***************///
String detectedLabel = "";

///**************///
void setup() {

///*******Motorlar*********///
  pinMode(rf, OUTPUT);
  pinMode(rb, OUTPUT);
  pinMode(lf, OUTPUT);
  pinMode(lb, OUTPUT);
///***********************///

///*******UltraSonic***********///
  pinMode(ultraTRIG1, OUTPUT);
  pinMode(ultraECHO1, INPUT);
  pinMode(ultraVCC1, OUTPUT);
  pinMode(ultraTRIG2, OUTPUT);
  pinMode(ultraECHO2, INPUT);
  pinMode(ultraVCC2, OUTPUT);
  pinMode(ultraTRIG3, OUTPUT);
  pinMode(ultraECHO3, INPUT);
  pinMode(ultraVCC3, OUTPUT);
  pinMode(ultraTRIG4, OUTPUT);
  pinMode(ultraECHO4, INPUT);
  pinMode(ultraVCC4, OUTPUT);
  pinMode(ultraGND4, OUTPUT);
  digitalWrite(ultraVCC2, HIGH);
  digitalWrite(ultraVCC1, HIGH);
  digitalWrite(ultraVCC3, HIGH);
  digitalWrite(ultraVCC4, HIGH);
  digitalWrite(ultraGND4, LOW);

  pinMode(bluetoothVCC, OUTPUT);
  pinMode(bluetoothGND, OUTPUT);
  digitalWrite(bluetoothVCC, HIGH);
  digitalWrite(bluetoothGND, LOW);
///****************************///

  pinMode(magnet, OUTPUT);
  digitalWrite(magnet,LOW);

  pwm.begin();
  pwm.setPWMFreq(60);
  Serial.begin(115200);
  Serial3.begin(9600);
  delay(3000);
  Reset();  
  motorSpeed = 80;
}

/***DetectorBOT messages protocol***
 * **Recive Messages
 *  *function_letter$param1$param2$...paramN$#
 * **Send Messages
 *  *distance1$distance2$distance3$distance4$#
 */

/***Functions Letter***
 * **DISTANCES**
 *  *check_left_distance(){dr#}
 *  *check_right_distance(){dl#}
 * **MOVEMENTS** 
 *  *stop(){s#}
 *  *forward(){f#}
 *  *backward(){b#}
 *  *left(){l#}
 *  *right(){r#}
 *  *leftForward(){lf#}
 *  *leftBackward(){lb#}
 *  *rightForward(){rf#}
 *  *rightBackward(){rb#}
 *  *centerButton(){c#}
 *  *speedSet{n#;n=[0,1,2,3,4,5,6,7,8,9,x]}
 * **states**
 *  *searchState{ss#}
 *  *findState{sf#}
 *  *trackingState{st#}
 *  *leftState{sl#}
 * **ServoPulse**
 *  *setPulseValue{pn$v#;n=pulseNumber[1,2,3,4];v=pulseValue}
 * **magnet**
 *  *magnetSwitch(){mg#}
 * **Modes**
 *  *autoMode{A#}
 *  *ManualMode{M#}
 */


void loop() {
  
  checkBluetoothMsg(isAuto);
  check_arm_distance();
}



///*****************DISTANCE FUNCTIONS**********************///


void check_left_distance(){
   int distance = sonar1.ping_cm();
   Serial3.print(distance);
   Serial3.print("cm   ");
}

void check_right_distance(){
   int distance = sonar3.ping_cm();
   Serial3.print(distance);
   Serial3.print("cm   ");
}

void check_arm_distance(){
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    int distance = sonar4.ping_cm();
    Serial3.print(distance);
    Serial3.print("       ");
  }
}

void check_distances(){
  unsigned long currentMillis = millis();
  if (currentMillis - distanceMillis >= distanceInterval) {
    distanceMillis = currentMillis;
    int distancef = sonar2.ping_cm();
    Serial3.print(distancef);
    Serial3.print("#f");
    int distancer = sonar3.ping_cm();
    Serial3.print(distancer);
    Serial3.print("#r");
    int distancel = sonar1.ping_cm();
    Serial3.print(distancel);
    Serial3.print("#l");
    int distancea = sonar4.ping_cm();
    Serial3.print(distancea);
    Serial3.print("#a");
  }
}
///*****************END DISTANCE FUNCTIONS******************///

///*****************MOVEMENTS FUNCTIONS*********************///
void Stop(){
  analogWrite(rf,0);
  analogWrite(lf,0);
  analogWrite(rb,0);
  analogWrite(lb,0);  
}

void forward(){
  analogWrite(rb,0);
  analogWrite(lb,0);
  analogWrite(rf,motorSpeed);
  analogWrite(lf,motorSpeed);
}

void backward(){
  analogWrite(rf,0);
  analogWrite(lf,0);
  analogWrite(rb,motorSpeed);
  analogWrite(lb,motorSpeed);
}

void left(){
  analogWrite(rf,turnSpeed());
  analogWrite(lf,0);
  analogWrite(rb,0);
  analogWrite(lb,turnSpeed());
}

void right(){
  analogWrite(rf,0);
  analogWrite(lf,turnSpeed());
  analogWrite(rb,turnSpeed());
  analogWrite(lb,0);
}

void leftForward(){
  analogWrite(rb,0);
  analogWrite(lb,0);
  analogWrite(rf,turnSpeed());
  analogWrite(lf,angelSpeed());    
}

void rightForward(){
  analogWrite(rb,0);
  analogWrite(lb,0);
  analogWrite(rf,angelSpeed());
  analogWrite(lf,turnSpeed());    
}

void leftBackward(){
  analogWrite(rb,turnSpeed());
  analogWrite(lb,angelSpeed());
  analogWrite(rf,0);
  analogWrite(lf,0);    
}

void rightBackward(){
  analogWrite(rb,angelSpeed());
  analogWrite(lb,turnSpeed());
  analogWrite(rf,0);
  analogWrite(lf,0);    
}

void centerButton(){
  Reset();
}

int turnSpeed(){
  int result = motorSpeed + turnSpeedInc;
  if(result >= 255)
    return 255;
  else
    return result; 
}

int angelSpeed(){
  int result = motorSpeed - motorAngel;
  if(result <= 40)
    return 40;
  else
    return result;
}
///*****************END MOVEMENTS FUNCTIONS*****************///

///***************Manual Commands*********************///
void checkCommands(){
  int Length = cmdValue.length();
  if(Length == 1){
      if(cmdValue == "0")
        motorSpeed = 0;
      else if(cmdValue == "1")
        motorSpeed = 70;
      else if(cmdValue == "2")
        motorSpeed = 80;
      else if(cmdValue == "3")
        motorSpeed = 100;
      else if(cmdValue == "4")
        motorSpeed = 120;
      else if(cmdValue == "5")
        motorSpeed = 140;
      else if(cmdValue == "6")
        motorSpeed = 160;
      else if(cmdValue == "7")
        motorSpeed = 180;
      else if(cmdValue == "8")
        motorSpeed = 200;
      else if(cmdValue == "9")
        motorSpeed = 230;
      else if(cmdValue == "x")
        motorSpeed = 255;
      else if(cmdValue == "l")
        left();
      else if(cmdValue == "s")
        Stop();
      else if(cmdValue == "r")
        right();
      else if(cmdValue == "f")
        forward();
      else if(cmdValue == "b")
        backward();
      else if(cmdValue == "c")
        centerButton();
      else if(cmdValue == "A")
        isAuto = true;
      else if(cmdValue == "M")
        isAuto = false;
    }else if(Length == 2){
      if(cmdValue == "lb")
        leftBackward();
      else if(cmdValue == "lf")
        leftForward();
      else if(cmdValue == "rb")
        rightBackward();
      else if(cmdValue == "rf")
        rightForward();
      else if(cmdValue == "a0")
        motorAngel = 0;
      else if(cmdValue == "a1")
        motorAngel = 60;
      else if(cmdValue == "a2")
        motorAngel = 120;
      else if(cmdValue == "a3")
        motorAngel = 180;
      else if(cmdValue == "mg")
        magnetSwitch();
    }else if(Length > 3){
      if(cmdValue[0] == 'p'){
        String pulseValueStr = "";
        for(int i=0; i<Length-3;i++){
          pulseValueStr += cmdValue[i+3];
        }
        int pulseValue = pulseValueStr.toInt();
        if(cmdValue[1] == '1'){
          servoWrite(s1, pulselen1 , pulseValue*2+pulselen1Min);
          pulselen1 = pulseValue*2+pulselen1Min;
        }else if(cmdValue[1] == '4'){
          servoWrite(s4, pulselen4 , pulseValue+pulselen4Min);
          pulselen4 = pulseValue+pulselen4Min;
        }else if(cmdValue[1] == '2'){
          servoWrite(s2, pulselen2 , pulseValue*2+pulselen2Min);
          pulselen2 = pulseValue*2+pulselen2Min;
        }else if(cmdValue[1] == '3'){
          servoWrite(s3, pulselen3 , pulseValue*2+pulselen3Min);
          pulselen3 = pulseValue*2+pulselen3Min;
        } 
      }
    }  
}
///******************End Manual Commands*********************///

///***************Auto Commands*********************///
void checkAutoCommands(){
  int Length = cmdValue.length();
  if(Length == 1){
      if(cmdValue == "0")
        motorSpeed = 0;
      else if(cmdValue == "1")
        motorSpeed = 70;
      else if(cmdValue == "2")
        motorSpeed = 80;
      else if(cmdValue == "3")
        motorSpeed = 90;
      else if(cmdValue == "4")
        motorSpeed = 100;
      else if(cmdValue == "5")
        motorSpeed = 125;
      else if(cmdValue == "6")
        motorSpeed = 150;
      else if(cmdValue == "7")
        motorSpeed = 180;
      else if(cmdValue == "8")
        motorSpeed = 210;
      else if(cmdValue == "9")
        motorSpeed = 255;
      else if(cmdValue == "l")
        left();
      else if(cmdValue == "s")
        Stop();
      else if(cmdValue == "r")
        right();
      else if(cmdValue == "f")
        forward();
      else if(cmdValue == "b")
        backward();
      else if(cmdValue == "t")
        turnSearch();
      else if(cmdValue == "g")
        goSearch();
      else if(cmdValue == "A")
        isAuto = true;
      else if(cmdValue == "M")
        isAuto = false;
      else if(cmdValue == "R")
        Reset();
    }else if(Length == 2){
      if(cmdValue == "mg")
        magnetSwitch();
    }else if(Length == 3){
      if(cmdValue == "s4u"){
        servoWrite(s4,pulselen4,pulselen4 - servo4SearchSpeed);
        pulselen4 -= servo4SearchSpeed;
      }else if(cmdValue == "s4d"){
        servoWrite(s4,pulselen4,pulselen4 + servo4SearchSpeed);
        pulselen4 += servo4SearchSpeed;
      }else if(cmdValue == "s1r"){
        servoWrite(s1,pulselen1,pulselen1 - servo1SearchSpeed);
        pulselen1 -= servo1SearchSpeed;
      }else if(cmdValue == "s1l"){
        servoWrite(s1,pulselen1,pulselen1 + servo1SearchSpeed);
        pulselen1 += servo1SearchSpeed;
      }else if(cmdValue == "s4R"){
        servo4WriteR(s4,pulselen4,pulselen4Rst);
        pulselen4 = pulselen4Rst;
      }else if(cmdValue == "s1R"){
        servo1WriteR(s1,pulselen1,pulselen1Rst);
        pulselen1 = pulselen1Rst;
      }
    }else if(Length > 3){
      if(cmdValue[0] == 'p'){
        String pulseValueStr = "";
        for(int i=0; i<Length-3;i++){
          pulseValueStr += cmdValue[i+3];
        }
        int pulseValue = pulseValueStr.toInt();
        if(cmdValue[1] == '1'){
          pulselen1 = pulseValue*2+pulselen1Min;
          pwm.setPWM(s1, 0, pulselen1);
        }else if(cmdValue[1] == '4'){
          pulselen4 = pulseValue+pulselen4Min;
          pwm.setPWM(s4, 0, pulselen4);
        }else if(cmdValue[1] == '5'){
          pulselen2 = pulseValue*2+pulselen2Min;  
          pulselen3 = 700 - pulselen2;
          pwm.setPWM(s3, 0, pulselen3);
          pwm.setPWM(s2, 0, pulselen2);
        }  
      }
    }  
}
///******************End Auto Commands*********************///



///********************Servo Func**********************///
void Reset(){
  pulselen1=pulselen1Rst;
  pulselen2=pulselen2Rst;
  pulselen3=pulselen3Rst;
  pulselen4=pulselen4Rst;
  pwm.setPWM(s4, 0, pulselen4);
  delay(10);
  pwm.setPWM(s2, 0, pulselen2);
  delay(10);
  pwm.setPWM(s3, 0, pulselen3);
  delay(200);
  pwm.setPWM(s1, 0, pulselen1);
}

void servoWrite(int n , int s , int e){
  if(s<e){
    for(int i = s ; i <= e ; i++){
      pwm.setPWM(n, 0, i);
      delay(servoWriteDelay);
    }  
  }else{
    for(int i = s ; i >= e ; i--){
      pwm.setPWM(n, 0, i);
      delay(servoWriteDelay);
    }
  }
}

void servo1WriteR(int n , int s , int e){
  if(s<e){
    for(int i = s ; i <= e ; i++){
      pwm.setPWM(n, 0, i);
      right();
      delay(servo1ResetDelay);

    }  
  }else{
    for(int i = s ; i >= e ; i--){
      pwm.setPWM(n, 0, i);
      left();
      delay(servo1ResetDelay);
    }
  }
  Stop();
}
void servo4WriteR(int n , int s , int e){
  if(s<e){
    for(int i = s ; i <= e ; i++){
      pwm.setPWM(n, 0, i);
      forward();
      delay(servo4ResetDelay);
    }  
  }else{
    for(int i = s ; i >= e ; i--){
      pwm.setPWM(n, 0, i);
      backward();
      delay(servo4ResetDelay);
    }
  }
  Stop();
}
///*******************End Servo Func********************///

///********************Magnet Func**********************///
void magnetSwitch(){
    magnetState = !magnetState;
    digitalWrite(magnet,magnetState);
}
///******************End Magnet Func********************///

void checkBluetoothMsg(boolean controllerMode){
   if(Serial3.available()){
      while(Serial3.available()){
        msg = Serial3.read();
        if(msg == '#'){
          msgEnd = true;
          break;}
        cmdValue += String(msg);
      }  
    }
  
    if(msgEnd){
      if(controllerMode){ 
        servoWriteDelay = servoAutoDelay;
        checkAutoCommands();
      }else{
        servoWriteDelay = servoManDelay;
        checkCommands();
      }
      msgEnd=false;
      cmdValue="";  
    }   
}

void turnSearch(){
  if(!isInTurn){
    int distancel = sonar1.ping_cm();
    int distancer = sonar3.ping_cm();
    if(distancer > distancel)
      isRightTurn = true;
    else
      isRightTurn = false;
    isInTurn = true;
  }
  if(isRightTurn){
    right();
  }else{
    left();  
  }
}

void goSearch(){
  int distance = sonar2.ping_cm();
  if(distance > minDistance)
    forward();
  else
    Stop();
}
