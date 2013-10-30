/**
 *
 * RFID
 * MOSI: Pin 11 / ICSP-4
 * MISO: Pin 12 / ICSP-1
 * SCK: Pin 13 / ISCP-3
 * NSS: Pin 10
 * RST: Pin 8
 * VCC: 3.3V
 *
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
#include <RFID.h>
#include <LiquidCrystal_I2C.h>

// LED pins
#define LED_GREEN 5
#define LED_RED 6
#define LED_BLUE 3

// Servo motor pin
#define SERVO_PIN 9

// RFID reader pins
#define SS_PIN 10
#define RST_PIN 8

boolean setup_done = false;
Servo lock;
LiquidCrystal_I2C lcd(0x27,20,4);
RFID rfid(SS_PIN, RST_PIN); 
int serNum0=0; // TODO add correct infos
int serNum1=0;
int serNum2=0;
int serNum3=0;
int serNum4=0;

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

  if (!setup_done)
  {
    if(Serial.available()) {
      delay(100);
      while (Serial.available() > 0) {
        if(Serial.read() == 's') {
          setup_done = true;
          lcd.clear();
          lcd.setCursor(0,0);
          lcd.print("Loading");
          lcd.setCursor(0,1);
          lcd.print("complete");
          //analogWrite(LED_RED, 0);
          analogWrite(LED_GREEN, 255);
          delay(500);
          analogWrite(LED_GREEN, 0);
          delay(500);
          analogWrite(LED_GREEN, 255);
          delay(500);
          analogWrite(LED_GREEN, 0);
          delay(500);
          lcd.noDisplay();
          lcd.noBacklight();
        }
      }
    }
  } else {

    // check if standard rfid is requested
    if (rfid.isCard()) {
      if (rfid.readCardSerial()) {
        lcd.clear();
        lcd.backlight();
        lcd.display();
        lcd.setCursor(0,0);
        lcd.print("Reading,");
        lcd.setCursor(0,1);
        lcd.print("please wait...");
        if (rfid.serNum[0] == serNum0
            && rfid.serNum[1] == serNum1
            && rfid.serNum[2] == serNum2
            && rfid.serNum[3] == serNum3
            && rfid.serNum[4] == serNum4
          ) {
            // correct card
            delay(500);
            lcd.clear();
            lcd.setCursor(0,0);
            lcd.print("Access");
            delay(100);
            lcd.setCursor(0,1);
            lcd.print("granted");
            analogWrite(LED_GREEN, 255);
            //Serial.println("1");
            delay(300);
            analogWrite(LED_GREEN, 0);
            delay(300);
            analogWrite(LED_GREEN, 255);
            delay(300);
            analogWrite(LED_GREEN, 0);
            // TODO open 'lock'
            delay(2000);
            // TODO close 'lock'
            lcd.noDisplay();
            lcd.noBacklight();
         } else {
            // unknown card
            delay(500);
            lcd.clear();
            lcd.setCursor(0,0);
            lcd.print("Access");
            delay(100);
            lcd.setCursor(0,1);
            lcd.print("denied");
            analogWrite(LED_RED, 255);
            delay(300);
            analogWrite(LED_RED, 0);
            delay(300);
            analogWrite(LED_RED, 255);
            delay(300);
            analogWrite(LED_RED, 0);
            delay(300);
            //Serial.println("0");
            delay(2000);
            lcd.noDisplay();
            lcd.noBacklight();
          }
        }
      }

      // check if server want to communicate
      if(Serial.available()) {
      delay(100);
      while (Serial.available() > 0) {
        if(Serial.read() == 'o') {
          // read user
        }
      }
    }
  }

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

  //rfid.halt();
  delay(500);
}


