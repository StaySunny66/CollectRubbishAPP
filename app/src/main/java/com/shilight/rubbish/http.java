package com.shilight.rubbish;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class http {
    String result = "{\"result\":\"10002\"}";

    public String POST(String url,String data){

        try {
            URL wz = new URL(url);
            try {
                HttpURLConnection HTTP= (HttpURLConnection) wz.openConnection();
                HTTP.setDoOutput(true);
                HTTP.setDoInput(true);
                HTTP.setRequestMethod("POST");
                HTTP.setConnectTimeout(3000);
                HTTP.setUseCaches(false);
                HTTP.setRequestProperty("Accept-Encoding","identity");
                OutputStream outputStream = HTTP.getOutputStream();
                outputStream.write(data.getBytes());
                int code = HTTP.getResponseCode();            //获得服务器的响应码
                Log.i("服务器:", String.valueOf(code));
                InputStream inputStream=HTTP.getInputStream();//获取服务器返回输入流
                BufferedReader reader=new  BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response=new StringBuilder();
                String line;
                while ((line=reader.readLine())!=null){
                    response.append(line);
                }
                result = (response.toString());

            } catch (IOException e) {
                e.printStackTrace();
                return result;
            }


        } catch (MalformedURLException e ){
            e.printStackTrace();
            return result;

        }finally {

        }


        return result;
    }
    //HTTP请求之 post
}
