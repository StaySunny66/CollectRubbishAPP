package com.shilight.rubbish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class JIFENG extends AppCompatActivity {
    private handler myh =new handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jifeng);
        getSupportActionBar().setTitle("积分明细");
        SharedPreferences mPref = getSharedPreferences("user", Activity.MODE_PRIVATE);
        String  user = mPref.getString("user","");
        if(user!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    getlog my = new getlog();
                    String[] line = my.get(user);
                    Log.i("fuck",line[0]);

                    Message fs = Message.obtain();
                    fs.obj = line;
                    myh.sendMessage(fs);


                }

            }).start();}
    }



    class handler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String[] strs = (String[]) msg.obj;
            Log.i("1231313",strs[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (JIFENG.this,android.R.layout.simple_expandable_list_item_1,strs);
            ListView list = findViewById(R.id.list);
            list.setAdapter(adapter);

        }
    }




}