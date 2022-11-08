package uz.example.instajclon.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHttp {
public static boolean IS_TESTER = true;
    private static String  SERVER_DEVELOPMENT = "https://fcm.googleapis.com";
    private static String SERVER_PRODUCTION = "https://fcm.googleapis.com";
    static String server() {
        if (IS_TESTER)
            return SERVER_DEVELOPMENT;
        return SERVER_PRODUCTION;
    }
    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(server())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static ApiService apiService = retrofit.create(ApiService.class);









}