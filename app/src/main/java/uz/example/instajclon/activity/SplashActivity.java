package uz.example.instajclon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;

import uz.example.instajclon.R;
/**
 * In SplashActivity, user can visit SignInActivity or MainActivity
 */
public class SplashActivity extends AppCompatActivity {
    String TAG = SplashActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initViews();
    }

    private void initViews() {
        countDownTimer();
    }

    private void countDownTimer() {
        new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                callSignInActivity();
            }
        }.start();
    }

    private void callSignInActivity() {
        Intent intent = new  Intent(this,SignInActivity.class);
        startActivity(intent);
        finish();
    }
}