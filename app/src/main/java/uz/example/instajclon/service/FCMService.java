package uz.example.instajclon.service;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class FCMService extends FirebaseMessagingService {
    String TAG = FCMService.class.getSimpleName();
    public static final String INTENT_FILTER = "PUSH EVENT";
    public static final String KEY_ACTION = "action";
    public static final String KEY_MESSAGE = "message";
    public static final String ACTION_SHOW_MESSAGE = "show_message";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Intent intent = new Intent("com.google.firebase.MESSAGING_EVENT");
        intent.putExtra("title",Objects.requireNonNull(message.getNotification()).getTitle());
        intent.putExtra("message",Objects.requireNonNull(message.getNotification()).getBody());
        sendBroadcast(intent);

    }
}
