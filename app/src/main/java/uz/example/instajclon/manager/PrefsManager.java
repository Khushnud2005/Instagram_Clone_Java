package uz.example.instajclon.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {
    private static PrefsManager prefsManager;
    private SharedPreferences sharedPreferences;

    public static PrefsManager getInstance(Context context) {
        if (prefsManager == null) {
            prefsManager = new PrefsManager(context);
        }
        return prefsManager;
    }

    private PrefsManager(Context context) {
        sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences != null)
            return sharedPreferences.getString(key, "");
        return "";
    }

    public void clearAll() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void storeDeviceToken(String token) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("device_token", token);
        prefsEditor.apply();
    }
    public String loadDeviceToken() {
        return sharedPreferences.getString("device_token", "");
    }
}
