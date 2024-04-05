package com.shilight.rubbish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class edit extends AppCompatActivity {
    EditText ypass,pass,pass1;
    hander han = new hander();
    Button change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setTitle("密码修改");
        ypass = (EditText) findViewById(R.id.now_PASS);
        pass  = (EditText) findViewById(R.id.change);
        pass1  = (EditText) findViewById(R.id.change2);
        change = (Button) findViewById(R.id.CHANGE);


        change.setOnClickListener(v -> {
            String now_pass = ypass.getText().toString();
            String c_pass = pass.getText().toString();
            String c_pass1 = pass1.getText().toString();
            if(c_pass.length()<=5){

                Toast.makeText(this,"密码过短",Toast.LENGTH_SHORT).show();

            }else{

                if(!c_pass.equals(c_pass1)){
                    Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();

                }else{
                    SharedPreferences mPref = getSharedPreferences("user", Activity.MODE_PRIVATE);
                    String pass_key = mPref.getString("password", "");
                    MD5 my = new MD5();

                    if(!pass_key.equals(my.MD5(now_pass))){

                        Toast.makeText(this,"原密码出错！",Toast.LENGTH_SHORT).show();


                    }else{
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = Message.obtain();
                                MD5 to_key = new MD5();
                                String  USER_KEY = to_key.MD5(c_pass);
                                Log.i("密钥",USER_KEY);
                                // 开始进行 HTTP 操作
                                http ADD_USER_ser = new http();
                                String r =  ADD_USER_ser.POST("HTTPS://api.shilight.cn/rubbish/changepass.php",
                                        "USER=" +getSharedPreferences("user", Activity.MODE_PRIVATE).getString("user","") +
                                                "&USER_KEY=" + USER_KEY);

                                Log.i("响应", r);
                                JSONObject result = null;
                                try {
                                    result = new JSONObject(r);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    msg.what = 0;
                                }
                                try {
                                    msg.what = result.getInt("statues");
                                    if(msg.what==200){
                                        SharedPreferences.Editor prefs = getSharedPreferences("user", Activity.MODE_PRIVATE).edit();
                                        prefs.putString("password",USER_KEY);
                                        prefs.apply();
                                    }
                                    if(msg.what==301){

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    msg.what = 0;
                                }


                                han.sendMessage(msg);

                            }

                        }).start();



                    }


                }


            }




        });


    }
    class hander extends Handler {
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what!=0) {
                int a= msg.what;
                switch (a) {
                    case 200:
                        Log.i("成功", "密码修改成功");
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(edit.this);
                        builder3.setTitle("成功");
                        builder3.setMessage("密码修改成功！\n请牢记您的密码！");
                        builder3.setPositiveButton("好的！",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                Intent go = new Intent();
                                go.setClass(edit.this, MainActivity2.class);
                                startActivity(go);finish();
                            }
                        });
                        builder3.create().show();
                        break;

                    case 100:
                        Log.i("服务器", "远程服务器出现严重错误！！");
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(edit.this);
                        builder4.setTitle("错误");
                        builder4.setMessage("远程服务器出现严重错误！\n请告知管理员！");
                        builder4.setPositiveButton("好的！",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        });
                        builder4.create().show();break;
                }

            }
        }


    }



}