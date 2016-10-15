package com.koalition.edu.lightsout;

import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;


public class GameOverActivity extends AppCompatActivity {
    TextView score;
    ImageView bestScoreText;
    ImageButton backToMainButton;
    ImageButton playAgainButton;
    ShareButton fbShareButton;
    TextView coinsReceivedText;
    SharedPreferences sharedPreferences;
    String difficulty;

    Animation fadeInAnimation;


    Typeface pixelFont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_game_over);
        score = (TextView) findViewById(R.id.tv_easy_score);
        bestScoreText = (ImageView) findViewById(R.id.thats_your_best_text);
        backToMainButton = (ImageButton) findViewById(R.id.back_to_main_menu_button);
        playAgainButton = (ImageButton) findViewById(R.id.play_again_button);
        coinsReceivedText = (TextView) findViewById(R.id.coins_received_text);
        fadeInAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        score.setTypeface(pixelFont);
        coinsReceivedText.setTypeface(pixelFont);


        /* hide onclick buttons**/
        bestScoreText.setVisibility(View.INVISIBLE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        difficulty = sharedPreferences.getString("lastPlayedDifficulty", "easy");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int highScore = sharedPreferences.getInt("HighScore", -1);
        int currentScore = sharedPreferences.getInt("CurrentScore", -5);
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

        fbShareButton = (ShareButton) findViewById(R.id.share_btn);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentDescription(
                        "I scored " + score.getText().toString() + " on " + capitalize(difficulty) + "! Can you beat me?" )
                .setContentUrl(Uri.parse("https://www.facebook.com/LightsOutMobile"))
                .build();
        fbShareButton.setShareContent(content);

        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if(difficulty.equals("easy"))
                    intent = new Intent(getApplicationContext(), EasyPlayGameActivity.class);
                else if(difficulty.equals("medium"))
                    intent = new Intent(getApplicationContext(), MediumPlayGameActivity.class);
                else if(difficulty.equals("insane"))
                    intent = new Intent(getApplicationContext(), InsanePlayGameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //hide buttons initially
        fbShareButton.setVisibility(View.INVISIBLE);
        backToMainButton.setVisibility(View.INVISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);

        animateTextView(0, currentScore, score);
        animateTextView(0, coinsReceived, coinsReceivedText);

        showButtonsHandler.postDelayed(showButtonsRunnable, 1700);
    }


    Handler showButtonsHandler = new Handler();
    Runnable showButtonsRunnable = new Runnable() {
        @Override
        public void run() {
            fbShareButton.startAnimation(fadeInAnimation);
            fbShareButton.setVisibility(View.VISIBLE);
            backToMainButton.startAnimation(fadeInAnimation);
            backToMainButton.setVisibility(View.VISIBLE);
            playAgainButton.startAnimation(fadeInAnimation);
            playAgainButton.setVisibility(View.VISIBLE);
        }
    };

    public void animateTextView(int initialValue, int finalValue, final TextView  textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1000);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
// Get the shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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

    @Override
    public void onBackPressed() {
    }

    public String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
