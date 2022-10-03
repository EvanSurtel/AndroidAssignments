package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Button;


public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    SharedPreferences sp;
    String emailStr, passwordStr;

    protected static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "In OnCreate()");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        login = findViewById(R.id.button);

        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String s1 = sp.getString("email", "");
        String s2 = sp.getString("password", "");
        email.setText(s1);
        password.setText(s2);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailStr = email.getText().toString();
                passwordStr = password.getText().toString();

                SharedPreferences.Editor editor = sp.edit();

                editor.putString("email", emailStr);
                editor.putString("password", passwordStr);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                                  }
        });

    }

    public void onResume() {
        super.onResume();
        Log.i(TAG, "In OnResume()");

    }

    public void onStart() {
        super.onStart();
        Log.i(TAG, "In OnStart()");
    }

    public void onPause() {
        super.onPause();
        Log.i(TAG, "In OnPause()");
    }

    public void onStop() {
        super.onStop();
        Log.i(TAG, "In OnStop()");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "In OnDestroy()");
    }
}