package uz.example.instajclon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import uz.example.instajclon.R;
/**
 * In SignUpActivity, user can signup with fullname, email, password
 */
public class SignUpActivity extends AppCompatActivity {
    String TAG = SignUpActivity.class.getSimpleName();
    EditText et_fullname;
    EditText et_password;
    EditText et_email;
    EditText et_cpassword; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews();
    }

    private void initViews() {
        et_fullname = findViewById(R.id.et_fullnameSU);
        et_email = findViewById(R.id.et_emailSU);
        et_password = findViewById(R.id.et_passwordSU);
        et_cpassword = findViewById(R.id.et_cpasswordSU);

        Button btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv_signin = findViewById(R.id.tv_signin);
        tv_signin.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}