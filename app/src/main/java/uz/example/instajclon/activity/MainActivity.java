package uz.example.instajclon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import uz.example.instajclon.R;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}