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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.TestCase;

import org.w3c.dom.Text;

public class ShopDialogActivity extends Activity {

    private Toast toast = null;
    Typeface pixelFont;

    ImageView close;

    ImageView powerupImageView;
    TextView powerUpTextView;
    TextView powerUpQuantity;
    TextView currentlyOwnedTextView;
    TextView powerUpDescription;
    ImageButton buyImageButton;


    SharedPreferences preferences;

    DatabaseHelper dbHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    PowerUp powerUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.activity_shop_dialog);

        dbHelper = new DatabaseHelper(getBaseContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = sharedPreferences.edit();

        Intent intent = this.getIntent();
        int shopItemID = intent.getExtras().getInt("shopItemID");
        powerUp = dbHelper.queryPowerUp(shopItemID);

        close = (ImageView) findViewById(R.id.iv_close);
        powerupImageView = (ImageView) findViewById(R.id.shopitem_imageview);
        powerUpTextView = (TextView) findViewById(R.id.shopitem_textview);
        powerUpQuantity = (TextView) findViewById(R.id.currentlyowned_num_textview);
        currentlyOwnedTextView = (TextView) findViewById(R.id.currentlyowned_textview);
        powerUpDescription = (TextView) findViewById(R.id.description_textview);
        buyImageButton = (ImageButton) findViewById(R.id.buy_button);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        powerUpDescription.setTypeface(pixelFont);
        powerUpTextView.setTypeface(pixelFont);
        powerUpQuantity.setTypeface(pixelFont);
        currentlyOwnedTextView.setTypeface(pixelFont);

        powerupImageView.setImageResource(getBaseContext().getResources().getIdentifier(powerUp.getIconTite(), "id", ShopDialogActivity.class.getPackage().getName()));
        powerUpTextView.setText(powerUp.getTitle());
        powerUpDescription.setText(powerUp.getDescription());
        powerUpQuantity.setText(String.valueOf(sharedPreferences.getInt("powerup" + powerUp.getId() + "Count", 0)));

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        switch (powerUp.getCategory()){
            case 0: currentlyOwnedTextView.setText("Stock: ");break;
            case 1: int ifBought = sharedPreferences.getInt("powerup" + powerUp.getId() + "Count", 0);
                if( ifBought>0 ) {
                    currentlyOwnedTextView.setText("Owned");
                    buyImageButton.setBackgroundResource(R.drawable.apply_btn_selector);
                }else{
                    currentlyOwnedTextView.setText("Not owned");
                }
                powerUpQuantity.setText("");break;
        }

        buyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentCoins = sharedPreferences.getInt("Coins", 0);

                if(powerUp.getCategory() == 0) {
                    if (currentCoins > powerUp.getPrice()) {
                        editor.putInt("Coins", currentCoins - powerUp.getPrice());
                        editor.putInt("powerup" + powerUp.getId() + "Count", sharedPreferences.getInt("powerup" + powerUp.getId() + "Count", 0) + 1);
                        editor.apply();
//                        TextView playerBalanceTV = (TextView) ((Activity) getBaseContext()).findViewById(R.id.player_balance);
//                        playerBalanceTV.setText(String.valueOf(sharedPreferences.getInt("Coins", 0)));
                        powerUpQuantity.setText(String.valueOf(sharedPreferences.getInt("powerup" + powerUp.getId() + "Count", 0)));
                        AudioPlayer.playSFX(getApplicationContext(), R.raw.cashsfx);

                        TextView textView = new TextView(getApplicationContext());
                        textView.setBackgroundColor(Color.DKGRAY);
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(20);
                        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/pixelmix.ttf");
                        textView.setTypeface(typeface);
                        textView.setPadding(10, 10, 10, 10);
                        textView.setText("Obtained a " + powerUp.getTitle() + " powerup!");


                        if (toast == null || !toast.getView().isShown()) {
                            if (toast != null) {
                                toast.cancel();
                            }
                            toast = Toast.makeText(getBaseContext(), "Obtained a " + powerUp.getTitle() + " powerup!",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, toast.getXOffset() / 2, toast.getYOffset() / 2);

                            toast.setView(textView);
                            toast.show();
                        }

                    } else {
                        TextView textView = new TextView(getApplicationContext());
                        textView.setBackgroundColor(Color.DKGRAY);
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(20);
                        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/pixelmix.ttf");
                        textView.setTypeface(typeface);
                        textView.setPadding(10, 10, 10, 10);
                        textView.setText("Not enough coins :(");

                        if (toast == null || !toast.getView().isShown()) {
                            if (toast != null) {
                                toast.cancel();
                            }
                            toast = Toast.makeText(getBaseContext(), "Not enough coins :(",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, toast.getXOffset() / 2, toast.getYOffset() / 2);

                            toast.setView(textView);
                            toast.show();
                        }
                    }
                }

                if(powerUp.getCategory() == 1) {
                    int ifBought = sharedPreferences.getInt("powerup" + powerUp.getId() + "Count", 0);
                    if (ifBought > 0) {
                        editor.putInt("CurrentDesign", powerUp.getId());
                        editor.apply();

                        TextView textView = new TextView(getApplicationContext());
                        textView.setBackgroundColor(Color.DKGRAY);
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(20);
                        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/pixelmix.ttf");
                        textView.setTypeface(typeface);
                        textView.setPadding(10, 10, 10, 10);
                        textView.setText("Changed design to " + powerUp.getTitle() + "!");

                        if (toast == null || !toast.getView().isShown()) {
                            if (toast != null) {
                                toast.cancel();
                            }
                            toast = Toast.makeText(getBaseContext(), "Changed design to " + powerUp.getTitle() + "!",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, toast.getXOffset() / 2, toast.getYOffset() / 2);

                            toast.setView(textView);
                            toast.show();
                        }

                    } else if (currentCoins > powerUp.getPrice()) {
                        editor.putInt("Coins", currentCoins - powerUp.getPrice());
                        editor.putInt("powerup" + powerUp.getId() + "Count", 1);
                        editor.putInt("CurrentDesign", powerUp.getId());
                        editor.apply();
//                        TextView playerBalanceTV = (TextView) ((Activity) getBaseContext()).findViewById(R.id.player_balance);
//                        playerBalanceTV.setText(String.valueOf(sharedPreferences.getInt("Coins", 0)));
//                        powerUpQuantity.setText(String.valueOf(sharedPreferences.getInt("powerup" + powerUp.getId() + "Count", 0)));
                        currentlyOwnedTextView.setText("Owned");
                        buyImageButton.setBackgroundResource(R.drawable.apply_btn_selector);
                        AudioPlayer.playSFX(getApplicationContext(), R.raw.cashsfx);

                        TextView textView = new TextView(getApplicationContext());
                        textView.setBackgroundColor(Color.DKGRAY);
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(20);
                        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/pixelmix.ttf");
                        textView.setTypeface(typeface);
                        textView.setPadding(10, 10, 10, 10);
                        textView.setText("Obtained a " + powerUp.getTitle() + "!");

                        if (toast == null || !toast.getView().isShown()) {
                            if (toast != null) {
                                toast.cancel();
                            }
                            Toast toast = Toast.makeText(getBaseContext(), "Obtained a " + powerUp.getTitle() + "!",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, toast.getXOffset() / 2, toast.getYOffset() / 2);
                            toast.setView(textView);
                            toast.show();
                        }
                    } else {

                        TextView textView = new TextView(getApplicationContext());
                        textView.setBackgroundColor(Color.DKGRAY);
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(20);
                        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/pixelmix.ttf");
                        textView.setTypeface(typeface);
                        textView.setPadding(10, 10, 10, 10);
                        textView.setText("Not enough coins :(");

                        if (toast == null || !toast.getView().isShown()) {
                            if (toast != null) {
                                toast.cancel();
                            }
                            toast = Toast.makeText(getBaseContext(), "Not enough coins :(",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, toast.getXOffset() / 2, toast.getYOffset() / 2);
                            toast.setView(textView);
                            toast.show();
                        }
                    }
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
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


