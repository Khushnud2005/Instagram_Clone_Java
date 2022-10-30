package uz.example.instajclon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.WindowManager;

import java.util.Objects;

import uz.example.instajclon.R;
import uz.example.instajclon.manager.PrefsManager;

/**
 * In SplashActivity, user can visit SignInActivity or MainActivity
 */
public class SplashActivity extends AppCompatActivity {
    String TAG = SplashActivity.class.getSimpleName();

    boolean login = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initViews();
    }

    private void initViews() {

        if (Objects.equals(PrefsManager.getInstance(this).getData("login"), "true")){
            login = true;
        }
        countDownTimer();
    }

    private void countDownTimer() {
        new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (login){
                    callMainActivity();
                }else{
                    callSignInActivity();
                }
            }
        }.start();
    }

    private void callMainActivity() {
        Intent intent = new  Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void callSignInActivity() {
        Intent intent = new  Intent(this,SignInActivity.class);
        startActivity(intent);
        finish();
    }
}