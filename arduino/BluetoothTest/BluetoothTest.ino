#include <LiquidCrystal_I2C.h>
#include <SoftwareSerial.h>
#include <NewPing.h>

#define ultraTRIG 9
#define ultraECHO 10
#define max_distance 400
NewPing sonar(ultraTRIG, ultraECHO, max_distance);
SoftwareSerial bluetooth(2, 4); // RX, TX
LiquidCrystal_I2C lcd(0x3f,20,4);
unsigned long previousMillis = 0;
const long interval = 1000;           
char msg;

int redLed=5;
int greenLed=6;
int blueLed=7;

void setup() {
  pinMode(redLed, OUTPUT);
  pinMode(greenLed, OUTPUT);
  pinMode(blueLed, OUTPUT);
  digitalWrite(redLed, LOW);
  digitalWrite(greenLed, LOW);
  digitalWrite(blueLed, LOW);
  bluetooth.begin(9600);

  pinMode(ultraTRIG, OUTPUT);
  pinMode(ultraECHO, INPUT);
  
  delay(200); // wait for voltage stabilize
  lcd.begin();
  lcd.setCursor(3,0);
  lcd.print("Merhaba Dunya");
  lcd.setCursor(2,2);
  lcd.print("DetectorBOT ben");
  delay(3000); // wait for settings to take affect. 
}

void loop() {
  if (bluetooth.available()) { // check if anything in UART buffer 
      lcd.clear();
      lcd.setCursor(0,0);
    while(bluetooth.available()){
      delay(1);
      msg= bluetooth.read();
      if(msg == '#'){
        break;  
      }else if(msg=='-'){
          digitalWrite(redLed, HIGH);
      }else if(msg=='/'){
          digitalWrite(greenLed, LOW);
          digitalWrite(blueLed, HIGH);
      }else if(msg=='*'){
          digitalWrite(greenLed, HIGH);
          digitalWrite(blueLed, LOW);
      }else if(msg=='!'){
          digitalWrite(redLed, LOW);
      }else{
          lcd.print(msg);
      }    
    }
  }
  
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    int distance = sonar.ping_cm();
    bluetooth.print(distance);
    bluetooth.print("cm   ");
    if(!bluetooth.available()){
      digitalWrite(greenLed, LOW);
      digitalWrite(blueLed, LOW);
    }
  }

}
