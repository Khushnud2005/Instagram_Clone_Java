package uz.example.instajclon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uz.example.instajclon.R;
import uz.example.instajclon.manager.PrefsManager;

/**
 * In SignInActivity, user can login with email and password
 */
public class SignInActivity extends AppCompatActivity {
    String TAG = SignInActivity.class.getSimpleName();
    EditText et_email;
    EditText et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initViews();
    }

    private void initViews() {
        et_email = findViewById(R.id.et_emailSI);
        et_password = findViewById(R.id.et_passwordSI);
        Button b_signin = findViewById(R.id.btn_signin);
        Context context = this;
        b_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMainActivity();
                PrefsManager.getInstance(context).saveData("login","true");
            }
        });
        TextView tv_signup = findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSignUpActivity();
            }
        });

    }

    private void callMainActivity() {
        Intent intent = new  Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void callSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}