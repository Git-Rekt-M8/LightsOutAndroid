package com.koalition.edu.lightsout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.TestCase;

import org.w3c.dom.Text;

public class SettingsDialogActivity extends Activity {

    TextView settingsTitle;
    TextView music;
    TextView sounds_fx;
    TextView credits;
    TextView reset_progress;
    TextView reset_question;
    TextView reset_cancel;
    TextView reset_confirm;
    TextView reset_done;

    TextView app_version;
    TextView name_caingles;
    TextView name_koa;
    TextView name_simeon;
    TextView name_ngo;
    TextView name_dwibbit;

    Typeface pixelFont;

    ImageView music_status;
    ImageView sound_fx_status;
    ImageView close;
    ImageView credits_close;

    ImageView close_done;

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

        settingsTitle = (TextView) findViewById(R.id.tv_settings);
        music = (TextView) findViewById(R.id.tv_music);
        sounds_fx = (TextView) findViewById(R.id.tv_sound_fx);
        credits = (TextView) findViewById(R.id.tv_credits);
        reset_progress = (TextView) findViewById(R.id.tv_reset_progress);
        close = (ImageView) findViewById(R.id.iv_close);



        music_status = (ImageView) findViewById(R.id.iv_music_status);
        sound_fx_status = (ImageView) findViewById(R.id.iv_sound_fx_status);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        settingsTitle.setTypeface(pixelFont);
        music.setTypeface(pixelFont);
        sounds_fx.setTypeface(pixelFont);
        credits.setTypeface(pixelFont);
        reset_progress.setTypeface(pixelFont);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        isMusic = preferences.getBoolean("Music", true);
        isSoundFX = preferences.getBoolean("SoundFX", true);

        /*initial dialog**/
        if(isMusic)
        {
            music_status.setImageResource(R.drawable.setting_check);
        }
        else
        {
            music_status.setImageResource(R.drawable.setting_not);
        }

        if(isSoundFX)
        {
            sound_fx_status.setImageResource(R.drawable.setting_check);
        }
        else
        {
            sound_fx_status.setImageResource(R.drawable.setting_not);
        }

        music_status.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        if (isMusic) {
                            music_status.setImageResource(R.drawable.setting_not);
                        } else {
                            music_status.setImageResource(R.drawable.setting_check);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        if (isMusic) {
                            editor.putBoolean("Music", false);
                            isMusic = false;
                            editor.commit();
                            editor.apply();
                            AudioPlayer.pauseMusic();
                        } else {
                            editor.putBoolean("Music", true);
                            isMusic = true;
                            editor.commit();
                            editor.apply();
                            AudioPlayer.playMusic(getApplicationContext(), R.raw.mainmenu);
                        }


                        return true;
                }
                return false;

            }
        });

        music.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        if (isMusic) {
                            music_status.setImageResource(R.drawable.setting_not);
                        } else {
                            music_status.setImageResource(R.drawable.setting_check);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        if (isMusic) {
                            editor.putBoolean("Music", false);
                            isMusic = false;
                            editor.commit();
                            editor.apply();
                            AudioPlayer.pauseMusic();
                        } else {
                            editor.putBoolean("Music", true);
                            isMusic = true;
                            editor.commit();
                            editor.apply();
                            AudioPlayer.playMusic(getApplicationContext(), R.raw.mainmenu);
                        }


                        return true;
                }
                return false;

            }
        });

        sounds_fx.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        if (isSoundFX) {
                            sound_fx_status.setImageResource(R.drawable.setting_not);
                        } else {
                            sound_fx_status.setImageResource(R.drawable.setting_check);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        if (isSoundFX) {
                            editor.putBoolean("SoundFX", false);
                            isSoundFX = false;
                        } else {
                            editor.putBoolean("SoundFX", true);
                            isSoundFX = true;
                        }

                        editor.commit();
                        editor.apply();
                        return true;
                }
                return false;

            }
        });

        sound_fx_status.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        if (isSoundFX) {
                            sound_fx_status.setImageResource(R.drawable.setting_not);
                        } else {
                            sound_fx_status.setImageResource(R.drawable.setting_check);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        if (isSoundFX) {
                            editor.putBoolean("SoundFX", false);
                            isSoundFX = false;
                        } else {
                            editor.putBoolean("SoundFX", true);
                            isSoundFX = true;
                        }

                        editor.commit();
                        editor.apply();
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

        credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.credits_dialog);
                app_version = (TextView) findViewById(R.id.tv_app_version);
                name_caingles = (TextView) findViewById(R.id.credits_caingles);
                name_koa = (TextView) findViewById(R.id.credits_koa);
                name_simeon = (TextView) findViewById(R.id.credits_simeon);
                name_ngo = (TextView) findViewById(R.id.credits_ngo);
                name_dwibbit = (TextView) findViewById(R.id.credits_dwibbit);
                credits_close = (ImageView) findViewById(R.id.iv_credits_close);


                app_version.setTypeface(pixelFont);
                name_caingles.setTypeface(pixelFont);
                name_koa.setTypeface(pixelFont);
                name_simeon.setTypeface(pixelFont);
                name_ngo.setTypeface(pixelFont);
                name_dwibbit.setTypeface(pixelFont);

                credits_close.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        finish();

                    }

                });
            }
        });

        reset_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.reset_dialog);
                reset_question = (TextView) findViewById(R.id.tv_reset_question);
                reset_cancel = (TextView) findViewById(R.id.tv_reset_question_cancel);
                reset_confirm = (TextView) findViewById(R.id.tv_reset_question_reset);
                reset_progress.setTypeface(pixelFont);
                reset_question.setTypeface(pixelFont);
                reset_cancel.setTypeface(pixelFont);
                reset_confirm.setTypeface(pixelFont);


                reset_confirm.setOnClickListener(new View.OnClickListener() { //* CHANGE RESET DATA Here*/
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.reset_done);
                        reset_done = (TextView) findViewById(R.id.tv_reset_done);
                        reset_done.setTypeface(pixelFont);
                        close_done = (ImageView) findViewById(R.id.iv_close_reset_done);

                        // Get the shared preferences
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.putBoolean("onboarding_complete", false);
                        editor.commit();
                        editor.apply();

                        editor.putInt("HighScore", 0); // Not used anymore, deprecated in version 1.0.3 HAHA
                        editor.putInt("EasyHighScore", 0);
                        editor.putInt("MediumHighScore", 0);
                        editor.putInt("InsaneHighScore", 0);
                        editor.putInt("CurrentScore", 0);
                        editor.putInt("Coins", 0);
                       // editor.putBoolean("Music", true);
                      //  editor.putBoolean("SoundFX", true);
                        editor.putInt("powerup1Count", 0);
                        editor.putInt("powerup2Count", 0);
                        editor.putInt("powerup3Count", 0);
                        editor.putInt("powerup4Count", 0);
                        editor.putInt("CurrentDesign", 0);
                        editor.putBoolean("getsFreeCoins", true);
                        editor.putBoolean("first_run", true);
                        editor.apply();
                       // editor.putBoolean("onboarding_complete", false);

                        preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.putBoolean("onboarding_complete", false);
                        editor.apply();

                        close_done.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                finish();

                            }

                        });
                    }



                });

                reset_cancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        finish();
                        Intent intent = new Intent(getApplicationContext(), SettingsDialogActivity.class);
                        startActivity(intent);

                    }

                });

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        AudioPlayer.playMusic(getApplicationContext(), R.raw.mainmenu);
        // Get the shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean("getsFreeCoins", false)){
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
            Toast toast = Toast.makeText(getBaseContext(), "You get free 100 coins!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, toast.getXOffset() / 2, toast.getYOffset() / 2);

            TextView textView = new TextView(getApplicationContext());
            textView.setBackgroundColor(Color.DKGRAY);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/pixelmix.ttf");
            textView.setTypeface(typeface);
            textView.setPadding(10, 10, 10, 10);
            textView.setText("You get free 100 coins!");

            toast.setView(textView);
            toast.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioPlayer.pauseMusic();
    }
}


