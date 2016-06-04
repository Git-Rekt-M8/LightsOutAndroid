package com.koalition.edu.lightsout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class DifficultySelectionActivity extends Activity {

    ImageView close;
    ImageView easyImageView;
    ImageView mediumImageView;
    ImageView hardImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.activity_difficulty_selection);

        easyImageView = (ImageView) findViewById(R.id.iv_easyDifficulty);
        mediumImageView = (ImageView) findViewById(R.id.iv_mediumDifficulty);
        hardImageView = (ImageView) findViewById(R.id.iv_hardDifficulty);

        easyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startEasyGameIntent = new Intent(getBaseContext(), EasyPlayGameActivity.class);
                startActivity(startEasyGameIntent);
            }
        });

        mediumImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMediumGameIntent = new Intent(getBaseContext(), MediumPlayGameActivity.class);
                startActivity(startMediumGameIntent);
            }
        });

        hardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startInsaneGameIntent = new Intent(getBaseContext(), InsanePlayGameActivity.class);
                startActivity(startInsaneGameIntent);
            }
        });
        close = (ImageView) findViewById(R.id.iv_close);

        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                finish();

            }

        });


    }

}
