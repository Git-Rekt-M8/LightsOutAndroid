package com.koalition.edu.lightsout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SettingsDialogActivity extends Activity {

    ImageView musicChecked;
    ImageView musicNotChecked;
    ImageView soundFxChecked;
    ImageView soundFxNotChecked;
    ImageView credits;
    ImageView resetProgress;
    ImageView close;

    ImageView resetQuestion;
    ImageView cancelButton;
    ImageView resetButton;

    ImageView resetStatus;

    SharedPreferences preferences;
    boolean isMusic;
    boolean isSoundFX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.activity_settings_dialog);

        musicChecked = (ImageView) findViewById(R.id.iv_music_check);
        musicNotChecked = (ImageView) findViewById(R.id.iv_music_not);;
        soundFxChecked = (ImageView) findViewById(R.id.iv_sound_fx_check);;
        soundFxNotChecked = (ImageView) findViewById(R.id.iv_sound_fx_not);;
        credits = (ImageView) findViewById(R.id.iv_credits);;
        resetProgress = (ImageView) findViewById(R.id.iv_reset);
        close = (ImageView) findViewById(R.id.iv_close);

        resetQuestion = (ImageView) findViewById(R.id.iv_reset_question);
        cancelButton = (ImageView) findViewById(R.id.iv_cancel_button);
        resetButton = (ImageView) findViewById(R.id.iv_reset_button);

        resetStatus = (ImageView) findViewById(R.id.iv_reset_status);



        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        isMusic = preferences.getBoolean("Music", true);
        System.out.println("PAG TRUE GG: " + preferences.getBoolean("Music", true));
        System.out.println("PAG FALSE GG: " + preferences.getBoolean("Music", false));
        isSoundFX = preferences.getBoolean("SoundFX", true);

        /*initial dialog**/
        if(isMusic)
        {
            musicNotChecked.setVisibility(View.INVISIBLE);
            musicChecked.setVisibility(View.VISIBLE);
        }
        else
        {
            musicChecked.setVisibility(View.INVISIBLE);
            musicNotChecked.setVisibility(View.VISIBLE);
        }

        if(isSoundFX)
        {
            soundFxNotChecked.setVisibility(View.INVISIBLE);
            soundFxChecked.setVisibility(View.VISIBLE);
        }
        else
        {
            soundFxChecked.setVisibility(View.INVISIBLE);
            soundFxNotChecked.setVisibility(View.VISIBLE);
        }

        musicChecked.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("wtf");
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        musicChecked.setVisibility(View.INVISIBLE);
                        musicNotChecked.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        editor.putBoolean("Music",false);
                        editor.commit();
                        isMusic = false;
                        return true;
                }
                return false;

            }
        });

        musicNotChecked.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        musicNotChecked.setVisibility(View.INVISIBLE);
                        musicChecked.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        editor.putBoolean("Music",true);
                        editor.commit();
                        isMusic = true;
                        return true;
                }
                return false;

            }
        });

        soundFxChecked.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        soundFxChecked.setVisibility(View.INVISIBLE);
                        soundFxNotChecked.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        editor.putBoolean("SoundFX",false);
                        editor.commit();
                        isSoundFX=false;
                        return true;
                }
                return false;

            }
        });

        soundFxNotChecked.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        soundFxNotChecked.setVisibility(View.INVISIBLE);
                        soundFxChecked.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        editor.putBoolean("SoundFX",true);
                        editor.commit();
                        isSoundFX = true;
                        return true;
                }
                return false;

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();

            }

        });

        hideResetDialog();
        hideResetStatusDialog();

        resetProgress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               RelativeLayout ly = (RelativeLayout) findViewById(R.id.settingsLayout);
                ly.setBackgroundResource(R.drawable.dialog_box_blank);
                hideSettingsDialog();
                showResetDialog();

/*                SharedPreferences preferences =  getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putBoolean("onboarding_complete", false);
                editor.commit();
                Toast.makeText(getBaseContext(), "Progress has been reset",
                        Toast.LENGTH_LONG).show();

                Intent i = new Intent(SettingsDialogActivity.this, MainActivity.class);
                startActivity(i);*/

            }

        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                // Get the shared preferences
                SharedPreferences preferences =
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putBoolean("onboarding_complete", false);
                editor.commit();
                editor.apply();

                editor.putInt("HighScore", 0); // STORE INITIAL SCORE OF 0
                editor.putInt("CurrentScore", 0);
                editor.putInt("Coins", 15000);
                editor.putBoolean("Music", true);
                editor.putBoolean("SoundFX", true);
                editor.putInt("powerup1Count", 0);
                editor.putInt("powerup2Count", 0);
                editor.putInt("powerup3Count", 0);
                editor.putInt("powerup4Count", 0);
                editor.putInt("CurrentDesign",0);
                editor.apply();

                editor.putBoolean("getsFreeCoins", true);
                editor.apply();
                editor.putBoolean("onboarding_complete", false);

                hideResetDialog();
                showResetStatusDialog();

                preferences =  getSharedPreferences("my_preferences", MODE_PRIVATE);
                editor = preferences.edit();
                editor.putBoolean("onboarding_complete", false);
                editor.apply();
                //finish();
                /*Intent i = new Intent(SettingsDialogActivity.this, MainActivity.class);
                startActivity(i);*/
            }

        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            finish();
                Intent intent=new Intent(getApplicationContext(), SettingsDialogActivity.class);
                startActivity(intent);

            }

        });

    }

    private void hideSettingsDialog() {
        musicChecked.setVisibility(View.INVISIBLE);
        musicNotChecked.setVisibility(View.INVISIBLE);
        soundFxChecked.setVisibility(View.INVISIBLE);
        soundFxNotChecked.setVisibility(View.INVISIBLE);
        credits.setVisibility(View.INVISIBLE);
        resetProgress.setVisibility(View.INVISIBLE);
    }
    private void hideResetDialog() {
        resetQuestion.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);

    }

    private void hideResetStatusDialog() {
        resetStatus.setVisibility(View.INVISIBLE);
    }
    private void showResetDialog() {
        resetQuestion.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.VISIBLE);
    }
    private void showResetStatusDialog() {
        resetStatus.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get the shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean("getsFreeCoins", false)){
            System.out.println("dito pumasok ang koya");
            int seconds = FreeCoinReceiver.TIMER_SEC;
            Intent broadcastIntent = new Intent(getBaseContext(), FreeCoinReceiver.class);
            PendingIntent pendingIntent
                    = PendingIntent.getBroadcast(getBaseContext(),
                    0,
                    broadcastIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            ((AlarmManager) getSystemService(Service.ALARM_SERVICE))
                    .set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + (seconds * 1000),
                            pendingIntent);

            editor.putBoolean("getsFreeCoins", false).apply();
            int currentCoins = sharedPreferences.getInt("Coins", 0);
            editor.putInt("Coins", currentCoins+100);
            editor.apply();
            /** toast */
            Toast.makeText(getBaseContext(), "YOU GET FREE 100 Coins",
                    Toast.LENGTH_LONG).show();
        }
    }
}


