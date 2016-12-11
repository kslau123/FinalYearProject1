package com.example.luke.finalyearproject;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Setting_Fragment extends FragmentActivity {

    private Button btn_changepw, btn_userprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting__fragment);

        btn_changepw = (Button) findViewById(R.id.btn_changepw);
        btn_changepw = (Button) findViewById(R.id.btn_userprofile);

        btn_changepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
