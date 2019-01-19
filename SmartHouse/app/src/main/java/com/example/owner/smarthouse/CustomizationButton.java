package com.example.owner.smarthouse;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Owner on 13/04/2017.
 *
 */
public class CustomizationButton extends Button {

    private String key;
    private JSONObject jsonOb;
    private int id;
    public <Activity extends AppCompatActivity> CustomizationButton(final JSONObject jsonOb, final String key, int id, Context context, final Activity mainActivity, final CustomSwitch cS) {
        super(context);
        this.key = key;
        this.jsonOb = jsonOb;
        this.id = id;
        String name = "";
        try {
            name = jsonOb.getJSONObject(key).get("name").toString();
            Log.d("json!!!!!!!!", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.setText(name + " - advanced options");
        this.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(mainActivity, Main3Activity.class);
                try {
                    jsonOb.getJSONObject(key).put("status", (cS.isChecked())?1:0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                myIntent.putExtra("stringed json", jsonOb.toString()); //Optional parameters
                myIntent.putExtra("key", key); //Optional parameters
                mainActivity.startActivity(myIntent);////// problem probably hearrrr!!!!!!!!!
            }
        });
    }

    public String toString(){
        return "key "+this.key+" id:"+this.id+ "json:"+this.jsonOb.toString();
    }


}
