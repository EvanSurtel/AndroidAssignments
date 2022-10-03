package com.example.androidassignments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ListItemsActivity extends AppCompatActivity {


        /*private  val CAMERA_PERMISSION_CODE = 1;
        private const val CAMERA = 2;*/

    public static final int PERM_CODE = 11;
    public static final int REQUEST_CODE = 10;

    ImageButton camera;
    Switch swi;
    CheckBox checkBox;

    protected static final String TAG = "ListItemsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "In OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        camera = findViewById(R.id.camButton);
        swi = findViewById(R.id.checkSwitch);
        checkBox = findViewById(R.id.checkBox);

        camera.setOnClickListener(new View.OnClickListener() {

            /*if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                    ) ==PackageManager.PERMISSION_GRANTED
            );*/
            @Override
            public void onClick(View v) {
                askCameraPermissions();
                /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);*/
            }
        });

        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    Toast.makeText(ListItemsActivity.this, R.string.SwitchOn, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ListItemsActivity.this, R.string.SwitchOff, Toast.LENGTH_LONG).show();
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                builder.setTitle(R.string.FinishActivityPrompt);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    Intent resultIntent = new Intent(ListItemsActivity.this, MainActivity.class  );
                    resultIntent.putExtra("Response", "Here is my response");
                    setResult(Activity.RESULT_OK, resultIntent);


                    finish();


                });

                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                if(isChecked){
                    alertDialog.show();
                }else{

                }
            }
        });


    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(ListItemsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ListItemsActivity.this,new String[] {Manifest.permission.CAMERA}, PERM_CODE);
        }else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(ListItemsActivity.this, R.string.PermissionRequired, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            camera.setImageBitmap(bitmap);
            }
    }


    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
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

    public void print(String string) {
        Toast.makeText(ListItemsActivity.this, string, Toast.LENGTH_LONG).show();
    }

}