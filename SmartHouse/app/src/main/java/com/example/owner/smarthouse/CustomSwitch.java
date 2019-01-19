package com.example.owner.smarthouse;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Owner on 20/04/2017.
 */
public class CustomSwitch extends Switch {

    public CustomSwitch(JSONObject jsonOb, String key, final int id, Context context){
        super(context);
        final Session session = ((Session)context);
        String name = "";
        String status = "0";
        Log.d("key in button", key);
        try {
            name = jsonOb.get("name").toString();
            Log.d("json!!!!!!!!", name);
            status = jsonOb.get("status").toString();
            Log.d("json!!!!!!!!", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //switch info
        this.setId(id);
        this.setText(name);
        this.setTextColor(Color.BLACK);
        String[] statusArr =  status.split("/");
        //boolean state = (statusArr[0].equals("0"))?false:true;
        boolean state = !status.trim().equals("0");
        Log.d("state111", statusArr[0]);
        Log.d("state", String.valueOf(state));
        this.setChecked(state);
        this.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String res = isChecked ? "1" : "0";
                String msg = "device"+id+"#" + res;
                try {
                    session.connection.send(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }



}

