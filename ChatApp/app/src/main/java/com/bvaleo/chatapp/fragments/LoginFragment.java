package com.bvaleo.chatapp.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bvaleo.chatapp.R;
import com.bvaleo.chatapp.activity.MainActivity;
import com.bvaleo.chatapp.database.MySQLHelper;

/**
 * Created by Valery on 16.04.2017.
 */
public class LoginFragment extends Fragment {

    private EditText etLogin;
    private EditText etPass;
    private Button login;
    private Button registration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_fragment, null);

        etLogin = (EditText) v.findViewById(R.id.etLoginR);
        etPass = (EditText) v.findViewById(R.id.etPassR);
        login = (Button) v.findViewById(R.id.login);
        registration = (Button) v.findViewById(R.id.signin);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySQLHelper db = new MySQLHelper(getActivity());
                String curLogin = etLogin.getText().toString().trim();
                String curPass = etPass.getText().toString().trim();

                if(!curLogin.equals("") && !curPass.equals("")){
                    if(db.isUser(curLogin, curPass)) {
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.putExtra("login", curLogin);
                        startActivity(i);
                    } else {
                        etLogin.setText("");
                        etPass.setText("");
                        Toast.makeText(getActivity(), "Неверные данные либо пользователь не зарегестрирован", Toast.LENGTH_LONG).show();
                    }
                } else{
                    etLogin.setText("");
                    etPass.setText("");
                    Toast.makeText(getActivity(), "Введите данные", Toast.LENGTH_LONG).show();
                }
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment registration = new RegistrationFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.startFrame, registration)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

}
