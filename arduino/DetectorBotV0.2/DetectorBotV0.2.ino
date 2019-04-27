///**************Libraries****************///
#include <NewPing.h>
#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
///************End Libraries**************///

int labelCounter = 0;
int led = 13;
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
#define ultraVCC4 44
#define ultraTRIG4 42
#define ultraECHO4 40
#define max_distance 400
NewPing sonar1(ultraTRIG1, ultraECHO1, max_distance);
NewPing sonar2(ultraTRIG2, ultraECHO2, max_distance);
NewPing sonar3(ultraTRIG3, ultraECHO3, max_distance);
NewPing sonar4(ultraTRIG4, ultraECHO4, max_distance);

int minDistance = 25;
///****************End UltraSonic**********************///


///************Time Counters****************///
unsigned long previousMillis = 0;
unsigned long forwardTime = 0;
unsigned long leftTime = 0;
unsigned long rightTime = 0;

const long interval = 100;
const long forwardInterval = 3000;
const long turnInterval = 500;
///**********End Time Counters*************///

///********Triggers********///
char movementFlag = 'f'; //forward = f ; turn = t ; forwardLeft = l ; forwardright = r;
char turnFlag = 't'; //turn = t ; right = r ; left = l;

boolean isAuto = false;
boolean isSearch = false;
boolean isFind = false;
boolean isTracking = false;
boolean isInTarget = false;
boolean isSmall = false;
char statesFlag = 's';

char robotState = 's';
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
#define pulselen2Rst 150
#define pulselen3Max 550
#define pulselen3Min 150
#define pulselen3Rst 525
#define pulselen4Max 520
#define pulselen4Min 270
#define pulselen4Rst 370

int s1 = 0;
int s2 = 2;
int s3 = 4;
int s4 = 6;
int servoSpeed = 1;

uint16_t pulselen1;  
uint16_t pulselen2;
uint16_t pulselen3;
uint16_t pulselen4;

int armSearchSpeed = 12;

Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();
///***********End Servos************///

///**************Time Variables*******************///
const int forwardCircleDelay = 7500;
unsigned long forwardCircleMillis = 0;
boolean forwardCircleFlag = true;
const int circleDelay = 1500;
unsigned long circleMillis = 0;
boolean circleFlag = true;

char searchStateFlag = 'a';
int circleFlipFlag = 0;

///************End Time Variables*****************///

///***************///
String detectedLabel = "";
///**************///
void setup() {
  pinMode(led, OUTPUT);
  digitalWrite(led,HIGH);
  delay(1000);
  digitalWrite(led,LOW);
  delay(1000);
  digitalWrite(led,HIGH);
  delay(1000);
  digitalWrite(led,LOW);
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
  digitalWrite(ultraVCC2, HIGH);
  digitalWrite(ultraVCC1, HIGH);
  digitalWrite(ultraVCC3, HIGH);
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

}



///*****************DISTANCE FUNCTIONS**********************///
void check_front_distance(){
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    int distance = sonar2.ping_cm();
    Serial3.print(distance);
    Serial3.print("cm   ");
  }
}

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
    Serial3.print("cm   ");
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
    }else if(Length == 3){
      if(cmdValue == "p1u" && pulselen1 < pulselen1Max){
        pulselen1 += 2;
        pwm.setPWM(s1, 0, pulselen1);
      }else if(cmdValue == "p1d" && pulselen1 > pulselen1Min){
        pulselen1 -= 2;
        pwm.setPWM(s1, 0, pulselen1);
      }else if(cmdValue == "p2u" && pulselen2 < pulselen2Max){
        pulselen2 += 2;
        pwm.setPWM(s2, 0, pulselen2);
      }else if(cmdValue == "p2d" && pulselen2 > pulselen2Min){
        pulselen2 -= 2;
        pwm.setPWM(s2, 0, pulselen2);
      }else if(cmdValue == "p3d" && pulselen3 < pulselen3Max){
        pulselen3 += 2;
        pwm.setPWM(s3, 0, pulselen3);
      }else if(cmdValue == "p3u" && pulselen3 > pulselen3Min){
        pulselen3 -= 2;
        pwm.setPWM(s3, 0, pulselen3);
      }else if(cmdValue == "p4u" && pulselen4 < pulselen4Max){
        pulselen4 += 1;
        pwm.setPWM(s4, 0, pulselen4);
      }else if(cmdValue == "p4d" && pulselen4 > pulselen4Min){
        pulselen4 -= 1;
        pwm.setPWM(s4, 0, pulselen4);
      }else if(cmdValue == "pxu" && pulselen2 < pulselen2Max){
        pulselen2 +=2;
        pulselen3 -=2;
        pwm.setPWM(s2, 0, pulselen2);
        pwm.setPWM(s3, 0, pulselen3);
      }else if(cmdValue == "pxd" && pulselen2 > pulselen2Min){
        pulselen2 -=2;
        pulselen3 +=2;
        pwm.setPWM(s2, 0, pulselen2);
        pwm.setPWM(s3, 0, pulselen3);
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
      else if(cmdValue == "c")
        centerButton();
      else if(cmdValue == "A")
        isAuto = true;
      else if(cmdValue == "M")
        isAuto = false;
    }else if(Length == 2){
      if(cmdValue == "ss")
        statesFlag = 's';
      else if(cmdValue == "sf")
        statesFlag = 'f';
      else if(cmdValue == "st")
        statesFlag = 't';
      else if(cmdValue == "sl")
        statesFlag = 's';
      else if(cmdValue == "dr")
        check_right_distance();
      else if(cmdValue == "dl")
        check_left_distance();
      else if(cmdValue == "mg")
        magnetSwitch();
    }else if(Length == 3){
      if(cmdValue == "p1u" && pulselen1 < pulselen1Max){
        pulselen1 += 2;
        pwm.setPWM(s1, 0, pulselen1);
      }else if(cmdValue == "p1d" && pulselen1 > pulselen1Min){
        pulselen1 -= 2;
        pwm.setPWM(s1, 0, pulselen1);
      }else if(cmdValue == "p2u" && pulselen2 < pulselen2Max){
        pulselen2 += 2;
        pwm.setPWM(s2, 0, pulselen2);
      }else if(cmdValue == "p2d" && pulselen2 > pulselen2Min){
        pulselen2 -= 2;
        pwm.setPWM(s2, 0, pulselen2);
      }else if(cmdValue == "p3d" && pulselen3 < pulselen3Max){
        pulselen3 += 2;
        pwm.setPWM(s3, 0, pulselen3);
      }else if(cmdValue == "p3u" && pulselen3 > pulselen3Min){
        pulselen3 -= 2;
        pwm.setPWM(s3, 0, pulselen3);
      }else if(cmdValue == "p4u" && pulselen4 < pulselen4Max){
        pulselen4 += 1;
        pwm.setPWM(s4, 0, pulselen4);
      }else if(cmdValue == "p4d" && pulselen4 > pulselen4Min){
        pulselen4 -= 1;
        pwm.setPWM(s4, 0, pulselen4);
      }else if(cmdValue == "pxu" && pulselen2 < pulselen2Max){
        pulselen2 +=2;
        pulselen3 -=2;
        pwm.setPWM(s2, 0, pulselen2);
        pwm.setPWM(s3, 0, pulselen3);
      }else if(cmdValue == "pxd" && pulselen2 > pulselen2Min){
        pulselen2 -=2;
        pulselen3 +=2;
        pwm.setPWM(s2, 0, pulselen2);
        pwm.setPWM(s3, 0, pulselen3);
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
      }else if(cmdValue[0] == 'l'){
        String label = "";
        for(int i=0; i<Length-2;i++){
          label += cmdValue[i+2];
        }
        detectedLabel = label;
        labelCounter++;
        Serial.print(labelCounter);
        Serial.println(detectedLabel);
      }
    }  
}
///******************End Auto Commands*********************///



///********************Servo Func**********************///
void Reset(){
  pulselen1=415;
  pulselen2=150;
  pulselen3=525;
  pulselen4=280;
  pwm.setPWM(s4, 0, pulselen4);
  delay(10);
  pwm.setPWM(s2, 0, pulselen2);
  delay(10);
  pwm.setPWM(s3, 0, pulselen3);
  delay(50);
  pulselen4=350;
  pwm.setPWM(s4, 0, pulselen4);
  delay(200);
  pwm.setPWM(s1, 0, pulselen1);
}
///*******************End Servo Func********************///

///********************Magnet Func**********************///
void magnetSwitch(){
    magnetState = !magnetState;
    digitalWrite(magnet,magnetState);
}
///******************End Magnet Func********************///

///********************Search State**********************///
/*
void searchState(){
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
     previousMillis = currentMillis;
     int distancef = sonar2.ping_cm();
     Stop();
     if(distancef > minDistance && movementFlag != 't'){
        if(currentMillis - forwardTime >= forwardInterval){
          int distancel = sonar1.ping_cm();
          int distancer = sonar3.ping_cm();
          if(distancel >= distancer && movementFlag == 'f' || movementFlag == 'l'){
            if(movementFlag == 'f'){
              leftTime = currentMillis;
              movementFlag = 'l';
            }
            left();
            if(currentMillis - leftTime >= turnInterval){
              forwardTime = currentMillis;
              movementFlag = 'f';
            }
          }else if(distancel < distancer && movementFlag == 'f' || movementFlag == 'r'){
            if(movementFlag == 'f'){
              rightTime = currentMillis;
              movementFlag = 'r';
            }
            right();
            if(currentMillis - rightTime >= turnInterval){
              forwardTime = currentMillis;
              movementFlag = 'f';
            }
          }
        }else{
          forward();
          movementFlag = 'f';  
        }
     }else{
        int distancel = sonar1.ping_cm();
        int distancer = sonar3.ping_cm();
        if(distancel >= distancer && turnFlag == 't' || turnFlag == 'l'){
            if(turnFlag == 't'){
              leftTime = currentMillis;
              turnFlag = 'l';
              movementFlag == 't';
            }
            left();
            if(currentMillis - leftTime >= turnInterval){
              movementFlag = 'f';
              turnFlag = 't';
            }
        }else if(distancel < distancer && turnFlag == 't' || turnFlag == 'r'){
            if(turnFlag == 't'){
              rightTime = currentMillis;
              turnFlag = 'r';
              movementFlag == 't';
            }
            right();
            if(currentMillis - rightTime >= turnInterval){
              movementFlag = 'f';
              turnFlag = 't';
            }
        }
     }
  }
}
*/
void searchState(){
  if(searchStateFlag == 'a'){
    int distance = sonar2.ping_cm();
    if( distance < minDistance ){}
    else armSearch();
    if(circleFlipFlag == 0){
      int distancel = sonar1.ping_cm();
      int distancer = sonar3.ping_cm();
      if(distancer > distancel)
        searchStateFlag = 'r';
      else
        searchStateFlag = 'l';
    }else
      searchStateFlag = 'f';
  }else if(searchStateFlag == 'r'){
    rightCircle();
    if(circleFlipFlag == 1)
      searchStateFlag = 'a';
  }else if(searchStateFlag == 'l'){
    leftCircle();
    if(circleFlipFlag == 1)
      searchStateFlag = 'a';
  }else if(searchStateFlag == 'f'){
    forwardCircle();
    if(circleFlipFlag == 0)
      searchStateFlag = 'a';
  }
  
}
void armSearchUpDown(){
  servoWrite(s4,370,400);
  servoWrite(s4,400,340);
  servoWrite(s4,340,370);
}

void armSearch(){
  armSearchUpDown();
  servoWrite(s1,415,300);
  //armSearchUpDown();
  servoWrite(s1,300,510);
  //armSearchUpDown();
  servoWrite(s1,510,415);
}

void servoWrite(int n , int s , int e){
  if(s<e){
    for(int i = s ; i <= e ; i++){
      pwm.setPWM(n, 0, i);
      delay(armSearchSpeed);
    }  
  }else{
    for(int i = s ; i >= e ; i--){
      pwm.setPWM(n, 0, i);
      delay(armSearchSpeed);
    }
  }
}

void rightCircle(){
  if(circleFlag){
    circleFlag=false;
    circleMillis=millis();
  }
  if(millis() - circleMillis >= circleDelay){
    circleFlag=true;
    circleFlipFlag++;
    Stop();
  }else{
    right();
  }
}

void leftCircle(){
  if(circleFlag){
    circleFlag=false;
    circleMillis=millis();
  }
  if(millis() - circleMillis >= circleDelay){
    circleFlag=true;
    circleFlipFlag++;
    Stop();
  }else{
    left();
  }
}

void forwardCircle(){
  if(forwardCircleFlag){
    forwardCircleFlag=false;
    forwardCircleMillis=millis();
  }
  if(millis() - forwardCircleMillis >= forwardCircleDelay){
    forwardCircleFlag=true;
    circleFlipFlag = 0;
    Stop(); 
  }else{
    int distance = sonar2.ping_cm();
    if( distance < minDistance )
      Stop();
    else
      forward();
  }
}


///******************End Search State*******************///

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
        checkAutoCommands();
        switch(statesFlag){
          case 's':
            digitalWrite(led,LOW);
            Serial.println("SearchState");
            searchState();
            break;
          case 'f':
            digitalWrite(led,HIGH);
            Serial.println("FindeState");
            break;
          default:
            Serial.println(statesFlag);
            break;  
        }
      }else
        checkCommands();
      msgEnd=false;
      cmdValue="";  
    }    
}
