char command;
String string;
double limit;

int val;
const int RELAY_ENABLE =4;


//for water flow sensor


byte sensorInterrupt = 0;  // 0 = digital pin 2
byte sensorPin       = 2;

int trig=10;
int echo=11;
long duration;
int distance;
int dist;

// The hall-effect flow sensor outputs approximately 4.5 pulses per second per
// litre/minute of flow.
float calibrationFactor = 4.5;

volatile byte pulseCount;  

float flowRate;
unsigned int flowMilliLitres;
unsigned long totalMilliLitres;
unsigned long oldTime;



  void setup(){
    Serial.begin(9600);
    pinMode(RELAY_ENABLE,OUTPUT);
    pinMode(sensorPin, INPUT);

//    pinMode(buzzer,OUTPUT);
    
    pinMode(trig,OUTPUT);
    pinMode(echo,INPUT);
  }

  void loop(){
    dist=ultrasonic();
    
    
    if (Serial.available() > 0) 
    {string = "";}
    while(Serial.available() > 0)
    {
      command = ((byte)Serial.read());
      
      if(command == ':'){
        break;
      }
     else{
        string += command;
      }
     delay(1);
    }
    if (string=="S"){
      // to setup the values for the water flow sensor
    digitalWrite(sensorPin, HIGH);
   pulseCount        = 0;
  flowRate          = 0.0;
  flowMilliLitres   = 0;
  totalMilliLitres  = 0;
  oldTime           = 0;

  // The Hall-effect sensor is connected to pin 2 which uses interrupt 0.
  // Configured to trigger on a FALLING state change (transition from HIGH
  // state to LOW state)
  attachInterrupt(sensorInterrupt, pulseCounter, FALLING);
  
}
    
    if (string=="W"){
      
      if (2<=dist && dist<=25){
        digitalWrite(RELAY_ENABLE,LOW);

  if((millis() - oldTime) > 1000)    // Only process counters once per second
  { 
    // Disable the interrupt while calculating flow rate and sending the value to
    // the host
    detachInterrupt(sensorInterrupt);
        
    // Because this loop may not complete in exactly 1 second intervals we calculate
    // the number of milliseconds that have passed since the last execution and use
    // that to scale the output. We also apply the calibrationFactor to scale the output
    // based on the number of pulses per second per units of measure (litres/minute in
    // this case) coming from the sensor.
    flowRate = ((1000.0 / (millis() - oldTime)) * pulseCount) / calibrationFactor;
    
    // Note the time this processing pass was executed. Note that because we've
    // disabled interrupts the millis() function won't actually be incrementing right
    // at this point, but it will still return the value it was set to just before
    // interrupts went away.
    oldTime = millis();
    
    // Divide the flow rate in litres/minute by 60 to determine how many litres have
    // passed through the sensor in this 1 second interval, then multiply by 1000 to
    // convert to millilitres.
    flowMilliLitres = (flowRate / 60) * 1000;
    
    // Add the millilitres passed in this second to the cumulative total
    totalMilliLitres += flowMilliLitres;
      
    unsigned int frac;
    


//    if (totalMilliLitres>1000.0){
//      digitalWrite(RELAY_ENABLE,HIGH);
//      
//}
      
    Serial.println(totalMilliLitres);
    
    // Reset the pulse counter so we can start incrementing again
    pulseCount = 0;
    
    // Enable the interrupt again now that we've finished sending output
    attachInterrupt(sensorInterrupt, pulseCounter, FALLING);
  }
}
else{
  digitalWrite(RELAY_ENABLE,HIGH);
  Serial.println(totalMilliLitres);
 }
    }
      
      
      
      if (string=="O"){
        digitalWrite(RELAY_ENABLE,HIGH);     // to close the solenoid valve
    }
      if(string=="C"){
      digitalWrite(RELAY_ENABLE,LOW);       // to open the solenoid valve
      }

if (string=="B"){
   

  digitalWrite(RELAY_ENABLE,LOW);
    if((millis() - oldTime) > 1000)    // Only process counters once per second
  { 
    detachInterrupt(sensorInterrupt);
       
    flowRate = ((1000.0 / (millis() - oldTime)) * pulseCount) / calibrationFactor;
    oldTime = millis();
    flowMilliLitres = (flowRate / 60) * 1000;
    
    // Add the millilitres passed in this second to the cumulative total
    totalMilliLitres += flowMilliLitres;
      
    unsigned int frac;
    if (totalMilliLitres>500.0){
      digitalWrite(RELAY_ENABLE,HIGH);
      string="O";
     }
      
   Serial.println(totalMilliLitres);
    pulseCount = 0;
    attachInterrupt(sensorInterrupt, pulseCounter, FALLING);
  }
}
delay(200);
 
}
  
void pulseCounter()
{
  pulseCount++;
} 
int ultrasonic(){
  digitalWrite(trig,LOW);
delayMicroseconds(2);

digitalWrite(trig,HIGH);
delayMicroseconds(10);
digitalWrite(trig,LOW);

duration=pulseIn(echo,HIGH);

distance=(0.034*duration)/2;
return distance;
}

    
