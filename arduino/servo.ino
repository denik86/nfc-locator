/**
 * Monitor
 * SDA: Pin A4
 * SCL: Pin A5
 * Vcc: 5V
 *
 * LED RGB
 * R: Pin 3
 * G: Pin 5
 * B: Pin 6
 *
 * SERVO MOTOR
 * Pin 9
*/

#include <Servo.h>
#include <LiquidCrystal_I2C.h>

#define LED_GREEN 5
#define LED_RED 6
#define LED_BLUE 3
#define SERVO_PIN 9

Servo lock;
LiquidCrystal_I2C lcd(0x27,20,4);

int pos = 0;

void setup() {
  Serial.begin(9600);
  lock.attach(SERVO_PIN);
  lcd.init();
  lcd.backlight();
  lcd.setCursor(0,0);
  lcd.print("Loading...");
}

void loop() {

  

  /*
  for(pos = 0; pos < 180; pos += 1)
  {
    lock.write(pos);
    delay(15);
  }
  for(pos = 180; pos >= 1; pos -= 1)
  {
    lock.write(pos);
    delay(15);
  }*/
}


