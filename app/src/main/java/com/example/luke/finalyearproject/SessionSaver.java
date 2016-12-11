package com.example.luke.finalyearproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import java.util.HashMap;

/**
 * Created by Luke on 7/11/2016.
 */

public class SessionSaver {

    SharedPreferences preferences;
    Editor editor;
    Context context;
    int MODE = 0;
    private static final String PREF_NAME = "Session";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "USER_ID";
    public static final String KEY_UNAME = "USERNAME";

    public SessionSaver(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME,MODE);
        editor = preferences.edit();
    }

    public void loginSession(String USER_ID, String USERNAME){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, USER_ID);
        editor.putString(KEY_UNAME, USERNAME);
        editor.commit();
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(context,LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public HashMap<String,String> getStoreSession(){
        HashMap<String,String> user = new HashMap<String, String>();
        user.put(KEY_ID, preferences.getString(KEY_ID, null));
        user.put(KEY_UNAME,preferences.getString(KEY_UNAME,null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGIN, false);
    }

}
