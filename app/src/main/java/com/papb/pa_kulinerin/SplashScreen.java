package com.papb.pa_kulinerin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    TextView wait1, wait2;
    int tes = 0;
    int thread = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        wait1 = findViewById(R.id.tv_wait);
        wait2 = findViewById(R.id.tv_wait2);
        wait2.setVisibility(View.INVISIBLE);

        //Thread untuk mengganti teks
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (tes<7){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("Main Activity", "thread ke "+ thread);
                    wait1.post(new Runnable() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void run() {
                            if(tes == 4){
                                wait1.setVisibility(View.INVISIBLE);
                                wait2.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    thread++;
                    tes++;
                }
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("LoggedIn", false);
                startActivity(intent);
                finish();
            }
        }).start();
    }
}