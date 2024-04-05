package com.shilight.rubbish;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.shilight.rubbish.databinding.ActivityMain2Binding;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {
    hander mhander = new hander();

    private ActivityMain2Binding binding;
    DialogLoading MY;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(MainActivity2.this, "用户取消", Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(MainActivity2.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message chick = Message.obtain();
                        chick.what=1;
                        mhander.sendMessage(chick);

                        http rest_dev = new http();
                        String r = rest_dev.POST("https://api.shilight.cn/rubbish/commend/","dev_id="+result.getContents());
                        JSONObject result = null;
                        try {
                            result = new JSONObject(r);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Message chick2 = Message.obtain();
                        try {
                            chick2.what=result.getInt("errno");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mhander.sendMessage(chick2);


                    }
                }).start();



            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    MY = new DialogLoading(MainActivity2.this);


        getSupportActionBar().hide();
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
    class hander extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    MY.setDialogLabel("发送指令");
                    MY.show();break;
                case 0:
                    MY.hide();
                    Intent go = new Intent();
                    go.setClass(MainActivity2.this, rubbish.class);
                    startActivity(go);break;
                case 3:
                    MY.hide();
                    Toast.makeText(MainActivity2.this,"未知设备",Toast.LENGTH_LONG).show();break;
                case 10:
                    MY.hide();
                    Toast.makeText(MainActivity2.this,"抱歉,设备已离线!",Toast.LENGTH_LONG).show();break;


            }


        }
    }
}


