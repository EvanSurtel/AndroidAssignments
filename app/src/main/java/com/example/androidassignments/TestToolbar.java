package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {

    protected static final String TAG = "TestToolbar";
    FloatingActionButton floatingActionButton;
    ConstraintLayout toolbarLayout;

    String snack = "You selected item 1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        toolbarLayout = findViewById(R.id.toolbarLayout);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(toolbarLayout, snack, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()){
            case R.id.action_one:
                Log.d("Toolbar", "Option 1 selected");
                Snackbar.make(toolbarLayout, snack, Snackbar.LENGTH_LONG).show();
                break;
            case R.id.action_two:
                Log.d("Toolbar", "Option 2 selected");
                AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
                builder.setTitle("Do you want to finish the activity?");

                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    finish();
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.action_three:
                Log.d("Toolbar", "Option 3 selected");
                AlertDialog.Builder newSnack = new AlertDialog.Builder(TestToolbar.this);
                newSnack.setTitle("New Message");
                final EditText enterSnack = new EditText(TestToolbar.this);
                newSnack.setView(enterSnack);

                newSnack.setPositiveButton("Ok", (DialogInterface.OnClickListener) (dia, which) -> {

                    snack = enterSnack.getText().toString();
                });
                newSnack.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dia, which) -> {
                    dia.cancel();
                });

                AlertDialog dia = newSnack.create();
                dia.show();
                break;
            case R.id.about:
                Toast.makeText(this, "Version 1.0, by Evan Surtel", Toast.LENGTH_SHORT).show();

        }
        return true;
    }
}