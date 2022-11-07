package uz.example.instajclon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;

import uz.example.instajclon.R;
import uz.example.instajclon.manager.AuthManager;
import uz.example.instajclon.manager.DBManager;
import uz.example.instajclon.manager.PrefsManager;
import uz.example.instajclon.manager.handler.AuthHandler;
import uz.example.instajclon.manager.handler.DBUserHandler;
import uz.example.instajclon.model.User;
import uz.example.instajclon.utils.Logger;
import uz.example.instajclon.utils.Utils;

/**
 * In SignUpActivity, user can signup with fullname, email, password
 */
public class SignUpActivity extends BaseActivity {
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
                String fullname = et_fullname.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String cpassword = et_cpassword.getText().toString().trim();

                if(!fullname.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    if (cpassword.equals(password)){
                        User user = new User(fullname, email, password,"");
                        firebaseSignUp(user);
                    }else {
                        Utils.toast(context,"Password Confirmation Failed !");
                    }
                }else{
                    Utils.toast(context,"Please Fil All Fields");
                }
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


    private void firebaseSignUp(User user) {
        showLoading(this);
        AuthManager.signUp(user.getEmail(), user.getPassword(), new AuthHandler() {
            @Override
            public void onSuccess(String uid) {
                user.setUid(uid);
                storeUserToDB(user);
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
                        Log.d("@@@_error",code+" - "+exception);
                        Utils.toast(context,exception.getMessage());
                        break;
                    case "ERROR_WEAK_PASSWORD" :
                        et_password.setError(getString(R.string.error_weak_password));
                        et_password.requestFocus();
                        Log.d("@@@_error",code+" - "+exception);
                        Utils.toast(context,exception.getMessage());
                        break;
                    case "ERROR_CREDENTIAL_ALREADY_IN_USE" :
                        et_email.setError(exception.getMessage());
                        et_email.requestFocus();
                        Log.d("@@@_error",code+" - "+exception);
                        Utils.toast(context,exception.getMessage());
                        break;
                    case "ERROR_EMAIL_ALREADY_IN_USE" :
                        et_email.setError(getString(R.string.error_user_exists));
                        et_email.requestFocus();
                        Utils.toast(context,exception.getMessage());
                        break;

                    default:
                        Utils.toast(context,getString(R.string.str_signup_failed));

                }

            }
        });
    }

    private void storeUserToDB(User user) {

        user.setDevice_token(PrefsManager.getInstance(context).loadDeviceToken());
        user.setDevice_id(Utils.getDeviceID(this));

        DBManager.storeUser(user, new DBUserHandler() {
            @Override
            public void onSuccess(User user) {
                dismissLoading();
                Utils.toast(context,getString(R.string.str_signup_success));
                callMainActivity(context);
            }

            @Override
            public void onError(Exception exception) {
                Utils.toast(context,getString(R.string.str_signup_failed));
            }
        });
    }
}