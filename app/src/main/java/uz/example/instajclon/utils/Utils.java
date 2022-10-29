package uz.example.instajclon.utils;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import uz.example.instajclon.model.ScreenSize;

public class Utils {

    public static void toast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    public static ScreenSize screenSize(Application context) {
        DisplayMetrics displayMetrics = new  DisplayMetrics();
        WindowManager windowsManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowsManager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        return new ScreenSize(deviceWidth, deviceHeight);
    }
}
