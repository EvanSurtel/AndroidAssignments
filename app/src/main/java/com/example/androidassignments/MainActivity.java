package com.example.androidassignments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button, startChat;
    public static final int REQUEST_CODE = 10;

    protected static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("Response");
            Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();
        }

        button = findViewById(R.id.button);
        startChat = findViewById(R.id.startChat);

        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User clicked Start Chat");
                Intent intent = new Intent(MainActivity.this, ChatWindow.class);
                startActivity(intent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            Log.i(TAG, "Returned to MainActivity.onActivityResult");
        }
        if(resultCode == Activity.RESULT_OK){

                String value = data.getStringExtra("Response");
                Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();
            }
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