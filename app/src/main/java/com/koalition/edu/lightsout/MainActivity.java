package com.koalition.edu.lightsout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    ImageView playGameButton;
    ImageView shopButton;
    ImageView settingsButton;
    ImageView backgroundImageView;

    private MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;
    final static int BC_PENDINGINTENT = 3;

    TransitionDrawable crossfader;
    int drawableIndex = 0;
    Resources res;
    Drawable backgrounds[] = new Drawable[2];
    int[] drawableIDs = new int[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // START OF CROSSFADE
        backgroundImageView = (ImageView)findViewById(R.id.background_rooms);

        drawableIDs[0] = R.drawable.room3bg;
        drawableIDs[1] = R.drawable.room1bg;
        drawableIDs[2] = R.drawable.room5bg;
        drawableIDs[3] = R.drawable.room4bg;
        drawableIDs[4] = R.drawable.room2bg;
        drawableIDs[5] = R.drawable.room6bg;

        res = getResources();
//        crossfadeHandler.postDelayed(crossfadeRunnable, 0);
        // END OF CROSSFADE

        //one time lang
        AudioPlayer.initSFX(getApplicationContext());

        // Get the solo preferences (only for this activity)
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        // Get the shared preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // music initial
        //editor.putBoolean("Music", true);
        //editor.apply();
        //editor.putBoolean("SoundFX", true);
        //editor.apply();

        System.out.println("If contains music onCreate: " + preferences.contains("Music"));

        // Check if onboarding_complete is false
        if(!preferences.getBoolean("onboarding_complete",false)) {
            dbHelper = new DatabaseHelper(getBaseContext());
            dbHelper.deleteAll();
            dbHelper.insertPowerUp(new PowerUp(1, "Freeze Money", 300, 0, "@drawable/freezeshopicon", "Avoid those electric bills and hold on to your money for a short amount of time!"));
            dbHelper.insertPowerUp(new PowerUp(2, "Brownout", 500, 0, "@drawable/brownshopicon", "Discharge a house-wide brownout and have all the lights turned off instantly!"));
            dbHelper.insertPowerUp(new PowerUp(3, "Default", 0, 1, "@drawable/defaulthouseicon", "The classic design in all its glory."));
            dbHelper.insertPowerUp(new PowerUp(4, "Green House", 3000, 1, "@drawable/greenhouseicon", "Be one with nature as a refreshing green tone blankets the house."));
            dbHelper.insertPowerUp(new PowerUp(5, "Nipa Hut", 5000, 1, "@drawable/nipahuticon", "Have a glimpse of the Phlippine heritage and rebuild your house out of bamboos and long leaves!"));


            editor.putInt("HighScore", 0); // STORE INITIAL SCORE OF 0
            editor.putInt("CurrentScore", 0);
            editor.putInt("Coins", 15000);
            //editor.putBoolean("Music", true);
            //editor.putBoolean("SoundFX", true);
            editor.putInt("powerup1Count", 0);
            editor.putInt("powerup2Count", 0);
            editor.putInt("powerup3Count", 1);
            editor.putInt("powerup4Count", 0);
            editor.putInt("powerup5Count", 0);
            editor.putInt("CurrentDesign",0);
            editor.apply();

            editor.putBoolean("getsFreeCoins", true);
            editor.apply();
            editor.putBoolean("onboarding_complete", false);
        }



        playGameButton = (ImageView) findViewById(R.id.iv_playgamewht);
        shopButton = (ImageView) findViewById(R.id.iv_shopwht);
        settingsButton = (ImageView) findViewById(R.id.iv_settingswht);


        playGameButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        playGameButton.setImageResource(R.drawable.playgame_btn_blk);
                        //=====Write down your Finger Pressed code here
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        playGameButton.setImageResource(R.drawable.playgame_btn);
                        AudioPlayer.playSFX(getApplicationContext(), R.raw.switchsfx);
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
                        shopButton.setImageResource(R.drawable.shop_btn_clk);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        shopButton.setImageResource(R.drawable.shop_btn);
                        AudioPlayer.playSFX(getApplicationContext(), R.raw.switchsfx);

                        Intent intent=new Intent(getApplicationContext(), ShopMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
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
                        settingsButton.setImageResource(R.drawable.settings_btn_clk);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        settingsButton.setImageResource(R.drawable.settings_btn);
                        AudioPlayer.playSFX(getApplicationContext(), R.raw.switchsfx);

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

        crossfadeHandler.postDelayed(crossfadeRunnable, 0);

        MyApplication.activityResumed();
        // Get the shared preferences
//        preferences =  getSharedPreferences("my_preferences", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        System.out.println("ANO DAW PO: " + sharedPreferences.getBoolean("Music", false));
        System.out.println("PAG ETO TRU >:( " + sharedPreferences.getBoolean("Music", true));
        System.out.println("iyak if false: " + sharedPreferences.contains("Music"));
        System.out.println("free coins dapat true: " + sharedPreferences.contains("getsFreeCoins"));

        AudioPlayer.playMusic(getApplicationContext(), R.raw.mainmenu);

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
            Toast toast = Toast.makeText(getBaseContext(), "You get free 100 coins!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, toast.getXOffset() / 2, toast.getYOffset() / 2);

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
        AudioPlayer.pauseMusic();
        MyApplication.activityPaused();
        crossfadeHandler.removeCallbacks(crossfadeRunnable);
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

    Handler crossfadeHandler = new Handler();
    Runnable crossfadeRunnable = new Runnable() {

        @Override
        public void run() {

            if((backgrounds[0]!=null) ) {
                if (backgrounds[0] instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) backgrounds[0];
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    bitmap.recycle();
                    backgrounds[0] = null;
                    System.out.println("TIME TO RECYCLE YO!! 0");
                }
            }

//            if((backgrounds[1]!=null)) {
//                if (backgrounds[1] instanceof BitmapDrawable) {
//                    BitmapDrawable bitmapDrawable = (BitmapDrawable) backgrounds[1];
//                    Bitmap bitmap = bitmapDrawable.getBitmap();
//                    bitmap.recycle();
//                    backgrounds[1] = null;
//                    System.out.println("TIME TO RECYCLE YO!! 1");
//                }
//            }

            //backgrounds[0] = res.getDrawable(drawableIDs[drawableIndex], getTheme());
            backgrounds[0] = res.getDrawable(drawableIDs[drawableIndex]);
            if(drawableIndex==5){
                //backgrounds[1] = res.getDrawable(drawableIDs[0], getTheme());
                backgrounds[1] = res.getDrawable(drawableIDs[0]);
            }
            else //backgrounds[1] = res.getDrawable(drawableIDs[drawableIndex+1], getTheme());
                backgrounds[1] = res.getDrawable(drawableIDs[drawableIndex+1]);
            crossfader = new TransitionDrawable(backgrounds);

            backgroundImageView.setImageDrawable(crossfader);

            crossfader.startTransition(1000);
            drawableIndex++;
            if(drawableIndex==6){
                drawableIndex=0;
            }
            System.out.println("YEH BUIIIII");
            crossfadeHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        recycleImageView(backgroundImageView);
        backgroundImageView.setImageDrawable(null);

    }

    private void recycleImageView(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }
    }

}
