package uz.example.instajclon.manager;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import uz.example.instajclon.manager.handler.AuthHandler;

public class AuthManager {
    static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static boolean isSignedIn(){
        return currentUser() != null;
    }

    private static FirebaseUser currentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public static void signIn(String email, String password, AuthHandler handler){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String uid = currentUser().getUid();
                    handler.onSuccess(uid);
                }else{
                    String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                    handler.onError(task.getException(),errorCode);
                }
            }
        });
    }
    public static void signUp(String email, String password, AuthHandler handler){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String uid = currentUser().getUid();
                    handler.onSuccess(uid);
                }else{
                    String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                    handler.onError(task.getException(),errorCode);
                }
            }
        });
    }
    public static void signOut(){
        firebaseAuth.signOut();
    }
}
