package com.koalition.edu.lightsout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;


public class GameOverActivity extends AppCompatActivity {
    TextView score;
    ImageView bestScoreText;
    ImageView backToMainButton;
    ImageView backToMainButtonClicked;
    ImageView playAgainButton;
    ImageView playAgainButtonClicked;
    TextView coinsReceivedText;
    SharedPreferences sharedPreferences;


    Typeface pixelFont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_game_over);
        score = (TextView) findViewById(R.id.tv_easy_score);
        bestScoreText = (ImageView) findViewById(R.id.thats_your_best_text);
        backToMainButton = (ImageView) findViewById(R.id.back_to_main_menu_button);
        backToMainButtonClicked = (ImageView) findViewById(R.id.back_to_main_menu_button_clicked);
        playAgainButton = (ImageView) findViewById(R.id.play_again_button);
        playAgainButtonClicked = (ImageView) findViewById(R.id.play_again_button_clicked);
        coinsReceivedText = (TextView) findViewById(R.id.coins_received_text);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        score.setTypeface(pixelFont);
        coinsReceivedText.setTypeface(pixelFont);


        /* hide onclick buttons**/
        backToMainButtonClicked.setVisibility(View.INVISIBLE);
        playAgainButtonClicked.setVisibility(View.INVISIBLE);
        bestScoreText.setVisibility(View.INVISIBLE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int highScore = sharedPreferences.getInt("HighScore", -1);
        int currentScore = sharedPreferences.getInt("CurrentScore", -5);
        System.out.println("SEX "+currentScore);
        //change to store the current score also in preferenceScore
        score.setText(String.valueOf(currentScore));
        if(currentScore >= highScore)
        {
            editor.putInt("HighScore",currentScore);
            editor.apply();
            bestScoreText.setVisibility(View.VISIBLE);
        }
        int coinsReceived = currentScore/10;
        coinsReceivedText.setText(String.valueOf(coinsReceived));

        int currentCoins = sharedPreferences.getInt("Coins", 0);
        editor.putInt("Coins", currentCoins + coinsReceived);
        editor.apply();



        ShareButton fbShareButton = (ShareButton) findViewById(R.id.share_btn);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentDescription(
                        "Can you beat my score: " + score.getText().toString())
                .setContentUrl(Uri.parse("https://www.facebook.com/LightsOutMobile"))
                .build();
        fbShareButton.setShareContent(content);

        backToMainButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        backToMainButton.setVisibility(View.INVISIBLE);
                        backToMainButtonClicked.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        //=====Write down you code Finger Released code here
                        backToMainButtonClicked.setVisibility(View.INVISIBLE);
                        backToMainButton.setVisibility(View.VISIBLE);
/*                        Intent i = new Intent(GameOverActivity.this, MainActivity.class);
                        startActivity(i);*/
                        finish();
                        return true;
                }
                return false;

            }
        });

        playAgainButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        //=====Write down your Finger Pressed code here
                        playAgainButton.setVisibility(View.INVISIBLE);
                        playAgainButtonClicked.setVisibility(View.VISIBLE);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        String difficulty = sharedPreferences.getString("lastPlayedDifficulty", "easy");
                        Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
                        if(difficulty.equals("easy"))
                            intent = new Intent(GameOverActivity.this, EasyPlayGameActivity.class);
                        else if(difficulty.equals("medium"))
                            intent = new Intent(GameOverActivity.this, MediumPlayGameActivity.class);
                        else if(difficulty.equals("insane"))
                            intent = new Intent(GameOverActivity.this, InsanePlayGameActivity.class);
                        startActivity(intent);

                        //=====Write down you code Finger Released code here
                        playAgainButtonClicked.setVisibility(View.INVISIBLE);
                        playAgainButton.setVisibility(View.VISIBLE);
                        /*Intent i = new Intent(GameOverActivity.this, MainActivity.class);
                        startActivity(i);*/
                        return true;
                }
                return false;

            }
        });


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
        //mediaPlayer.start();
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
}
