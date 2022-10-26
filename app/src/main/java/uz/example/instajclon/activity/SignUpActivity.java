package uz.example.instajclon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import uz.example.instajclon.R;
/**
 * In SignUpActivity, user can signup with fullname, email, password
 */
public class SignUpActivity extends AppCompatActivity {
    String TAG = SignUpActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
}