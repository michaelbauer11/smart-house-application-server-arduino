package com.example.owner.smarthouse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Main3Activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        try {
            LinearLayout linearmain3 = (LinearLayout) findViewById(R.id.linear_main3);
            Intent myIntent = getIntent();
            String jsonStringed = myIntent.getStringExtra("stringed json");
            final String key = myIntent.getStringExtra("key");
            final JSONObject generalJson = new JSONObject(jsonStringed);
            final Session session = ((Session) getApplicationContext());

            //all the device information :
            //device's name:
            TextView deviceNameTextView = new TextView(getApplicationContext());
            deviceNameTextView.setText("device's name: ");
            deviceNameTextView.setTextColor(Color.BLACK);
            final EditText deviceName = new EditText(getApplicationContext());
            Log.d("key main cativit3", key);
            Log.d("json main cativit3", generalJson.toString());
            deviceName.setText(generalJson.getJSONObject(key).get("name").toString());
            Log.d("device name ---------- ", deviceName.getText().toString());
            deviceName.setTextColor(Color.BLACK);
            linearmain3.addView(deviceNameTextView);
            linearmain3.addView(deviceName);

            //according temperature outside
            TextView tempOutsideTextView = new TextView(getApplicationContext());
            tempOutsideTextView.setText("operate device when the degrease outside is over:(if don't want this function enter 'none')");
            tempOutsideTextView.setTextColor(Color.BLACK);
            //tempOutsideTextView.set
            final EditText tempOutside = new EditText(getApplicationContext());
            tempOutside.setText(generalJson.getJSONObject(key).get("temp_activate_outside").toString());
            tempOutside.setTextColor(Color.BLACK);
            linearmain3.addView(tempOutsideTextView);
            linearmain3.addView(tempOutside);

            Button dairyButton = new Button(getApplicationContext());
            dairyButton.setText("set turning on dairy");
            dairyButton.setTextColor(Color.BLACK);
            linearmain3.addView(dairyButton);
            dairyButton.setOnClickListener((new View.OnClickListener() {
                public void onClick(View v) {
                    Intent newIntent = new Intent(Main3Activity.this, Main6Activity.class);
                    try {
                        generalJson.getJSONObject(key).put("name", String.valueOf(deviceName.getText()));
                        generalJson.getJSONObject(key).put("temp_activate_outside", String.valueOf(tempOutside.getText()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    newIntent.putExtra("key", key);
                    newIntent.putExtra("stringed json", generalJson.toString()); //Optional parameters
                    finish();
                    startActivity(newIntent);
                }
            }));

            Button savingChangeButton = new Button(getApplicationContext());
            savingChangeButton.setText("save_changes");
            linearmain3.addView(savingChangeButton);
            savingChangeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String msg = "";
                    String newName = String.valueOf(deviceName.getText());
                    String newTempOutSide = String.valueOf(tempOutside.getText());
                    try {
                        generalJson.getJSONObject(key).put("name", newName);
                        generalJson.getJSONObject(key).put("temp_activate_outside", newTempOutSide);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    msg += "update#";
                    msg += generalJson;
                    try {
                        session.connection.send(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent newIntent = new Intent(Main3Activity.this, Main4Activity.class);
                    newIntent.putExtra("fromclass", "mainActivity3");
                    newIntent.putExtra("stringed json", generalJson.toString()); //Optional parameters
                    startActivity(newIntent);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }




}

