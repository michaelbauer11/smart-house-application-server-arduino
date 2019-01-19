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

public class Main4Activity extends AppCompatActivity {

    LinearLayout linearmain4;
    Connection connection;
    CustomizationButton currentButton;
    CustomSwitch customSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Intent currentIntent = new Intent(getIntent());
        try {
            JSONObject updatedJson = new JSONObject(currentIntent.getStringExtra("stringed json"));
            SetUpdatedDevices(updatedJson);
        }catch (JSONException e){ e.printStackTrace();}

        //Log.d("-",currentIntent.getStringExtra("stringed json"));

    }

    private void SetUpdatedDevices(JSONObject jsonObject) throws JSONException {
        linearmain4 = (LinearLayout) findViewById(R.id.linear_main4);
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
            buttons.add(new CustomizationButton(jsonObject, key, id+1, getApplicationContext(), this, switches.get(id)));
            customSwitch = switches.get(id);
            Log.d(customSwitch.getText().toString(),"----------------");
            this.runOnUiThread(new Runnable() {
                public void run() {
                    linearmain4.addView(customSwitch);
                }
            });
            currentButton = buttons.get(id);
            this.runOnUiThread(new Runnable() {
                public void run() {
                    linearmain4.addView(currentButton);
                }
            });

        }
    }




}
