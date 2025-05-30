package com.srujal.pdfreader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.srujal.pdfreader.databinding.ActivitySplashBinding;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    Animation fadein,translateb,translatet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fadein = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.fadein);
        translateb = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.translate_scale_bottom);
        translatet = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.translate_scale_top);

        binding.logo.setAnimation(translatet);
        binding.appName.setAnimation(translateb);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
        },3000);
    }
}