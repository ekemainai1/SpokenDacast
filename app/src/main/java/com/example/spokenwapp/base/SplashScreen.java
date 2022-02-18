package com.example.spokenwapp.base;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import com.example.spokenwapp.R;
import com.example.spokenwapp.services.SpokenMediaBrowserService;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

public class SplashScreen extends DaggerAppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.
        setContentView(R.layout.activity_main);


        int SPLASH_SCREEN_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, SpokenMainScreen.class);
                //Intent is used to switch from one activity to another.
                startActivity(intent);

                // Start service for mediaBrowser
                Intent intentBrowserService = new Intent(SplashScreen.this, SpokenMediaBrowserService.class);
                startService(intentBrowserService);
                //invoke the SecondActivity.
                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }



}
