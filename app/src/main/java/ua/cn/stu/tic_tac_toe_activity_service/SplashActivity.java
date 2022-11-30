package ua.cn.stu.tic_tac_toe_activity_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(runnable, TimeUnit.SECONDS.toMillis(2));
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private final Runnable runnable = () -> {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    };
}