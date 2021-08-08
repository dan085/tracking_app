package com.transvip.test.ui.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.transvip.test.databinding.ActivityMainSplashScreenBinding;
import com.transvip.test.ui.introduction.MainActivityIntroducion;

public class MainActivitySplashScreen extends AppCompatActivity {
    private Integer SPLASH_DISPLAY_LENGTH = 4000;
    private ActivityMainSplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainSplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Handler handler = new Handler(Looper.getMainLooper());
        ///Vista que muestra e logo de la aplicación
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivitySplashScreen.this, MainActivityIntroducion.class));
                MainActivitySplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
