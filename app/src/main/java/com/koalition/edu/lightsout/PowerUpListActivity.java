package com.koalition.edu.lightsout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PowerUpListActivity extends Activity {

    DatabaseHelper dbHelper;
    TextView powerUpHead;
    TextView backButton;

    TextView playerBalance;

    Typeface pixelFont;

    SharedPreferences sharedPreferences;

    RecyclerView recyclerView;
    PowerupAdapter powerupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_up_list);

        dbHelper = new DatabaseHelper(getBaseContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        powerUpHead = (TextView) findViewById(R.id.shop_head);
        backButton = (TextView) findViewById(R.id.back_button_shop);

        //unsure test
//        final LayoutInflater factory = getLayoutInflater();
//
//        final View textEntryView = factory.inflate(R.layout.list_item_powerup, null);


        playerBalance = (TextView) findViewById(R.id.player_balance);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        playerBalance.setTypeface(pixelFont);
        powerUpHead.setTypeface(pixelFont);
        backButton.setTypeface(pixelFont);

        // Step 1: create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_powerup);

        // Step 3: Create our adapter
        powerupAdapter = new PowerupAdapter(dbHelper.queryAllPowerups(), this);

        // Step 4: Attach adapter to UI
        recyclerView.setAdapter(powerupAdapter);

        // Step 5: Attach layout manager to UI
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));


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

                        Intent intent = new Intent(getApplicationContext(), ShopMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

        AudioPlayer.playMusic(getApplicationContext(), R.raw.mainmenu);

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
