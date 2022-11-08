package uz.example.instajclon.network;

import static uz.example.instajclon.network.Values.SERVER_KEY;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import uz.example.instajclon.model.PushNotification;

public interface ApiService {

    @Headers({"Authorization:"+SERVER_KEY,"Content-type:application/json"})

    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification pushNotification);

}
