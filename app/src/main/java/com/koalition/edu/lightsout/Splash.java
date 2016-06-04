package com.koalition.edu.lightsout;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Kingston on 3/5/2016.
 */
public class Splash extends Activity {
    DatabaseHelper dbHelper;
    final static int BC_PENDINGINTENT = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashh);

        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final Button start = (Button) findViewById(R.id.button_start);
        final Animation rotateAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation slideDownAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_down);
        final Animation slideUpAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_up);
        final Animation fadeOutAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);
        final Animation fadeInAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_in);
        start.setVisibility(View.GONE);
        iv.startAnimation(fadeInAnim);
        fadeInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.startAnimation(fadeOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                start.setVisibility(View.VISIBLE);
                start.startAnimation(slideUpAnim);
                //finish();
                //Intent i = new Intent(Splash.this, MainActivity.class);
                //startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
            }

        });
    }
}
