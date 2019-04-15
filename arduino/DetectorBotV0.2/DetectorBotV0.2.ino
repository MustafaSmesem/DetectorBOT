///**************Libraries****************///
#include <NewPing.h>
#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
///**************************************///


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
///**************************************************///


unsigned long previousMillis = 0;
const long interval = 500;           


///***Movement Motors***///
int rb=11;
int rf=10;
int lb=9;
int lf=8;
int motorSpeed = 100;
int motorAngel = 120;
///***End Movement Motors***///

///********Megnatis*********///
int megnatis = 46;
boolean isMegnatis = false;
///******End Megnatis*******///

///*********MSG*********///
char msg;
String cmdValue="";
boolean msgEnd = false;
///******End MSG********///

///********Servos*********///
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
#define pulselen4Rst 280

int s1 = 0;
int s2 = 2;
int s3 = 4;
int s4 = 6;
int servoSpeed = 1;

uint16_t pulselen1;  
uint16_t pulselen2;
uint16_t pulselen3;
uint16_t pulselen4;

Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();
///********End Servos*********///

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
  digitalWrite(ultraVCC2, HIGH);
///****************************///

  pinMode(megnatis, OUTPUT);
  digitalWrite(megnatis,LOW);

  pwm.begin();
  pwm.setPWMFreq(60);
  Serial.begin(115200);
  Serial3.begin(9600);
  delay(3000);
  Reset();  
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
 * ****
 */


void loop() {
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
    checkCommands();   
    msgEnd=false;
    cmdValue="";  
  }

  
  
  check_front_distance();
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
  int result = motorSpeed + 20;
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

///***************Commands*********************///
void checkCommands(){
  int Length = cmdValue.length();
  if(Length == 1){
      if(cmdValue == "0")
        motorSpeed = 0;
      else if(cmdValue == "1")
        motorSpeed = 80;
      else if(cmdValue == "2")
        motorSpeed = 90;
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
    }else if(Length == 2){
      if(cmdValue == "ss")
        motorSpeed = 0;
      else if(cmdValue == "sf")
        motorSpeed = 80;
      else if(cmdValue == "st")
        motorSpeed = 0;
      else if(cmdValue == "sl")
        motorSpeed = 80;
      else if(cmdValue == "dr")
        check_right_distance();
      else if(cmdValue == "dl")
        check_left_distance();
      else if(cmdValue == "lb")
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
///******************End Commands*********************///

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
