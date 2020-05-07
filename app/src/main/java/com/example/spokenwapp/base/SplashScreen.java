package com.example.spokenwapp.base;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.spokenwapp.R;
import com.example.spokenwapp.services.LoadLocalVideoService;

import java.util.ArrayList;
import java.util.List;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

public class SplashScreen extends DaggerAppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 200;
    String [] listOfAppPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.
        setContentView(R.layout.activity_main);

        // If All permission is enabled successfully then this block will execute.
        if(CheckingPermissionIsEnabledOrNot()) {
            Toast.makeText(SplashScreen.this, "All Permissions Granted Successfully",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SplashScreen.this, LoadLocalVideoService.class);
            startService(intent);
        }

        // If, If permission is not enabled then else condition will execute.
        else {

            //Calling method to enable permission.
            RequestMultiplePermission();

        }

        int SPLASH_SCREEN_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, SpokenMainScreen.class);
                //Intent is used to switch from one activity to another.
                startActivity(intent);
                //invoke the SecondActivity.
                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(SplashScreen.this, new String[]
                {
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECORD_AUDIO
                }, PERMISSIONS_REQUEST_CODE);

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean ReadExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean WriteExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean SendSMSPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean RecordAudioPermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                if (CameraPermission && ReadExternalStoragePermission && WriteExternalStoragePermission &&
                        SendSMSPermission && RecordAudioPermission) {

                    Toast.makeText(SplashScreen.this, "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SplashScreen.this, "Permission Denied", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

}
