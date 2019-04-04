///**************Libraries****************///
#include <SoftwareSerial.h>
#include <NewPing.h>
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

SoftwareSerial bluetooth(2, 4); // RX, TX

unsigned long previousMillis = 0;
const long interval = 500;           


///***Movement Motors***///
int rf=11;
int rb=10;
int lf=9;
int lb=8;
///********************///

int megnatis = 46;
boolean isMegnatis = false;

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
  digitalWrite(ultraVCC1, HIGH);
///****************************///

  pinMode(megnatis, OUTPUT);
  digitalWrite(megnatis,LOW);
  
  Serial.begin(115200);
  bluetooth.begin(9600);
  delay(3000);  
}

/***DetectorBOT messages protocol***
 * **Recive Messages
 *  *function_letter$param1$param2$...paramN$#
 * **Send Messages
 *  *distance1$distance2$distance3$distance4$#
 */

/***Functions Letter***
 * getDistance(int){D$sensorNumber$#  => int#}
 * **MOVEMENTS** 
 *  *stop(){s#}
 *  *forward(){f#}
 *  *backward(){b#}
 *  *left(){l#}
 *  *right(){r#}
 * **states**
 *  *searchState{sh#}
 *  *findState{fi#}
 *  *trackingState{tr#}
 */


void loop() {

  if (bluetooth.available()) { 

    while(bluetooth.available()){
      delay(1);
      char msg = bluetooth.read();
      if(msg == '$')
        break;
    }
  }
  
  check_front_distance();
}



///*****************DISTANCE FUNCTIONS**********************///
void getDistance(int n){
  int distance=0;
  switch(n){
    case 1:
      distance = sonar1.ping_cm();
      break;
    case 2:
      digitalWrite(ultraVCC2, HIGH);
      distance = sonar2.ping_cm();
      digitalWrite(ultraVCC2, LOW);
      break;
    case 3:
      digitalWrite(ultraVCC3, HIGH);
      distance = sonar3.ping_cm();
      digitalWrite(ultraVCC3, LOW);
      break;
    case 4:
      distance = sonar4.ping_cm();
      break;
    default:
      break;  
  }
  bluetooth.print(distance);
  bluetooth.print("#");
}

void check_front_distance(){
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    int distance = sonar1.ping_cm();
    bluetooth.print(distance);
    bluetooth.print("#");
  }
}

void check_arm_distance(){
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    int distance = sonar4.ping_cm();
    bluetooth.print(distance);
    bluetooth.print("#");
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

void forward(int Speed){
  analogWrite(rb,0);
  analogWrite(lb,0);
  analogWrite(rf,Speed);
  analogWrite(lf,Speed);
}

void backward(int Speed){
  analogWrite(rf,0);
  analogWrite(lf,0);
  analogWrite(rb,Speed);
  analogWrite(lb,Speed);
}

void right(int Speed){
  analogWrite(rf,Speed);
  analogWrite(lf,0);
  analogWrite(rb,0);
  analogWrite(lb,Speed);
}

void left(int Speed){
  analogWrite(rf,0);
  analogWrite(lf,Speed);
  analogWrite(rb,Speed);
  analogWrite(lb,0);
}
///*****************END MOVEMENTS FUNCTIONS*****************///
