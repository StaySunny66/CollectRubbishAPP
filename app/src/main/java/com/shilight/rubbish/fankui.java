package com.shilight.rubbish;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

public class fankui extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fankui);
        getSupportActionBar().setTitle("故障申报");
        Button fan = findViewById(R.id.fankui);
        fan.setOnClickListener(v -> {

            AlertDialog.Builder builder4 = new AlertDialog.Builder(fankui.this);
            builder4.setTitle("操作成功！");
            builder4.setMessage("您的故障申报已经提交成功！");
            builder4.setPositiveButton("知道了",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                }
            });
            builder4.create().show();



        });
    }
}