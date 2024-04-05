package com.shilight.rubbish.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.shilight.rubbish.JIFENG;
import com.shilight.rubbish.MD5;
import com.shilight.rubbish.MainActivity2;
import com.shilight.rubbish.R;
import com.shilight.rubbish.ScanActivity;
import com.shilight.rubbish.databinding.FragmentHomeBinding;
import com.shilight.rubbish.http;
import com.shilight.rubbish.login;
import com.shilight.rubbish.rubbish;
import com.shilight.rubbish.web;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private QMUIPullRefreshLayout mPullRefreshLayout;
    handler han =new handler();
    Button sm;
    ImageView JIFENG,GDYY,JFSC,TZGG;
    PieChart mpiechart;
    JSONArray a;

    int score,count;

    TextView  USER,JIF,COU;


    private void requsetPermission(){
        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.CAMERA)!=     PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.CAMERA},1);

            }else {

            }
        }else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    Log.i("123","123121123");


                }else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    Toast.makeText(getActivity(),"请手动打开相机权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    private boolean showData(int x,int y){
        PieEntry pie1 = new PieEntry(x,"瓶子");
        PieEntry pie2 = new PieEntry(y,"废纸");

        List<PieEntry> pieEntryList = new ArrayList<>();
        pieEntryList.add(pie1);pieEntryList.add(pie2);


        PieDataSet set = new PieDataSet(pieEntryList,"");
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FFC107"));
        colors.add(Color.parseColor("#ee6e55"));

        set.setColors(colors);//添加颜色
        set.setSliceSpace(3f);//切割空间
        set.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);//值在图表外显示
        final int [] arr = {x, y};//格式值

        PieData pieData = new PieData(set);
        set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//值在图表外显示(所占百分比)
        Description description = mpiechart.getDescription();//获得图表的描述
        description.setText("垃圾投放比例");
        Legend legend = mpiechart.getLegend();//获得图例的描述
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(15f);
        legend.setFormSize(15f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setDrawInside(true);//再里面显示
        //mpiechart.setHoleRadius(90f);
        mpiechart.animateXY(1000, 1000);
        mpiechart.setData(pieData);





        return true;


    }





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sm = (Button)root.findViewById(R.id.button);
        Toast.makeText(getActivity(),"下拉或点击刷新按钮",Toast.LENGTH_SHORT).show();
        mPullRefreshLayout = (QMUIPullRefreshLayout)root.findViewById(R.id.qmui_pull);
        mpiechart = (PieChart) root.findViewById(R.id.mpiechart);

        JIFENG = (ImageView)root.findViewById(R.id.imageView4);
        GDYY = (ImageView)root.findViewById(R.id.imageView6);
        JFSC = (ImageView)root.findViewById(R.id.imageView5);
        TZGG = (ImageView)root.findViewById(R.id.imageView3);
        USER = (TextView) root.findViewById(R.id.usr);
        JIF = (TextView) root.findViewById(R.id.jif);
        COU = (TextView) root.findViewById(R.id.cou);
        score = 0;
        count = 0;


        SharedPreferences m1Pref = this.getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
        String user = m1Pref.getString("user", "");
        Log.i("测试",user);

        if(user.length()>=5){
            JIF.setText(String.valueOf(m1Pref.getInt("JIFENG", 0)));
            COU.setText(String.valueOf(m1Pref.getInt("COUNT", 0)));
            USER.setText("欢迎！"+m1Pref.getString("user", ""));


        }else{

            JIF.setText("-");
            COU.setText("-");


        }



        USER.setOnClickListener(v -> {
            SharedPreferences mPref = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
            String user1 = mPref.getString("user", "");
            if(user1.length()<=5){

                Intent go = new Intent();
                go.setClass(getActivity(), com.shilight.rubbish.login.class);
                startActivity(go);

            }else {

                Toast.makeText(getActivity(),"小主好漂亮！",Toast.LENGTH_LONG).show();

            }

        });

        JIFENG.setOnClickListener(v -> {
            Intent go = new Intent();
            go.setClass(getActivity(), com.shilight.rubbish.JIFENG.class);
            startActivity(go);
        });
        GDYY.setOnClickListener(v -> {
            Intent go = new Intent();
            go.setClass(getActivity(), com.shilight.rubbish.JIFENG.class);
            startActivity(go);
        });
        JFSC.setOnClickListener(v -> {
            Intent go1 = new Intent();
            go1.setClass(getActivity(), web.class);
            go1.putExtra("data","https://xh12370258.m.icoc.me/col.jsp?id=108");
            go1.putExtra("tittle","积分商城");
            startActivity(go1);
        });
        TZGG.setOnClickListener(v -> {
            Intent go = new Intent();
            go.setClass(getActivity(), com.shilight.rubbish.tzgg.class);
            startActivity(go);
        });





        showData(1,3);




        sm.setOnClickListener(v -> {

            requsetPermission();
            IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
            intentIntegrator.setBeepEnabled(true);
            /*设置启动我们自定义的扫描活动，若不设置，将启动默认活动*/
            intentIntegrator.setCaptureActivity(ScanActivity.class);
            intentIntegrator.initiateScan();



        });




        mPullRefreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {
                
            }

            @Override
            public void onMoveRefreshView(int offset) {

            }

            @Override
            public void onRefresh() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取所需的参数
                        Message msg = Message.obtain();
                        SharedPreferences mPref = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
                        String user = mPref.getString("user", "");
                        String pass = mPref.getString("password","");


                        // 开始进行 HTTP 操作
                        http ADD_USER_ser = new http();
                        String r =  ADD_USER_ser.POST("https://api.shilight.cn/rubbish/login.php",
                                "USER=" +user+
                                        "&USER_KEY=" + pass);

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
                                SharedPreferences.Editor prefs = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE).edit();
                                prefs.putInt("JIFENG",result.getInt("score"));
                                prefs.putInt("COUNT",result.getInt("count"));
                                score = result.getInt("score");
                                count = result.getInt("count");



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





        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;


    }

    class handler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

                    mPullRefreshLayout.finishRefresh();

                    switch (msg.what){
                        case 200 :
                            JIF.setText(String.valueOf(score)); COU.setText(String.valueOf(count));Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();break;

                        case 301 :Toast.makeText(getActivity(),"登录失败",Toast.LENGTH_SHORT).show();break;
                        case 0   :Toast.makeText(getActivity(),"未知错误",Toast.LENGTH_SHORT).show();break;



                    }



            }



        }
    }

