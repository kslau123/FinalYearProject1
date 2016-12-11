package com.example.luke.finalyearproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DashBoard extends AppCompatActivity {

    TextView tvun;
    TextView tvpw;
    TextView tvemail;
    String Username;
    String Password;
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        tvun = (TextView) findViewById(R.id.tvun);
        tvpw = (TextView) findViewById(R.id.tvpw);


        Username = getIntent().getStringExtra("username");
        Password = getIntent().getStringExtra("password");
        tvun.setText(Username);
        tvpw.setText(Password);


        Button btn = (Button) findViewById(R.id.btnre);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoard.this,LoginActivity.class));
            }
        });
    }
}
