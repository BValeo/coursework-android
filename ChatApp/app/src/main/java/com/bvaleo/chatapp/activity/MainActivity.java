package com.bvaleo.chatapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bvaleo.chatapp.R;
import com.bvaleo.chatapp.fragments.DialogeFragment;

/**
 * Created by Valery on 16.04.2017.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction()
                .add(R.id.mainFragment, new DialogeFragment()).commit();

    }
}
