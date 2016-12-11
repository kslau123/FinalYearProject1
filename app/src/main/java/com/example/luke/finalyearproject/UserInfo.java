package com.example.luke.finalyearproject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.ArrayList;

public class UserInfo extends FragmentActivity {


    EditText editage, editcuwei, edittarwei, editheight;
    RadioButton radlow, radmed, radhigh, radexhigh, radmale, radfemale;
    RadioGroup groupactivitylvl, radioGroupGender;
    Spinner wkgoalspin;
    Button btnsummit;

    String url_add;

    //  String[] goalarray = {"0 lbs","0.5 lbs","1 lb","1.5 lbs","2 lbs"};
    String height, age, current_weight, target_weight;
    double curweikg, calgender, calactivitylevel;
    double BMR, dailycalories, weeklygoal, Calwkgoal;
    ArrayList<String> spinwkg;
    String gender, activity_level, weekly_goal, daily_calories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Connector.setDbfile("update_user_info.php");
        url_add = Connector.getUrl();

        editage = (EditText) findViewById(R.id.editage);
        editcuwei = (EditText) findViewById(R.id.editcuwei);
        edittarwei = (EditText) findViewById(R.id.edittarwei);
        editheight = (EditText) findViewById(R.id.editheight);

        btnsummit = (Button) findViewById(R.id.btnsummit);

        radmale = (RadioButton) findViewById(R.id.radmale);
        radfemale = (RadioButton) findViewById(R.id.radfemale);
        radlow = (RadioButton) findViewById(R.id.radlow);
        radmed = (RadioButton) findViewById(R.id.radmed);
        radhigh = (RadioButton) findViewById(R.id.radhigh);
        radexhigh = (RadioButton) findViewById(R.id.radexhigh);

        groupactivitylvl = (RadioGroup) findViewById(R.id.groupactivitylvl);
        radioGroupGender = (RadioGroup) findViewById(R.id.radioGroupGender);

        //Set Spinner Item
        wkgoalspin = (Spinner) findViewById(R.id.wkgoalspin);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);

        wkgoalspin.setAdapter(adapter);

        spinwkg = new ArrayList<String>();
        spinwkg.add("0 lbs");
        spinwkg.add("0.5 lbs");
        spinwkg.add("1 lb");
        spinwkg.add("1.5 lbs");
        spinwkg.add("2 lbs");

        wkgoalspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int spinnerpos = wkgoalspin.getSelectedItemPosition();

                switch (spinnerpos) {
                    case 0: {
                    }
                    weekly_goal = "0";
                    weeklygoal = 0;
                    case 1: {
                    }
                    weekly_goal = "0.5";
                    weeklygoal = 0.5;
                    case 2: {
                    }
                    weekly_goal = "1";
                    weeklygoal = 1;
                    case 3: {
                    }
                    weekly_goal = "1.5";
                    weeklygoal = 1.5;
                    case 4: {
                    }
                    weekly_goal = "2";
                    weeklygoal = 2;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                weeklygoal = 0;
            }
        });

        //Set Gender Value
        switch (radioGroupGender.getCheckedRadioButtonId()) {
            case R.id.radmale:
                gender = "M";
                calgender = 5;
            case R.id.radfemale:
                gender = "F";
                calgender = -161;
            default:
                calgender = 1;
        }

        //Set Activity Level Value
        switch (groupactivitylvl.getCheckedRadioButtonId()) {
            case R.id.radlow:
                activity_level = "Low";
                calactivitylevel = 1.3375;
            case R.id.radmed:
                activity_level = "Medium";
                calactivitylevel = 1.525;
            case R.id.radhigh:
                activity_level = "High";
                calactivitylevel = 1.7;
            case R.id.radexhigh:
                activity_level = "Extreme_High";
                calactivitylevel = 1.9;
            default:
                calactivitylevel = 1;
        }

        age = editage.getText().toString().trim();
        height = editheight.getText().toString().trim();
        current_weight = editcuwei.getText().toString().trim();
        target_weight = edittarwei.getText().toString().trim();


        curweikg = Integer.parseInt(editcuwei.getText().toString().trim()) / 2.2;

        //BMR formula
        BMR = 10 * curweikg + 6.25 * (Integer.parseInt(editheight.getText().toString().trim())) + 5 * (Integer.parseInt(editage.getText().toString().trim())) + calgender;
        Calwkgoal = weeklygoal * 500;
        dailycalories = calactivitylevel * BMR + Calwkgoal;
        daily_calories = String.valueOf(dailycalories);

        btnsummit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // public class Register
                if (editage != null || editcuwei != null || editheight != null || edittarwei != null || groupactivitylvl != null || radioGroupGender != null || wkgoalspin != null)
                {
                    final updateUserInfo up = new updateUserInfo(v.getContext(), url_add, btnsummit);
                    up.execute(age, gender, height, current_weight, target_weight, weekly_goal, activity_level,daily_calories);

                    if(up.is_success = true)
                    {
                        Toast.makeText(v.getContext(),"You information has been updated.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserInfo.this,DashBoard.class));
                    }
                } else {
                    Toast.makeText(v.getContext(), "You have missed some items.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class updateUserInfo extends AsyncTask<String, String, String> {

        Context c;
        String address;
        Button btnsummit;
        ProgressDialog pd;
        Boolean is_success = false;

        public updateUserInfo(Context c, String address, Button btnsummit) {
            this.c = c;
            this.address = address;
            this.btnsummit = btnsummit;
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
                int Agep = Integer.parseInt(params[0]);
                String Genderp = params[1];
                int Heightp = Integer.parseInt(params[2]);
                int Curweip = Integer.parseInt(params[3]);
                int Tarweip = Integer.parseInt(params[4]);
                int Wekgoalp = Integer.parseInt(params[5]);
                String Actlvlp = params[6];
                double Dailycp = Integer.parseInt(params[7]);


                URL url = new URL(url_add);
                String dataparams = "age=" + Agep + "&gender=" + Genderp + "&height=" + Heightp + "&current_weight=" + Curweip +
                        "&target_weight=" + Tarweip + "&weekly_goal=" + Wekgoalp + "&activity_level=" + Actlvlp + "&daily_calories=" + Dailycp;

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
        protected void onPostExecute(String jsonResult) {
            super.onPostExecute(jsonResult);
            pd.dismiss();
            try {
                JSONArray ja = new JSONArray(jsonResult);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = null;
                    jo = ja.getJSONObject(i);
                    is_success = jo.getBoolean("is_success");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
