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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;

import java.util.Objects;

import uz.example.instajclon.R;
import uz.example.instajclon.manager.AuthManager;
import uz.example.instajclon.manager.PrefsManager;
import uz.example.instajclon.manager.handler.AuthHandler;
import uz.example.instajclon.utils.Logger;
import uz.example.instajclon.utils.Utils;

/**
 * In SignInActivity, user can login with email and password
 */
public class SignInActivity extends BaseActivity {
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
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()){
                    firebaseSignIn(email,password);
                }else{
                    Utils.toast(context, getString(R.string.error_email_pass_empty));
                }
                //PrefsManager.getInstance(context).saveData("login","true");
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

    private void firebaseSignIn(String email, String password) {
        showLoading(this);
        AuthManager.signIn(email, password, new AuthHandler() {
            @Override
            public void onSuccess(String uid) {
                dismissLoading();
                Utils.toast(context, getString(R.string.str_signin_success));
                callMainActivity(context);
            }

            @Override
            public void onError(Exception exception,String code) {
                dismissLoading();
                switch (code) {

                    case "ERROR_INVALID_EMAIL" :
                        et_email.setError(getString(R.string.error_invalid_email));
                        et_email.requestFocus();
                        Log.d("@@@_error",code+" - "+exception);
                        Utils.toast(context,exception.getMessage());
                        break;
                    case "ERROR_WRONG_PASSWORD" :
                        et_password.setError(getString(R.string.error_wrong_password));
                        et_password.requestFocus();
                        Utils.toast(context,exception.getMessage());
                        break;

                    case "ERROR_USER_NOT_FOUND" :
                        et_email.setError(getString(R.string.error_user_not_found));
                        et_email.requestFocus();
                        Log.d("@@@_error",code+" - "+exception);
                        Utils.toast(context,exception.getMessage());
                        break;
                    default:
                        Log.d("@@@_error",code+" - "+exception);
                        Utils.toast(context,exception.getMessage());

                }

            }
        });

    }


    private void callSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}