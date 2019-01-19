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

public class Main5Activity extends AppCompatActivity {

    private TextView username;
    private TextView password;
    private TextView secretKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        final Session session = ((Session)getApplicationContext());
        session.connection.setMain5Activity(this);
        final LinearLayout linearmain = (LinearLayout) findViewById(R.id.linear_main5);

        username = new TextView(getApplicationContext());
        username.setText("choose username");
        final EditText choseUserName = new EditText(getApplicationContext());
        choseUserName.setTextColor(Color.BLACK);

        password = new TextView(getApplicationContext());
        password.setText("choose password");
        final EditText chosePassword = new EditText(getApplicationContext());
        chosePassword.setTextColor(Color.BLACK);

        secretKey = new TextView(getApplicationContext());
        secretKey.setText("insert secretkey");
        final EditText choseSecretKey= new EditText(getApplicationContext());
        choseSecretKey.setTextColor(Color.BLACK);

        linearmain.addView(username);
        linearmain.addView(choseUserName);
        linearmain.addView(password);
        linearmain.addView(chosePassword);
        linearmain.addView(secretKey);
        linearmain.addView(choseSecretKey);

        Button b = new Button(getApplicationContext());
        b.setText("sign up");
        linearmain.addView(b);
        b.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                String request = "";
                request += "signup#";
                request += choseUserName.getText()+"#";
                /*MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                byte[] hashPassword = digest.digest(password.getText().toString().getBytes(StandardCharsets.UTF_8));
                request += hashPassword;*/
                request += chosePassword.getText()+"#";
                request += choseSecretKey.getText()+"#";
                try {
                    session.connection.send(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Intent myIntent = new Intent(Main5Activity.this, Main2Activity.class);
                //Main5Activity.this.startActivity(myIntent);
            }
        });
    }



    public void identityIsOk(boolean bool){
        if(bool){
            Intent myIntent = new Intent(Main5Activity.this, Main2Activity.class);
            Main5Activity.this.startActivity(myIntent);
        }else{
            Intent currentIntent = getIntent();
            finish();
            startActivity(currentIntent);
        }
    }
}
