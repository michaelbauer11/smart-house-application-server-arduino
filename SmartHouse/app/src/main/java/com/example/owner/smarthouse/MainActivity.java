package com.example.owner.smarthouse;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    LinearLayout linearmain;
    Connection connection;
    CustomizationButton currentButton;
    CustomSwitch customSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            connection = new Connection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.start();
        Session session = ((Session) getApplicationContext());
        connection.setMain1Activity(this);
        session.setConnection(connection);
        //Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
        //MainActivity.this.startActivity(myIntent);

        LinearLayout linearmain = (LinearLayout) findViewById(R.id.linear_main1);

        TextView headline = new TextView(getApplicationContext());
        headline.setText("sign in");
        headline.setTextColor(Color.BLACK);
        linearmain.addView(headline);

        final EditText username = new EditText(getApplicationContext());
        username.setText("Enter your username");
        username.setTextColor(Color.BLACK);
        linearmain.addView(username);
        username.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                username.setText("");
            }
        });

        final EditText password = new EditText(getApplicationContext());
        password.setText("Enter your password");
        password.setTextColor(Color.BLACK);
        linearmain.addView(password);
        password.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                password.setText("");
            }
        });

        Button b = new Button(getApplicationContext());
        b.setText("Enter");
        linearmain.addView(b);
        b.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                String request = "";
                request += "signin#";
                request += username.getText()+"#";
                //hash password by SHA-256
                /*MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                byte[] hashPassword = digest.digest(password.getText().toString().getBytes(StandardCharsets.UTF_8));
                request += hashPassword;*/

                request += password.getText();
                try {
                    connection.send(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Button b2 = new Button(getApplicationContext());
        b2.setText("sign up");
        linearmain.addView(b2);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Main5Activity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }





    public void identityVerified(boolean bool){
        if(bool){
            Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
            MainActivity.this.startActivity(myIntent);
        }else{
            Intent currentIntent = getIntent();
            finish();
            startActivity(currentIntent);
        }
    }

}