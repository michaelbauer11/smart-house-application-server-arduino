package com.example.owner.smarthouse;

/**
 * Created by Owner on 24/02/2017.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection extends Thread {
    final String serverAddress = "10.7.132.197";
    private int serverPort = 20000;
    private Socket socket;
    public BufferedReader in;
    public PrintWriter out;
    Main2Activity activity2;
    MainActivity activity1;
    Main5Activity activity5;

    public Connection() throws IOException {
        // Make connection and initialize streams

    }
    public void setMain5Activity(Main5Activity mA){
        this.activity5 = mA;
    }

    public void setMain2Activity(Main2Activity mA){
        this.activity2 = mA;
    }

    public void setMain1Activity(MainActivity mA){
        this.activity1 = mA;
    }
    @Override
    public void run() {
        try {
            Log.d("++++++++++++++++++++", "hear");
            socket = new Socket(serverAddress, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while(true) {
                String msg = "";
                Log.d("++++++++++++++++++++", "Start");
                msg = in.readLine();
                Log.d("++++++++++++++++++++", "Start");
                //while ((msg = in.readLine()) != null) {
                Log.d("+++++++++++++++++", msg);
                if (msg.length()>0 && msg.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(msg);
                    Log.d("json stringed",jsonObject.toString());
                    activity2.SetDevices(jsonObject);
                }
                if(msg.length()>0 &&  msg.charAt(0) == 'c') {
                    activity1.identityVerified(true);
                    //String[] msgSplited = msg.split("#");
                    //JSONObject jsonObject = new JSONObject(msgSplited[1]);
                    //activity1.SetDevices(jsonObject);
                }
                if(msg.length()>0 && msg.charAt(0) == 'u') activity1.identityVerified(false);
                if(msg.length()>0 && msg.charAt(0) == 'v') activity5.identityIsOk(true);
                if(msg.length()>0 && msg.charAt(0) == 'x') activity5.identityIsOk(false);
            }
            } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    public void setPort(int p){
        this.serverPort = p;
    }

    public BufferedReader getIn(){
        return  in;
    }

    public String getInputStream(){
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ain't receive";
    }
    public void send(String command) throws IOException{
        out = new PrintWriter(socket.getOutputStream(), true);
        out.println(command);
    }
}
