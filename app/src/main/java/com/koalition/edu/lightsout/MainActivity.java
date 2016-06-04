package com.koalition.edu.lightsout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    ImageView playGameButton;
    ImageView shopButton;
    ImageView settingsButton;

    ImageView playGameButtonOnClick;
    ImageView shopButtonOnClick;
    ImageView settingsButtonOnClick;

    private MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;
    final static int BC_PENDINGINTENT = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.mainmenu);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Get the solo preferences (only for this activity)
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        // Get the shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // music initial
        editor.putBoolean("Music", true);
        editor.apply();
        editor.putBoolean("SoundFX", true);
        editor.apply();

        System.out.println("If contains music onCreate: " + preferences.contains("Music"));

        // Check if onboarding_complete is false
        if(!preferences.getBoolean("onboarding_complete",false)) {
            dbHelper = new DatabaseHelper(getBaseContext());
            dbHelper.deleteAll();
            dbHelper.insertPowerUp(new PowerUp(1, "Freeze Time", 300));
            dbHelper.insertPowerUp(new PowerUp(2, "Brownout", 500));
            dbHelper.insertPowerUp(new PowerUp(3, "Green House", 3000));
            dbHelper.insertPowerUp(new PowerUp(4, "Nipa Hut", 5000));


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
        }



        playGameButton = (ImageView) findViewById(R.id.iv_playgamewht);
        shopButton = (ImageView) findViewById(R.id.iv_shopwht);
        settingsButton = (ImageView) findViewById(R.id.iv_settingswht);

        playGameButtonOnClick = (ImageView) findViewById(R.id.iv_playgameblk);
        shopButtonOnClick = (ImageView) findViewById(R.id.iv_shopblk);
        settingsButtonOnClick = (ImageView) findViewById(R.id.iv_settingsblk);

        /* hide onclick buttons**/
        playGameButtonOnClick.setVisibility(View.INVISIBLE);
        shopButtonOnClick.setVisibility(View.INVISIBLE);
        settingsButtonOnClick.setVisibility(View.INVISIBLE);

        playGameButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        playGameButton.setVisibility(View.INVISIBLE);
                        playGameButtonOnClick.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        playGameButtonOnClick.setVisibility(View.INVISIBLE);
                        playGameButton.setVisibility(View.VISIBLE);
                        // Get the solo preferences (only for this activity)
                        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                        if(!preferences.getBoolean("onboarding_complete",false)) {
                            // Start the onboarding Activity
                            Intent onboarding = new Intent(getBaseContext(), OnboardingActivity.class);
                            startActivity(onboarding);
                        }
                        else {
                            Intent intent=new Intent(getApplicationContext(), DifficultySelectionActivity.class);
                            startActivity(intent);
                        }
                        return true;
                }
                return false;

            }
        });

        shopButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        shopButton.setVisibility(View.INVISIBLE);
                        shopButtonOnClick.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        shopButtonOnClick.setVisibility(View.INVISIBLE);
                        shopButton.setVisibility(View.VISIBLE);

                        Intent intent=new Intent(MainActivity.this, ShopMainActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;

            }
        });

        settingsButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        settingsButton.setVisibility(View.INVISIBLE);
                        settingsButtonOnClick.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        settingsButtonOnClick.setVisibility(View.INVISIBLE);
                        settingsButton.setVisibility(View.VISIBLE);

                        Intent intent=new Intent(getApplicationContext(), SettingsDialogActivity.class);
                        startActivity(intent);

                        return true;
                }
                return false;

            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        MyApplication.activityResumed();
        // Get the shared preferences
//        preferences =  getSharedPreferences("my_preferences", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        System.out.println("ANO DAW PO: " + sharedPreferences.getBoolean("Music", false));
        System.out.println("PAG ETO TRU >:( " + sharedPreferences.getBoolean("Music", true));
        System.out.println("iyak if false: " + sharedPreferences.contains("Music"));
        System.out.println("free coins dapat true: " + sharedPreferences.contains("getsFreeCoins"));

        // MUSIC TUGS TUGS

        if(sharedPreferences.getBoolean("Music", false)) {
//                mediaPlayer.setOnPreparedListener(this);
//                mediaPlayer.prepareAsync();
//            if(!mediaPlayer.isPlaying())
            mediaPlayer.setVolume(1.0f,1.0f);

        } else
        {
            mediaPlayer.setVolume(0.0f, 0.0f);
//            mediaPlayer.stop();
//            mediaPlayer.reset();

//            mediaPlayer.release();
        }

        // check if new coins
        if(sharedPreferences.getBoolean("getsFreeCoins", false)){
            System.out.println("dito pumasok ang koya");
            int seconds = FreeCoinReceiver.TIMER_SEC;
            Intent broadcastIntent = new Intent(getBaseContext(), FreeCoinReceiver.class);
            PendingIntent pendingIntent
                    = PendingIntent.getBroadcast(getBaseContext(),
                    BC_PENDINGINTENT,
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
        /** checks if opened from notification */
//        Intent intent = this.getIntent();
//        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey("getsFreeCoins")) {
//            if(intent.getExtras().getBoolean("getsFreeCoins")==true) {
//                int seconds = 3;
//                Intent broadcastIntent = new Intent(getBaseContext(), FreeCoinReceiver.class);
//                PendingIntent pendingIntent
//                        = PendingIntent.getBroadcast(getBaseContext(),
//                        BC_PENDINGINTENT,
//                        broadcastIntent,
//                        PendingIntent.FLAG_CANCEL_CURRENT);
//
//                ((AlarmManager) getSystemService(Service.ALARM_SERVICE))
//                        .set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                                SystemClock.elapsedRealtime() + (seconds * 1000),
//                                pendingIntent);
//
//                intent.removeExtra("getsFreeCoins");
//            }
//
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPrepared(MediaPlayer player) {
        mediaPlayer.start();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mediaPlayer.stop();
//    }

}
