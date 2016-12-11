package com.example.luke.finalyearproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Date;

import static com.example.luke.finalyearproject.Connector.url_add;

public class RegisterActivity extends FragmentActivity {

    Button btnreg;
    EditText USERNAME, PASSWORD, CON_PASSWORD, EMAIL, CONTACT;
    String username, password, con_password, email, contact;


    TextView txtusername, txtpassword, txtcon_password, txtemail, txtcontact;
    Button btnback, btnsummit;
    // String username, password, con_password, email, contact = null;
    String url_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Declaer type
        btnreg = (Button) findViewById(R.id.btnregnext);
        USERNAME = (EditText) findViewById(R.id.edittxtreguser);
        PASSWORD = (EditText) findViewById(R.id.edittxtregpw);
        CON_PASSWORD = (EditText) findViewById(R.id.editregconfirmpw);
        EMAIL = (EditText) findViewById(R.id.edittxtregemail);
        CONTACT = (EditText) findViewById(R.id.edittxtcontact);


        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = USERNAME.getText().toString().trim();
                password = PASSWORD.getText().toString().trim();
                con_password = CON_PASSWORD.getText().toString().trim();
                email = EMAIL.getText().toString().trim();
                contact = CONTACT.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || con_password.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                    Toast.makeText(v.getContext(), "You have not input every information.", Toast.LENGTH_LONG).show();
                } else if (password.equals(con_password)) {
                    showDialog(username, password, con_password, email, contact);
                } else {
                    Toast.makeText(v.getContext(), "Your Password and Confirm_Password are different.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void showDialog(String un, String pw, String cpw, String em, String ct) {
        Connector.setDbfile("register.php");
        url_add = Connector.getUrl();

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Details");
        dialog.setContentView(R.layout.dialog_register_detail);

        txtusername = (TextView) dialog.findViewById(R.id.txtusername);
        txtpassword = (TextView) dialog.findViewById(R.id.txtpassword);
        txtcon_password = (TextView) dialog.findViewById(R.id.txtcon_password);
        txtemail = (TextView) dialog.findViewById(R.id.txtemail);
        txtcontact = (TextView) dialog.findViewById(R.id.txtcontact);

        try {
            txtusername.setText(un);
            txtpassword.setText(pw);
            txtcon_password.setText(cpw);
            txtemail.setText(em);
            txtcontact.setText(ct);
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnback = (Button) dialog.findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnsummit = (Button) dialog.findViewById(R.id.btnsummit);
        btnsummit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Reg register = new Reg(v.getContext(), url_add, btnsummit);
                register.execute(username, password, con_password, email, contact);
            }
        });
        dialog.show();
    }


    private class Reg extends AsyncTask<String, String, String> {
        Context c;
        String address;
        Button btnregi;
        ProgressDialog pd;
        //String Username, Password, Con_password, Email, Contact = null;
        Boolean is_success = false;


        public Reg(Context c, String address, Button btnregi) {
            this.c = c;
            this.address = address;
            this.btnregi = btnregi;
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
                        + Con_passwordp + "&email=" + Emailp + "&contact=" + Contactp;

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
            if (is_success == true) {
                Intent intent = new Intent(c, LoginActivity.class);
                c.startActivity(intent);
                Toast.makeText(c, "Register Successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(c, "Your Username or Email has already existed. Please check you input.", Toast.LENGTH_LONG).show();
            }
        }
    }


}