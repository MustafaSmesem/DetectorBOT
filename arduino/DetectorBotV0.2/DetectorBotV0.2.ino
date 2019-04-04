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

void setup() {
  pinMode(12, OUTPUT);
  bluetooth.begin(9600);

  pinMode(ultraTRIG, OUTPUT);
  pinMode(ultraECHO, INPUT);
  
  delay(200); 
  lcd.begin();
  lcd.setCursor(3,0);
  lcd.print("Merhaba Dunya");
  lcd.setCursor(2,2);
  lcd.print("DetectorBOT ben");
  delay(3000);  
}

void loop() {

  if (bluetooth.available()) { 
    lcd.clear();
    lcd.setCursor(0,0);
    while(bluetooth.available()){
      delay(1);
      char msg = bluetooth.read();
      if(msg == '$')
        break;
      lcd.print(msg);
    }
  }
  
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    int distance = sonar.ping_cm();
    bluetooth.print(distance);
    bluetooth.print("cm   ");
  }

}
