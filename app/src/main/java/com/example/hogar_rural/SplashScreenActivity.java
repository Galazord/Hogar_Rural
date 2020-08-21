package com.example.hogar_rural;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    //--> VARIABLES
    private ImageView ivSplash_background1, ivSplash_background2, ivSplash_logo;
    private TextView tvSplash_txt_hogar, tvSplash_txt_rural;
    private MediaPlayer soundStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Iniciar componentes
        initComponent();

    }

    // Iniciar componentes
    private void initComponent() {

        // Relaccionar la parte gráfica con las variables
        ivSplash_background1 = (ImageView) findViewById(R.id.ivSplash_background1);
        ivSplash_background2 = (ImageView) findViewById(R.id.ivSplash_background2);
        ivSplash_logo = (ImageView) findViewById(R.id.ivSplash_logo);
        tvSplash_txt_hogar = (TextView) findViewById(R.id.tvSplash_txt_hogar);
        tvSplash_txt_rural = (TextView) findViewById(R.id.tvSplash_txt_rural);
        soundStart = MediaPlayer.create(this, R.raw.sound_birds_1);

        // animación logo y texto
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                soundStart.start();
                YoYo.with(Techniques.FadeIn).duration(900).playOn(ivSplash_logo);
                YoYo.with(Techniques.FadeInLeft).duration(900).playOn(tvSplash_txt_hogar);
                YoYo.with(Techniques.FadeInRight).duration(900).playOn(tvSplash_txt_rural);
            }
        }, 100);

        // Activar fondo 2
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ivSplash_background2.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(900).playOn(ivSplash_background2);
            }
        }, 500);

        // Pasar al MainActivity usando un Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // SPLASH 2.- Comenzar con el splash declarando el intent (paso siguiente en el manifest)
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 2000);

    }
}