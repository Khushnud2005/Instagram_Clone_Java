package uz.example.instajclon.manager;


import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import uz.example.instajclon.manager.handler.DBUserHandler;
import uz.example.instajclon.manager.handler.DBUsersHandler;
import uz.example.instajclon.model.User;

public class DBManager {
    static final String USER_PATH = "users";
    static final String POST_PATH = "posts";
    static final String FEED_PATH = "feeds";
    static final String FOLLOWING_PATH = "following";
    static final String FOLLOWERS_PATH = "followers";

    @SuppressLint("StaticFieldLeak")
    static FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static void storeUser(User user, DBUserHandler handler){
        database.collection(USER_PATH).document(user.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                handler.onSuccess(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }

    public static void loadUser(String uid ,DBUserHandler handler) {
        database.collection(USER_PATH).document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fullname = snapshot.getString("fullname");
                    String email = snapshot.getString("email");
                    String userImg = snapshot.getString("userImg");

                    User user = new User(fullname, email, userImg);
                    user.setUid(uid);
                    handler.onSuccess(user);
                } else {
                    handler.onSuccess(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }

    public static void loadUsers(DBUsersHandler handler) {
        database.collection(USER_PATH).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<User> users = new ArrayList<User>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String uid = document.getString("uid");
                        String fullname = document.getString("fullname");
                        String email = document.getString("email");
                        String userImg = document.getString("userImg");


                        User user = new User(fullname, email, userImg);
                        user.setUid(uid);
                        users.add(user);
                    }
                    handler.onSuccess(users);
                } else {
                    handler.onError(task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }
    public static void updateUserImage(String userImg) {
        String uid = AuthManager.currentUser().getUid();
        database.collection(USER_PATH).document(uid).update("userImg", userImg);
    }
}
