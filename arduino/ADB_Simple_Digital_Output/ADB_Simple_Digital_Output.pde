#include <SPI.h>
#include <Adb.h>

#define  LEDcount  3

Connection * connection;

long lastTime;

//Array of state of LEDs
uint8_t LEDState[LEDcount];

// Event handler for shell connection; called whenever data sent from Android to Microcontroller
void adbEventHandler(Connection * connection, adb_eventType event, uint16_t length, uint8_t * data)
{
  // In this example Data packets contain three bytes: one for the state of each LED
  if (event == ADB_CONNECTION_RECEIVE)
  {
    int i;
    for (i = 0; i < LEDcount; i++)
    {
      if(LEDState[i] != data[i])
      {
        digitalWrite(i+2, data[i]);   // Change the state of LED 
        Serial.println(data[i],DEC);   // Output debugging to serial
        LEDState[i] = data[i];          // Store the State of LED
      }	
    }
  }
}

void setup()
{
  int i;
  for (i = 0; i < LEDcount; i++)
  {
    pinMode(i+2,OUTPUT); // Set pins as output
    LEDState[i] = 0; // Init state to 0
  }  
  // Init serial port for debugging
  Serial.begin(57600);

  // Init the ADB subsystem.  
  ADB::init();

  // Open an ADB stream to the phone's shell. Auto-reconnect. Use any unused port number eg:4568
  connection = ADB::addConnection("tcp:4568", true, adbEventHandler);  
}

void loop()
{
  // Poll the ADB subsystem.
  ADB::poll();
}

