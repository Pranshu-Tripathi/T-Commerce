package com.example.t_commerce.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.t_commerce.R;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ImageView imageView = findViewById(R.id.logo);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce_image_splash);
        imageView.setAnimation(animation);
        imageView.startAnimation(animation);


        Date d = new Date();
        Log.i("Date", String.valueOf(d.getDate()));

        Timer t2= new Timer();
        t2.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}