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

public class DesignListActivity extends Activity {

    DatabaseHelper dbHelper;
    ImageView designHead;
    ImageView backButton;
    ImageView backButtonOnClick;
    ImageView greenHouseButton;
    //ImageView freezePowerUpButtonOnClick;
    ImageView nipaHutButton;
    //ImageView brownoutPowerUpButtonOnClick;

    TextView playerBalance;


    Typeface pixelFont;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_list);

        dbHelper = new DatabaseHelper(getBaseContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        designHead = (ImageView) findViewById(R.id.iv_design_header);
        backButton = (ImageView) findViewById(R.id.back_button);;
        backButtonOnClick = (ImageView) findViewById(R.id.back_button_clicked);;
        greenHouseButton = (ImageView) findViewById(R.id.iv_green_house_btn);;
        //freezePowerUpButtonOnClick = (ImageView) findViewById(R.id.freeze_powerup_cliicked);;
        nipaHutButton = (ImageView) findViewById(R.id.iv_nipa_hut_btn);;
        //brownoutPowerUpButtonOnClick = (ImageView) findViewById(R.id.brownout_powerup_clicked);

        playerBalance = (TextView) findViewById(R.id.player_balance);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        playerBalance.setTypeface(pixelFont);


        /* hide onclick buttons**/
//        freezePowerUpButtonOnClick.setVisibility(View.INVISIBLE);
//        brownoutPowerUpButtonOnClick.setVisibility(View.INVISIBLE);
//        backButtonOnClick.setVisibility(View.INVISIBLE);

        greenHouseButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
//                        freezePowerUpButton.setVisibility(View.INVISIBLE);
//                        freezePowerUpButtonOnClick.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
//                        freezePowerUpButtonOnClick.setVisibility(View.INVISIBLE);
//                        freezePowerUpButton.setVisibility(View.VISIBLE);

                        int currentCoins = sharedPreferences.getInt("Coins", 0);
                        int ifBought = sharedPreferences.getInt("powerup3Count", 0);
                        if( ifBought>0 ) {
                            editor.putInt("CurrentDesign", 1);
                        } else
                        if (currentCoins > dbHelper.queryPowerUp(1).getPrice() ) {
                            editor.putInt("Coins", currentCoins - dbHelper.queryPowerUp(3).getPrice());
                            editor.putInt("powerup3Count", 1);
                            editor.putInt("CurrentDesign", 1);
                            editor.apply();
                            playerBalance.setText(String.valueOf(sharedPreferences.getInt("Coins", 0)));


                            Toast.makeText(getBaseContext(), "Obtained a Green House!",
                                    Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getBaseContext(), "Not enough coins :(",
                                    Toast.LENGTH_SHORT).show();

                        return true;
                }
                return false;

            }
        });

        nipaHutButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
//                        brownoutPowerUpButton.setVisibility(View.INVISIBLE);
//                        brownoutPowerUpButtonOnClick.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
//                        brownoutPowerUpButtonOnClick.setVisibility(View.INVISIBLE);
//                        brownoutPowerUpButton.setVisibility(View.VISIBLE);

                        int currentCoins = sharedPreferences.getInt("Coins", 0);
                        int ifBought = sharedPreferences.getInt("powerup4Count", 0);
                        if( ifBought>0 ) {
                            editor.putInt("CurrentDesign", 2);
                        } else
                        if (currentCoins > dbHelper.queryPowerUp(4).getPrice()) {
                            editor.putInt("Coins", currentCoins - dbHelper.queryPowerUp(4).getPrice());
                            editor.putInt("powerup4Count", 1);
                            editor.putInt("CurrentDesign", 2);
                            editor.apply();
                            playerBalance.setText(String.valueOf(sharedPreferences.getInt("Coins", 0)));

                            Toast.makeText(getBaseContext(), "Obtained a Nipa Hut!",
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
