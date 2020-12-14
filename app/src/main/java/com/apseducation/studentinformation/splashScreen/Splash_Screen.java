package com.apseducation.studentinformation.splashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.apseducation.studentinformation.R;
import com.apseducation.studentinformation.profile.ui.Student_Login;

public class Splash_Screen extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(Splash_Screen.this, Student_Login.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
