package com.bvaleo.chatapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bvaleo.chatapp.R;
import com.bvaleo.chatapp.database.MySQLHelper;

/**
 * Created by Valery on 16.04.2017.
 */
public class RegistrationFragment extends Fragment {

    private EditText login;
    private EditText pass;
    private Button signin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration_fragment, null);

        login = (EditText) v.findViewById(R.id.etLoginR);
        pass = (EditText) v.findViewById(R.id.etPassR);
        signin = (Button) v.findViewById(R.id.reg);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginET = login.getText().toString().trim();
                String passET = pass.getText().toString().trim();

                MySQLHelper db = new MySQLHelper(getActivity());
                db.insertNewUser(loginET, passET);

                getFragmentManager().beginTransaction()
                        .replace(R.id.startFrame, new LoginFragment()).commit();
            }
        });
    }
}
