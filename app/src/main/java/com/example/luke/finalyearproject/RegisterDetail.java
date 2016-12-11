package com.example.luke.finalyearproject;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class RegisterDetail extends DialogFragment {
//
//    //Connection path
//    String ip = "192.168.25.209:8080";
//    String folder = "android/FYP";
//    String file = "register.php";
//    final String url_add = "http://" + ip + "/" + folder + "/" + file;


    TextView txtusername, txtpassword, txtcon_password, txtemail, txtcontact;
    Button btnback, btnsummit;
    String username, password, con_password, email, contact = null;

    String url_add;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_register_detail, null);
        getDialog().setTitle("Confirm_Your_Input");

        //Set Connection file
        Connector.setDbfile("register.php");
        url_add = Connector.url_add;

        txtusername = (TextView) view.findViewById(R.id.txtusername);
        txtpassword = (TextView) view.findViewById(R.id.txtpassword);
        txtcon_password = (TextView) view.findViewById(R.id.txtcon_password);
        txtemail = (TextView) view.findViewById(R.id.txtemail);
        txtcontact = (TextView) view.findViewById(R.id.txtcontact);

        username = getActivity().getIntent().getStringExtra("username");
        password = getActivity().getIntent().getStringExtra("password");
        con_password = getActivity().getIntent().getStringExtra("con_password");
        email = getActivity().getIntent().getStringExtra("email");
        contact = getActivity().getIntent().getStringExtra("contact");

        txtusername.setText(username);
        txtpassword.setText(password);
        txtcon_password.setText(con_password);
        txtemail.setText(email);
        txtcontact.setText(contact);

        btnback = (Button) view.findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnsummit = (Button) view.findViewById(R.id.btnsummit);
        btnsummit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Register register = new Register(v.getContext(),url_add,btnsummit);
                register.execute(username,password,con_password,email,contact);
            }
        });
        return view;
    }

    public class Register extends AsyncTask<String, String, String> {
        Context c;
        String address;
        Button btnreg;
        ProgressDialog pd;
        //String Username, Password, Con_password, Email, Contact = null;
        Boolean is_success = false;

        public Register(Context c, String address, Button btnreg) {
            this.c = c;
            this.address = address;
            this.btnreg = btnreg;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(c);
            pd.setTitle("Register");
            pd.setMessage("Progressing Please Wait");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";
            int temp;
            try {
                String Usernamep = params[0];
                String Passwordp = params[1];
                String Con_passwordp = params[2];
                String Emailp = params[3];
                String Contactp = params[4];

                URL url = new URL(url_add);
                String dataparams = "username=" + Usernamep + "&password=" + Passwordp + "&con_password="
                        + Con_passwordp + "&email=" + Emailp + "contact" + Contactp;

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
                    JSONObject json = null;
                    json = ja.getJSONObject(i);
                    is_success = json.getBoolean("is_success");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (is_success == true){
                Intent intent = new Intent(c,DashBoard.class);
                c.startActivity(intent);
            }else{
                Toast.makeText(c,"Your Username or Email has already existed.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

