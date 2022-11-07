package uz.example.instajclon.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import uz.example.instajclon.R;
import uz.example.instajclon.model.ScreenSize;

public class Utils {
    @SuppressLint("HardwareIds")
    public static String getDeviceID(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
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
    public static void dialogDouble(Context context ,String title,DialogListener callback) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.view_dialog_double);
        dialog.setCanceledOnTouchOutside(true);
        TextView d_title = dialog.findViewById(R.id.d_title);
        TextView d_confirm = dialog.findViewById(R.id.d_confirm);
        TextView d_cancel = dialog.findViewById(R.id.d_cancel);
                d_title.setText(title);
        d_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onCallback(true);
            }
        });
        d_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onCallback(false);
            }
        });
        dialog.show();
    }
}
