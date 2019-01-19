package com.example.owner.smarthouse;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Main6Activity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button button;
    TextView from_to;
    TimePicker timePicker;
    ArrayList<String> dairy = new ArrayList<>();
    String addedtime ="";
    String jsonStringed;
    String key;
    JSONObject generalJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        try {
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        jsonStringed = intent.getStringExtra("stringed json");
        generalJson = new JSONObject(jsonStringed);
        String s = generalJson.getJSONObject(key).get("turning_on_diary").toString();
        dairy.addAll(Arrays.asList(s.split(",")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //for(int i=0;i<7;i++) dairy.add("");
        Log.d("dairy", dairy.toString().replace("[","").replace("]",""));
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        button = (Button) findViewById(R.id.button);
        from_to = (TextView) findViewById(R.id.from_to);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onClickOnButton(View v) throws InterruptedException {
        if(button.getText().equals("next")){
            if(checkUser()) {
                int minute = timePicker.getMinute();
                Log.d("minute recieved",String.valueOf(minute));
                String minuteFixed;
                if(minute==0) minuteFixed = "00";
                else minuteFixed = (minute < 10) ? "0" + String.valueOf(minute)   : String.valueOf(minute);
                Log.d("minute returned",minuteFixed);
                int hour = timePicker.getHour();
                String hourFixed = String.valueOf(hour);
                if(hour==0) hourFixed = "00";

                Toast.makeText(getBaseContext(), "from - " + hourFixed + ":" + minuteFixed, Toast.LENGTH_LONG).show();
                from_to.setText("to:");
                button.setText("add to dairy");

                addedtime = hourFixed + minuteFixed + "-";
                timePicker.setHour(00);
                timePicker.setMinute(00);
            } else Toast.makeText(getBaseContext(), "you must pick a day first", Toast.LENGTH_LONG).show();
        }
        else{
            int minute = timePicker.getMinute();
            String minuteFixed;
            if(minute==0) minuteFixed = "00";
            else minuteFixed = (minute < 10) ? "0" + String.valueOf(minute) : String.valueOf(minute);
            int hour = timePicker.getHour();
            String hourFixed = String.valueOf(hour);
            if(hour==0) hourFixed = "00";

            Toast.makeText(getBaseContext(),"to - "+hourFixed+":"+minuteFixed,Toast.LENGTH_LONG).show();
            addedtime += String.valueOf(hourFixed+String.valueOf(minuteFixed));
            int dateIndex = getDateChosenIndex();
            Log.d("dairy", dairy.toString().replace("[","").replace("]",""));
            Log.d("getDAteChosen", String.valueOf(dateIndex));
            dairy.set(dateIndex,dairy.get(dateIndex)+addedtime+".");
            addedtime = "";
            Toast.makeText(getBaseContext(),"make more hours or press continue",Toast.LENGTH_LONG).show();
            button.setText("next");
            from_to.setText("from:");
            Log.d("dairy", dairy.toString().replace("[","").replace("]",""));
            timePicker.setHour(00);
            timePicker.setMinute(00);
        }
    }



    //check if user pick up a day
    public boolean checkUser(){
        try {
            int radioButtonId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(radioButtonId);
            Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
            return true;
        }catch (Exception e){
               return false;
        }
    }

    public void rbclick(View v){
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioButtonId);
        Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
    }


    public int getDateChosenIndex(){
        //return the date choosed index
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioButtonId);

        //Toast.makeText(getBaseContext(),radioButton.getText(),Toast.LENGTH_LONG).show();
        String date = radioButton.getText().toString();
        switch (date) {
            case "sunday": return  0;
            case  "monday": return 1;
            case  "tuesday": return 2;
            case  "wednesday": return 3;
            case  "thursday": return 4;
            case  "friday": return 5;
            case  "saturday": return 6;
        }
        return 0;
    }

    public void onClickContinue(View v) throws JSONException{
        generalJson.getJSONObject(key).put("turning_on_diary",dairy.toString().replace("[","").replace("]",""));
        Intent newIntent = new Intent(Main6Activity.this, Main3Activity.class);
        newIntent.putExtra("stringed json",generalJson.toString());
        newIntent.putExtra("key",key);
        startActivity(newIntent);
    }





}
