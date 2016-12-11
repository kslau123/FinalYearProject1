package com.example.luke.finalyearproject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Luke on 10/31/2016.
 */

public class Connector {
    static String url_add;

    public static final HttpURLConnection connect (String urlAddress){
        try{
            URL url = new URL(urlAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //Setting
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setReadTimeout(20000);
            httpURLConnection.setConnectTimeout(20000);
            //
            return httpURLConnection;
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    };

    public static void setDbfile(String file) {
        //Connection path
        String ip = "192.168.25.199:8080";
        String folder = "android/FYP";
        url_add = "http://" + ip + "/" + folder + "/" + file;
    }

    public static String getUrl(){
        return url_add;
    }
}
