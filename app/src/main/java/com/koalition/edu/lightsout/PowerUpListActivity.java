package com.koalition.edu.lightsout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PowerUpListActivity extends Activity {

    DatabaseHelper dbHelper;
    ImageView powerUpHead;
    ImageView backButton;
    ImageView backButtonOnClick;
    ImageView freezePowerUpButton;
    ImageView freezePowerUpButtonOnClick;
    ImageView brownoutPowerUpButton;
    ImageView brownoutPowerUpButtonOnClick;

    TextView playerBalance;

    Typeface pixelFont;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_up_list);

        dbHelper = new DatabaseHelper(getBaseContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        powerUpHead = (ImageView) findViewById(R.id.powerup_head);
        backButton = (ImageView) findViewById(R.id.back_button);;
        backButtonOnClick = (ImageView) findViewById(R.id.back_button_clicked);;
        freezePowerUpButton = (ImageView) findViewById(R.id.freeze_powerup);;
        freezePowerUpButtonOnClick = (ImageView) findViewById(R.id.freeze_powerup_cliicked);;
        brownoutPowerUpButton = (ImageView) findViewById(R.id.brownout_powerup);;
        brownoutPowerUpButtonOnClick = (ImageView) findViewById(R.id.brownout_powerup_clicked);

        playerBalance = (TextView) findViewById(R.id.player_balance);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        playerBalance.setTypeface(pixelFont);

        /* hide onclick buttons**/
        freezePowerUpButtonOnClick.setVisibility(View.INVISIBLE);
        brownoutPowerUpButtonOnClick.setVisibility(View.INVISIBLE);
        backButtonOnClick.setVisibility(View.INVISIBLE);

        freezePowerUpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        freezePowerUpButton.setVisibility(View.INVISIBLE);
                        freezePowerUpButtonOnClick.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        freezePowerUpButtonOnClick.setVisibility(View.INVISIBLE);
                        freezePowerUpButton.setVisibility(View.VISIBLE);

                        int currentCoins = sharedPreferences.getInt("Coins", 0);
                        if (currentCoins > dbHelper.queryPowerUp(1).getPrice()) {
                            editor.putInt("Coins", currentCoins - dbHelper.queryPowerUp(1).getPrice());
                            editor.putInt("powerup1Count", sharedPreferences.getInt("powerup1Count", 0)+1);
                            editor.apply();
                            playerBalance.setText(String.valueOf(sharedPreferences.getInt("Coins", 0)));


                            Toast.makeText(getBaseContext(), "Obtained a Freeze Time powerup!",
                                    Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getBaseContext(), "Not enough coins :(",
                                    Toast.LENGTH_SHORT).show();

                        return true;
                }
                return false;

            }
        });

        brownoutPowerUpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        brownoutPowerUpButton.setVisibility(View.INVISIBLE);
                        brownoutPowerUpButtonOnClick.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        brownoutPowerUpButtonOnClick.setVisibility(View.INVISIBLE);
                        brownoutPowerUpButton.setVisibility(View.VISIBLE);

                        int currentCoins = sharedPreferences.getInt("Coins", 0);
                        if (currentCoins > dbHelper.queryPowerUp(2).getPrice()) {
                            editor.putInt("Coins", currentCoins - dbHelper.queryPowerUp(2).getPrice());
                            editor.putInt("powerup2Count", sharedPreferences.getInt("powerup2Count", 0)+1);
                            editor.apply();
                            playerBalance.setText(String.valueOf(sharedPreferences.getInt("Coins", 0)));

                            Toast.makeText(getBaseContext(), "Obtained a Brownout powerup!",
                                    Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getBaseContext(), "Not enough coins :(",
                                    Toast.LENGTH_SHORT).show();


                        return true;
                }
                return false;

            }
        });

        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        backButton.setVisibility(View.INVISIBLE);
                        backButtonOnClick.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        backButtonOnClick.setVisibility(View.INVISIBLE);
                        backButton.setVisibility(View.VISIBLE);

                        finish();
                        return true;
                }
                return false;

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        playerBalance.setText(String.valueOf(sharedPreferences.getInt("Coins", 0)));

        // Get the shared preferences
       sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       editor = sharedPreferences.edit();

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
