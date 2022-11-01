package uz.example.instajclon.manager.handler;

import com.google.firebase.auth.FirebaseAuthException;

public interface AuthHandler {
    void onSuccess(String uid);
    void onError(Exception exception,String code);
}
