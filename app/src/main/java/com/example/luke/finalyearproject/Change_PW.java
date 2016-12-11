package com.example.luke.finalyearproject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.luke.finalyearproject.Connector.url_add;

public class Change_PW extends Fragment {

    private EditText edchopw, edchnpw, edchconpw;
    private Button btn_chreset, btn_chapply;
    private String sOLDpw, sNEWpw, sCONpw;
    private SessionSaver sessionSaver;
    Connector connector;
    String url_add;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_change__pw, container, false);

        sessionSaver.checkLogin();

        connector.setDbfile("changepassword.php");
        url_add = connector.getUrl();

        edchopw = (EditText) view.findViewById(R.id.edchopw);
        edchnpw = (EditText) view.findViewById(R.id.edchnpw);
        edchconpw = (EditText) view.findViewById(R.id.edchconpw);

        btn_chreset = (Button) view.findViewById(R.id.btn_chreset);
        btn_chapply = (Button) view.findViewById(R.id.btn_chapply);

        btn_chreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edchopw.setText("");
                edchnpw.setText("");
                edchconpw.setText("");
            }
        });
        btn_chapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sOLDpw = edchopw.getText().toString().trim();
                sNEWpw = edchnpw.getText().toString().trim();
                sCONpw = edchconpw.getText().toString().trim();
                if (edchopw.length() > 0 && edchnpw.length() > 0 && edchconpw.length() > 0) {
                    if(sOLDpw.equals(sNEWpw) || (sOLDpw.equals(sCONpw))){
                        if (sNEWpw.equals(sCONpw)) {
                            final ChangePassword cpw = new ChangePassword(view.getContext(), url_add, btn_chapply);
                            cpw.execute(sOLDpw, sNEWpw, sCONpw);
                        }else {
                            Toast.makeText(view.getContext(),"The New Password is not the same as the Confirm Password",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(view.getContext(),"Your New Password is same as the Existing Password",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(view.getContext(),"You have missed input some information",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private class ChangePassword extends AsyncTask<String, String, String> {

        private final Context c;
        private final String address;
        private final Button btnapply;
        private ProgressDialog pd;
        private Boolean isSuccess = false;

        private ChangePassword(Context c, String address, Button btnapply) {
            this.c = c;
            this.address = address;
            this.btnapply = btnapply;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(c);
            pd.setTitle("Change Password");
            pd.setMessage("Progressing Please Wait");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            int temp;
            try {
                String Passwordp = params[0];
                String Con_Passwordp = params[1];

                URL url = new URL(url_add);
                String dataparams = "password=" + Passwordp + "&con_password=" + Con_Passwordp;

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
            try {
                JSONArray ja = new JSONArray(joResult);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = null;
                    jo = ja.getJSONObject(i);
                    isSuccess = jo.getBoolean("is_success");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (isSuccess == true) {
                //Successful

                Intent i = new Intent(c, HomePage.class);
                startActivity(i);

                pd = new ProgressDialog(c);
                pd.setTitle("Successful");
                pd.setMessage("Password Changed. Your password can be used in next login.");
                pd.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        pd.dismiss();
                    }
                }, 3000);

                } else {
                Toast.makeText(c,"Please check your internet connection.",Toast.LENGTH_SHORT).show();
                }
        }
    }
}
