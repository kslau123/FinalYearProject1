package com.example.luke.finalyearproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Setting_Fragment extends Fragment {

    private Button btn_changepw, btn_userprofile;
    Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);

        btn_changepw = (Button) view.findViewById(R.id.btn_changepw);
        btn_userprofile = (Button) view.findViewById(R.id.btn_userprofile);

        btn_changepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new Change_PW();
            }
        });
        btn_userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;

    }

    private void setFragment() {
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_homepage, fragment).commit();


        }
    }



}



