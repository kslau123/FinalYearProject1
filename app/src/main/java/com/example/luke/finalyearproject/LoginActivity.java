package com.example.luke.finalyearproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.logging.Handler;

public class LoginActivity extends Activity {
//
//    //Connection path
//    String ip = "192.168.25.209:8080";
//    String folder = "android/FYP";
//    String file = "checklogin.php";
//    final String url_add = "http://" + ip + "/" + folder + "/" + file;

    String url_add;

    private  Button btnlogin, btnreglogin;
    private EditText edittxtuser, edittxtpw;
    private String UserName = null, PassWord = null, Email = null;
    private int btnclick =5;
    private long mLastClickTime = 0;
    private long enableClickTime = 0;

    SessionSaver sessionSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set Connection file
        Connector.setDbfile("checklogin.php");
        url_add = Connector.getUrl();

        sessionSaver = new SessionSaver(getApplicationContext());

        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnreglogin = (Button) findViewById(R.id.btnreglogin);

        edittxtuser = (EditText) findViewById(R.id.edittxtuser);
        edittxtpw = (EditText) findViewById(R.id.edittxtpw);

        //Set Register Button Action
        btnreglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        //Set Login Button Action
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserName = edittxtuser.getText().toString().trim();
                PassWord = edittxtpw.getText().toString().trim();

                //Prevent multi click
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if (UserName.isEmpty() || PassWord.isEmpty()) {
                    Toast.makeText(v.getContext(), "Your Username or Password is empty.", Toast.LENGTH_SHORT).show();

                } else {
                    final Login l = new Login(v.getContext(), url_add, btnlogin);
                    l.execute(UserName, PassWord);
                }
                //Prevention
                if(btnclick == 0){
                    btnlogin.setEnabled(false);
                    Toast.makeText(LoginActivity.this,"Please wait 10 minutes",Toast.LENGTH_LONG).show();

                    new CountDownTimer(600000,1000){
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            btnclick = 5;
                            btnlogin.setEnabled(true);
                        }
                    }.start();
                }else if(btnclick < 0){
                    btnlogin.setEnabled(false);
                    Toast.makeText(LoginActivity.this,"Please wait 10 minutes",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private class Login extends AsyncTask<String, String, String> {

        Context c;
        String address;
        Button btnlogin;
        ProgressDialog pd;
        String Username, User_id;
        Boolean isSuccess;


        public Login(Context c, String address, Button btnlogin) {
            this.c = c;
            this.address = address;
            this.btnlogin = btnlogin;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(c);
            pd.setTitle("Login");
            pd.setMessage("Authenticating Please Wait");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            int temp;
            try {
                String Usernamep = params[0];
                String Passwordp = params[1];

                URL url = new URL(url_add);
                String dataparams = "username=" + Usernamep + "&password=" + Passwordp;

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(dataparams.getBytes());
                os.flush();
                os.close();

                InputStream is = con.getInputStream();
                while ((temp = is.read()) != -1) {
                    data += (char) temp;
                }
                is.close();
                con.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String joResult) {
            super.onPostExecute(joResult);
            pd.dismiss();
            //    String error =null;
            try {
                JSONArray ja = new JSONArray(joResult);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = null;
                    jo = ja.getJSONObject(i);
                    //   JSONObject userdata = jo.getJSONObject("userdata");
                    Username = jo.getString("username");
                    //Password = jo.getString("password");
                    isSuccess = jo.getBoolean("is_success");

                }
                //       Email = userdata.getString("Email_address");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (isSuccess == true) {
                //Login Successful
                sessionSaver.loginSession(User_id,Username);
                Intent i = new Intent(c, HomePage.class);
                i.putExtra("username", Username);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            } else {
                //Login Failed
                Toast.makeText(c, "We have not got your account. Please check you input.", Toast.LENGTH_SHORT).show();
                btnclick --;
                if(btnclick < 5 && btnclick > 0){
                    Toast.makeText(c, "You can try" + btnclick + "more", Toast.LENGTH_SHORT).show();
                }
            }


        }


    }
}
