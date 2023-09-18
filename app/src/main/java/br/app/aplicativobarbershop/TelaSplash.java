package br.app.aplicativobarbershop;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class TelaSplash extends AppCompatActivity {

    private ImageView imgSplash;
    private TextView txtFrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_splash);

        getSupportActionBar().hide();

        animacao();
    }

    private void animacao() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade);
        imgSplash = findViewById(R.id.imgSplash);
        txtFrase = findViewById(R.id.txtFrase);
        anim.reset();

        txtFrase.clearAnimation();
        txtFrase.startAnimation(anim);

        imgSplash.clearAnimation();
        imgSplash.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent(TelaSplash.this, FormLogin.class);
                startActivity(intent);

                TelaSplash.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}