package uz.example.instajclon.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import uz.example.instajclon.R;
import uz.example.instajclon.manager.AuthManager;
import uz.example.instajclon.manager.PrefsManager;

/**
 * In SplashActivity, user can visit SignInActivity or MainActivity
 */
public class SplashActivity extends BaseActivity {
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

        //if (Objects.equals(PrefsManager.getInstance(this).getData("login"), "true")) login = true;
        loadFCMToken();
        countDownTimer();
    }

    private void loadFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "Fetching FCM registration token failed");
                    return;
                }
                // Get new FCM registration token
                // Save it in locally to use later
                String token = task.getResult();
                Log.d(TAG, token.toString());
                PrefsManager.getInstance(context).storeDeviceToken(token.toString());
            }
        });
    }

    private void countDownTimer() {
        new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (AuthManager.isSignedIn()){
                    callMainActivity(context);
                }else{
                    callSignInActivity(context);
                }
            }
        }.start();
    }


}