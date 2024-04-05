package com.shilight.rubbish.ui.notifications;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.shilight.rubbish.R;
import com.shilight.rubbish.about;
import com.shilight.rubbish.databinding.FragmentNotificationsBinding;
import com.shilight.rubbish.edit;
import com.shilight.rubbish.fankui;
import com.shilight.rubbish.login;
import com.shilight.rubbish.web;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    TextView USER,time;
    ListView ls;
    ImageView mes,share,guzhang;



    public void allShare(){
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享给你的好友");//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, "我发现了一款特别优秀的软件快来下载看看吧\nhttps://wwa.lanzoui.com/iY8J1vatrvi\n");//添加分享内容
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "分享给好友");
        startActivity(share_intent);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        time = (TextView) root.findViewById(R.id.time);
        mes = (ImageView) root.findViewById(R.id.message);
        share = (ImageView) root.findViewById(R.id.share);
        USER = (TextView) root.findViewById(R.id.user_id);
        guzhang = (ImageView) root.findViewById(R.id.guzhang);
        ls = (ListView) root.findViewById(R.id.list2);

        long sys_time = System.currentTimeMillis();
        Date date = new Date(sys_time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        time.setText(format.format(date));
        Log.i("时间",format.format(date));


        SharedPreferences mPref = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
        String  us = mPref.getString("user","");
        String  em = mPref.getString("email","");
        Log.i("运行了","12311");



        USER.setText(us);

        guzhang.setOnClickListener(v -> {

            Intent go1 = new Intent();
            go1.setClass(getActivity(), fankui.class);
            startActivity(go1);


        });
        mes.setOnClickListener(v -> {

            Intent go1 = new Intent();
            go1.setClass(getActivity(), web.class);
            go1.putExtra("data","https://s.weibo.com/top/summary/");
            go1.putExtra("tittle","信息流");
            startActivity(go1);


        });
        share.setOnClickListener(v -> {
            allShare();

        });


        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        Intent go2 = new Intent();
                        go2.setClass(getContext(), edit.class);
                        startActivity(go2);
                        break;
                    case 1:
                        Intent go = new Intent();
                        go.setClass(getActivity(), login.class);
                        startActivity(go);
                        break;
                    case 2:
                        Intent go1 = new Intent();
                        go1.setClass(getActivity(), web.class);
                        go1.putExtra("data","https://api.shilight.cn/PARK/privacy.html");
                        go1.putExtra("tittle","隐私政策");
                        startActivity(go1);
                        break;
                    case 3:
                        Intent go3 = new Intent();
                        go3.setClass(getActivity(), about.class);
                        startActivity(go3);
                        break;
                    case 4:
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(requireActivity());
                        builder4.setTitle("退出登录");
                        builder4.setMessage("确认退出登录吗？！\n这将无法及时获取车位信息！");
                        builder4.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE).edit().putString("user","").apply();
                                getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE).edit().putString("password","").apply();
                                Intent go4 = new Intent();
                                go4.setClass(getActivity(), login.class);
                                startActivity(go4);
                                getActivity().finish();
                            }
                        });
                        builder4.create().show();break;
                }
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}