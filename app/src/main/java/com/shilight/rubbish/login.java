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

public class login extends AppCompatActivity {
    Button TO_ADD,login;
    EditText login_user,login_pass;
    private hander han = new hander();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("登录");
        TO_ADD = (Button) findViewById(R.id.TO_ADD);
        login = (Button) findViewById(R.id.LOGIN);
        login_user = (EditText) findViewById(R.id.LOGIN_USER);
        login_pass = (EditText) findViewById(R.id.LOGIN_PASS);

        login.setOnClickListener(v -> {

                if(login_user.getText().length()<=5||login_pass.getText().length()<=5){

                    Log.i("错误","用户名称或者密码过短");
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            MD5 to_key = new MD5();
                            String  USER_KEY = to_key.MD5(login_pass.getText().toString());
                            Log.i("密钥",USER_KEY);
                            // 开始进行 HTTP 操作
                            http ADD_USER_ser = new http();
                            String r =  ADD_USER_ser.POST("https://api.shilight.cn/rubbish/login.php",
                                    "USER=" + login_user.getText().toString()+
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
                                    prefs.putString("user",login_user.getText().toString());
                                    prefs.putString("password",USER_KEY);
                                    prefs.putInt("JIFENG",result.getInt("score"));
                                    prefs.putInt("COUNT",result.getInt("count"));



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




        });
        TO_ADD.setOnClickListener(v -> {
            Intent go_add = new Intent();
            go_add.setClass(login.this, add.class);
            startActivity(go_add);

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

                        Intent go = new Intent();
                        go.setClass(login.this, MainActivity2.class);
                        startActivity(go);
                        finish();
                        break;

                    case 201:
                        Log.i("服务器", "用户名或密码错误！");
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(login.this);
                        builder1.setTitle("错误");
                        builder1.setMessage("用户名或密码错误！\n请尝试更正信息！");
                        builder1.setPositiveButton("好的！",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {


                            }
                        });
                        builder1.create().show();break;
                    case 100:
                        Log.i("服务器", "远程服务器出现严重错误！！");
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(login.this);
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