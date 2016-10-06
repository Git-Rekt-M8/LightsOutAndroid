package com.koalition.edu.lightsout;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class DifficultySelectionActivity extends Activity {

    ImageView close;
    TextView easyTextView;
    TextView mediumTextView;
    TextView hardTextView;
    Typeface pixelFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.activity_difficulty_selection);

        easyTextView = (TextView) findViewById(R.id.iv_easyDifficulty);
        mediumTextView = (TextView) findViewById(R.id.iv_mediumDifficulty);
        hardTextView = (TextView) findViewById(R.id.iv_hardDifficulty);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        easyTextView.setTypeface(pixelFont);
        mediumTextView.setTypeface(pixelFont);
        hardTextView.setTypeface(pixelFont);

        easyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startEasyGameIntent = new Intent(getBaseContext(), EasyPlayGameActivity.class);
                startEasyGameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startEasyGameIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        mediumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMediumGameIntent = new Intent(getBaseContext(), MediumPlayGameActivity.class);
                startMediumGameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startMediumGameIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        hardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startInsaneGameIntent = new Intent(getBaseContext(), InsanePlayGameActivity.class);
                startInsaneGameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startInsaneGameIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
        close = (ImageView) findViewById(R.id.iv_close);

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioPlayer.pauseMusic();
    }
}
