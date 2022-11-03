package uz.example.instajclon.manager;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import uz.example.instajclon.manager.handler.StorageHandler;
import uz.example.instajclon.utils.Logger;

public class StorageManager {
    static final String USER_PHOTO_PATH = "users";
    static final String POST_PHOTO_PATH = "posts";

    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = storage.getReference();

    public static void uploadUserPhoto(Uri uri, StorageHandler handler) {

        String filename = AuthManager.currentUser().getUid() + ".png";
        UploadTask uploadTask = storageRef.child(USER_PHOTO_PATH).child(filename).putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                Task<Uri> result = Objects.requireNonNull(Objects.requireNonNull(snapshot.getMetadata()).getReference()).getDownloadUrl();
                result.addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object it) {
                        String imgUrl = it.toString();
                        handler.onSuccess(imgUrl);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handler.onError(e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }

    public static void uploadPostPhoto(Uri uri, StorageHandler handler) {
        String filename = AuthManager.currentUser().getUid() + "_" + System.currentTimeMillis() + ".png";
        UploadTask uploadTask = storageRef.child(POST_PHOTO_PATH).child(filename).putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                Task<Uri> result = Objects.requireNonNull(Objects.requireNonNull(snapshot.getMetadata()).getReference()).getDownloadUrl();
                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imgUrl = uri.toString();
                        handler.onSuccess(imgUrl);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handler.onError(e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }
}
