package com.shilight.rubbish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {
    TextView tittle1;
    handler mhandler = new handler();
    boolean run = true;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getSupportActionBar().hide();
        tittle1 = findViewById(R.id.tittle);
        Typeface tittle = Typeface.createFromAsset(getAssets(), "tittle.ttf");
        tittle1.setTypeface(tittle);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(run){
                    try {

                        if(i>=2) {
                            run = !run;

                        }
                        Thread.sleep(600); i++;


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
                Message msg = Message.obtain();
                msg.arg1 = i;
                msg.what = 1;
                mhandler.sendMessage(msg);

            }

        }).start();





    }

    class handler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);



                Intent go = new Intent();
                go.setClass(FirstActivity.this, MainActivity2.class);
                startActivity(go);
                finish();


        }


    }
}