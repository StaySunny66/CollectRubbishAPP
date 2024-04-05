package com.shilight.rubbish;

import android.util.Log;

import com.shilight.rubbish.http;

import org.json.JSONException;
import org.json.JSONObject;


public class getlog {

public String[] get(String id){
    String[] str = new String[]{" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " "};
    http get_log = new http();
    String r ="123";
    String data ="USER="+id;
    r = get_log.POST("https://api.shilight.cn/rubbish/settlement/get_log.php",data);
    Log.i("获取",r);
    JSONObject log = null;
        try {
            log = new JSONObject(r);
        } catch (JSONException e) { e.printStackTrace(); }


for(int i=0; i<15; i++) {
    try {

        String line = log.getJSONArray("user").get(i).toString();
        Log.i("待解析",line);
        JSONObject loog = new JSONObject(line);
        if (loog.getString("data").equals("9999-99-99")) break;
        str[i]="时间:"+loog.getString("data")+"\n废纸 : "+loog.getString("weight")+"克       瓶子:  "+loog.getString("count")+"个        积分:  "+loog.getString("get_score")+"分";



    } catch (JSONException e) {
        e.printStackTrace();
    }
}

    return str;
}

    public String getone(String id){
        http get_log = new http();
        String r ="123";
        String line = null;
        String data ="source=keshiapp&user="+id;
        r = get_log.POST("https://auto.shilight.cn/user/log.php",data);
        Log.i("获取",r);
        JSONObject log = null;
        try {
            log = new JSONObject(r);
            line = log.getJSONArray("user").get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return line;


    }




}
