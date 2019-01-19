#include <Ethernet.h>
#include <SPI.h>


byte mac[] = { 0x00, 0xAA, 0xBB, 0xCC, 0xDE, 0x00  };
byte ip[] = { 192, 168, 1, 14 };
byte server[] = { 192, 168, 43, 66 }; 
//byte server[] = { 127, 0, 0, 1 }; 
//IPAddress ip(192,168,0, 50);
//IPAddress gateway(192,168,1, 1);
//IPAddress subnet(255, 255, 255, 0);
//IPAddress server = (192,168,0, 1);
EthernetClient client;
int input = 9;
int output = 3;
byte deviceStatus = 0; // 0 - means off, 1 - means on 

void connectToServer(){
   Serial.println("connecting...");
   delay(1000);
  bool connectSuccess = false;
  while(connectSuccess==false){
   if(client.connect(server, 8820)) {
     Serial.println("connected to sever");    
     client.write(deviceStatus);
     connectSuccess = true;
  }else {
     Serial.println("connection to sever failed");
     client.stop();
     delay(5000); 
  }
 } 
}

void setup()
{
  Serial.begin(9600);
  Serial.println("before");
  Ethernet.begin(mac);
  Serial.println("after");
  delay(1000);
  pinMode(output, OUTPUT);
  pinMode(input, INPUT);
  digitalWrite(output, LOW);
  deviceStatus = 0; 
}

void loop()
{
   if (!client.connected()){
    connectToServer();
   }
   if(client.available()>0) {
     Serial.println("client avaliable");
     char c = client.read();
     Serial.println(c);
    if(c == '1' ){
      Serial.println("turn on");
      digitalWrite(output, HIGH); 
      deviceStatus = 1;
    }if(c == '0'){
      Serial.println("turn off");
      digitalWrite(output, LOW);
      deviceStatus = 0;
    }
  }
}
