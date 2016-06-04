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

public class ShopMainActivity extends Activity {

    ImageView shopHead;
    ImageView backButton;
    ImageView backButtonOnClick;
    ImageView powerUpButton;
    ImageView powerUpButtonOnClick;
    ImageView designButton;
    ImageView designButtonOnClick;
    TextView playerBalance;

    Typeface pixelFont;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_main);

        shopHead = (ImageView) findViewById(R.id.shop_head);
        backButton = (ImageView) findViewById(R.id.back_button);
        backButtonOnClick = (ImageView) findViewById(R.id.back_button_clicked);
        powerUpButton = (ImageView) findViewById(R.id.power_button);
        powerUpButtonOnClick = (ImageView) findViewById(R.id.power_button_clicked);
        designButton = (ImageView) findViewById(R.id.design_button);
        designButtonOnClick = (ImageView) findViewById(R.id.design_button_clicked);
        playerBalance = (TextView) findViewById(R.id.player_balance);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        playerBalance.setTypeface(pixelFont);

        /* hide onclick buttons**/
        powerUpButtonOnClick.setVisibility(View.INVISIBLE);
        designButtonOnClick.setVisibility(View.INVISIBLE);
        backButtonOnClick.setVisibility(View.INVISIBLE);



        powerUpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        powerUpButton.setVisibility(View.INVISIBLE);
                        powerUpButtonOnClick.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        powerUpButtonOnClick.setVisibility(View.INVISIBLE);
                        powerUpButton.setVisibility(View.VISIBLE);

                        Intent intent = new Intent(getApplicationContext(), PowerUpListActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;

            }
        });

        designButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        designButton.setVisibility(View.INVISIBLE);
                        designButtonOnClick.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        designButtonOnClick.setVisibility(View.INVISIBLE);
                        designButton.setVisibility(View.VISIBLE);

                        Intent intent = new Intent(getApplicationContext(), DesignListActivity.class);
                        startActivity(intent);
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
