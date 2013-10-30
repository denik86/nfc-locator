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
 * VCC: 5V
 */

#include <Servo.h>
#include <RFID.h>
#include <LiquidCrystal_I2C.h>
#include <Wire.h>
#include <SPI.h>

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
int serNum0=130;
int serNum1=191;
int serNum2=30;
int serNum3=205;
int serNum4=238;
char user[11];

void setup() {
  //initialize RFID reader
  SPI.begin();
  rfid.init();
  
  // initialize serial communication
  Serial.begin(9600);
  
  // initialize servo motor
  lock.attach(SERVO_PIN);
  lock.write(0);
  
  // initialize lcd
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
            // open lock
            lock.write(180);
            delay(2000);
            // close lock
            lock.write(0);
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
            // DEBUG
            Serial.println(rfid.serNum[0]);
            Serial.println(rfid.serNum[1]);
            Serial.println(rfid.serNum[2]);
            Serial.println(rfid.serNum[3]);
            Serial.println(rfid.serNum[4]);
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
          int i=0;
          while(Serial.available() > 0 && i < 10) {
            user[i] = Serial.read();
            i++;
          }
          // DEBUG try to send user back
          //int j=0;
          //while(j < i) {
            //Serial.write(user[j]);
            //j++;
          //}
          
          // open
          lcd.clear();
          lcd.backlight();
          lcd.display();
          lcd.setCursor(0,0);
          lcd.print("user ");
          int j=0;
          while(j < i) {
            lcd.print(user[j]);
            j++;
          }
          delay(100);
          lcd.setCursor(0,1);
          lcd.print("access granted");
          analogWrite(LED_GREEN, 255);
          //Serial.println("1");
          delay(300);
          analogWrite(LED_GREEN, 0);
          delay(300);
          analogWrite(LED_GREEN, 255);
          delay(300);
          analogWrite(LED_GREEN, 0);
          // open lock
          lock.write(180);
          delay(2000);
          // close lock
          lock.write(0);
          lcd.noDisplay();
          lcd.noBacklight();
        }
      }
    }
  }

  delay(500);
}


