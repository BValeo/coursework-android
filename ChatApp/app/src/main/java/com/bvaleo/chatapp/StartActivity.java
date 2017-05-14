package com.bvaleo.chatapp;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bvaleo.chatapp.fragments.LoginFragment;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Fragment loginFrag = new LoginFragment();

        getFragmentManager().beginTransaction()
                .add(R.id.startFrame, loginFrag).commit();
    }
}
