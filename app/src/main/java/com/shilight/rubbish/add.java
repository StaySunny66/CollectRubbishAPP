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

import org.json.JSONException;
import org.json.JSONObject;

public class add extends AppCompatActivity {
    EditText ADD_USER,ADD_USER_KEY,ADD_USER_EMAIL,ADD_USER_KEY2;
    Button ADD_SEVER;
    private hander han = new hander();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("注册新用户");

        ADD_USER = (EditText)findViewById(R.id.user);
        ADD_USER_KEY = (EditText)findViewById(R.id.pass);
        ADD_USER_EMAIL = (EditText)findViewById(R.id.email);
        ADD_USER_KEY2 = (EditText)findViewById(R.id.pass2);
        ADD_SEVER = (Button) findViewById(R.id.ADD);
        ADD_SEVER.setOnClickListener(v -> {
            if(ADD_USER.getText().length()<=5||ADD_USER_KEY.getText().length()<=5||((EditText)findViewById(R.id.email)).getText().length()<=8){
                Log.i("错误","用户名称或者密码过短");



            }else{

                if(!ADD_USER_KEY.getText().toString().equals(ADD_USER_KEY2.getText().toString())){

                    Log.i("错误","两次密码不正确"+ADD_USER_KEY.getText().toString()+ADD_USER_KEY2.getText().toString());

                }else{

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            MD5 to_key = new MD5();
                            String  USER_KEY = to_key.MD5(ADD_USER_KEY.getText().toString());
                            Log.i("密钥",USER_KEY);
                            // 开始进行 HTTP 操作
                            http ADD_USER_ser = new http();
                            String r =  ADD_USER_ser.POST("https://api.shilight.cn/rubbish/add.php",
                                    "USER=" + ADD_USER.getText().toString()+
                                            "&USER_KEY=" + USER_KEY +
                                            "&EMAIL=" + ADD_USER_EMAIL.getText().toString());

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
                                    prefs.putString("user",ADD_USER.getText().toString());
                                    prefs.putString("password",USER_KEY);
                                    prefs.putString("email",ADD_USER_EMAIL.getText().toString());
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


        });


    }
    class hander extends Handler {
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what!=0) {
                int a= msg.what;
                switch (a) {
                    case 200:
                        Log.i("服务器", "服务器添加成功！");
                        AlertDialog.Builder builder = new AlertDialog.Builder(add.this);
                        builder.setTitle("添加成功");
                        builder.setMessage("恭喜！\n您的信息已录入成功");
                        builder.setPositiveButton("好的！",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                Intent go = new Intent();
                                go.setClass(add.this, MainActivity2.class);
                                startActivity(go);
                            }
                        });
                        builder.create().show();break;
                    case 301:
                        Log.i("服务器", "用户信息已经存在了！");
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(add.this);
                        builder1.setTitle("错误");
                        builder1.setMessage("用户信息已经存在！\n请直接登录！");
                        builder1.setPositiveButton("好的！",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {


                            }
                        });
                        builder1.create().show();break;
                    case 100:
                        Log.i("服务器", "远程服务器出现严重错误！！");
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(add.this);
                        builder3.setTitle("错误");
                        builder3.setMessage("远程服务器出现严重错误！\n请告知管理员！");
                        builder3.setPositiveButton("好的！",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        });
                        builder3.create().show();break;
                }

            }
        }


    }





}