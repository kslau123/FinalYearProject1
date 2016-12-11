package com.example.luke.finalyearproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nav_view;
    private SessionSaver sessionSaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        sessionSaver.checkLogin();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        nav_view = (NavigationView) findViewById(R.id.nav_view);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nav_view.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.homepage_menu, menu);
            return true;
        }
        */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.menu_activity_home_page:
            case R.id.menu_activity_userinfo:
            case R.id.menu_activity_calories_counter:
            case R.id.menu_activity_report:
            case R.id.menu_activity_setting:
                Intent i = new Intent(this, Setting_Fragment.class);
                startActivity(i);
            case R.id.menu_activity_logout:
                logoutDialog();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logoutDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Logout");
        dialog.setContentView(R.layout.dialog_logout);

        Button btn_logoutcan = (Button) dialog.findViewById(R.id.btn_logoutcan);
        Button btn_logoutlo = (Button) dialog.findViewById(R.id.btn_logoutlo);

        btn_logoutcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_logoutlo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionSaver.logout();
            }
        });
        dialog.show();
    }

}





