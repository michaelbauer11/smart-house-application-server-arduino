package com.example.owner.smarthouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    LinearLayout linearmain;
    Connection connection;
    CustomizationButton currentButton;
    CustomSwitch customSwitch;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        //Log.d("-----------------",intent.getStringExtra("key"));
        Session session = ((Session)getApplicationContext());
        //session.connection.start();
        session.connection.setMain2Activity(this);
        //final LinearLayout linearmain = (LinearLayout) findViewById(R.id.linear_main2);

        //final TextView username = new TextView(getApplicationContext());
        //username.setText("choose username");
        //linearmain.addView(username);
        /*else{
            final String jsonStringed = intent.getStringExtra("stringed json");
            try {
                SetDevices(new JSONObject(jsonStringed));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    }
    /*public int devicesNum(JSONObject jsonObject){
        int devicesNum = 0;
        Iterator<?> keys = jsonObject.keys();
        while( keys.hasNext() ) {
            String key = (String) keys.next();
            devicesNum++;
        }
        return devicesNum;
    }*/

    //tells the server to disconnect from the phone when application closed
    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        connection.send("disconnect#");
    }
    */




    public void SetDevices(JSONObject jsonObject) throws JSONException {
        linearmain = (LinearLayout) findViewById(R.id.linear_main2);
        this.jsonObject = jsonObject;
        final List<CustomSwitch> switches = new ArrayList<>();
        final List<CustomizationButton> buttons = new ArrayList<>();
        Log.d("---------json---------", jsonObject.toString());
        //String[] msgSplit = msg.split("#");
        //String devicesNum = msgSplit[0]
        String name;
        Iterator<?> keys = jsonObject.keys();
        for (int id=0;keys.hasNext(); id++) {
            String key = (String) keys.next();
            Log.d(key +"+++++++++",key);
            switches.add(new CustomSwitch(jsonObject.getJSONObject(key),key, id+1, getApplicationContext()));
            buttons.add(new CustomizationButton(jsonObject, key, id+1, getApplicationContext(), this,switches.get(id)));
            customSwitch = switches.get(id);
            Log.d(customSwitch.getText().toString(),"----------------");
            this.runOnUiThread(new Runnable() {
                public void run() {
                    linearmain.addView(customSwitch);
                }
            });
            currentButton = buttons.get(id);
            Log.d("currentButton", currentButton.toString());
            this.runOnUiThread(new Runnable() {
                public void run() {
                    linearmain.addView(currentButton);
                    //linearmain.removeView(currentButton);
                }
            });

        }
    }


}
