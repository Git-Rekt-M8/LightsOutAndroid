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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DesignListActivity extends Activity {

    DatabaseHelper dbHelper;
    TextView designUpHead;
    TextView backButton;

    TextView playerBalance;


    Typeface pixelFont;

    SharedPreferences sharedPreferences;

    RecyclerView recyclerView;
    PowerupAdapter powerupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_list);

        dbHelper = new DatabaseHelper(getBaseContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        designUpHead = (TextView) findViewById(R.id.design_head);
        backButton = (TextView) findViewById(R.id.back_button_shop);

        playerBalance = (TextView) findViewById(R.id.player_balance);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        playerBalance.setTypeface(pixelFont);
        designUpHead.setTypeface(pixelFont);
        backButton.setTypeface(pixelFont);

        // Step 1: create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_design);

        // Step 3: Create our adapter
        powerupAdapter = new PowerupAdapter(dbHelper.queryAllDesigns(), this);

        // Step 4: Attach adapter to UI
        recyclerView.setAdapter(powerupAdapter);

        // Step 5: Attach layout manager to UI
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));


/*        *//* hide onclick buttons**//*
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
        });*/

        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        backButton.setTextColor(getResources().getColor(R.color.pressedText));
                        //backButtonOnClick.setVisibility(View.VISIBLE); change color
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        //backButtonOnClick.setVisibility(View.INVISIBLE); change back color
                        backButton.setTextColor(getResources().getColor(R.color.notPressedText));

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
