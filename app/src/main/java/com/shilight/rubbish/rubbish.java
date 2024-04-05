package com.shilight.rubbish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shilight.rubbish.DialogLoading;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class rubbish extends AppCompatActivity {
    Button FINISH;
    DialogLoading loading ;

    hander my= new hander();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish);
        FINISH = (Button)findViewById(R.id.FIN);
        getSupportActionBar().hide();
        QMUITipDialog tipDialog= new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        tipDialog.dismiss();

        FINISH.setOnClickListener(v -> {

            setDialogLabel("请稍等！");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message chick = Message.obtain();
                    chick.what=1;
                    SharedPreferences mPref = getSharedPreferences("user", Activity.MODE_PRIVATE);
                    String user1 = mPref.getString("user", "");
                    http rest_dev = new http();
                    String r = rest_dev.POST("https://api.shilight.cn/rubbish/settlement/","USER="+user1);
                    Log.i("结算",r);
                    JSONObject result = null;
                    try {
                        result = new JSONObject(r);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Message chick2 = Message.obtain();
                    try {
                        Log.i("1231321", String.valueOf(result.getInt("jifeng")));
                        chick.what=4;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    my.sendMessage(chick2);

                }
            }).start();



            loading.show();
            Intent go = new Intent();
            go.setClass(rubbish.this, MainActivity2.class);
            startActivity(go);


        });




    }

    private void setDialogLabel(String label) {
        if (loading == null) {
            loading = new DialogLoading(this);
        }
        loading.setDialogLabel(label);
    }




    class hander extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

              finish();
        }
    }


}