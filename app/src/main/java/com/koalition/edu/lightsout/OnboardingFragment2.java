package com.koalition.edu.lightsout;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kingston on 3/5/2016.
 */
public class OnboardingFragment2 extends Fragment {

    Typeface pixelFont;
    TextView welcome;
    TextView message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle s) {

        View view = inflater.inflate(R.layout.onboarding_screen2, container, false);

        pixelFont = Typeface.createFromAsset(getContext().getAssets(),"fonts/pixelmix.ttf");
        welcome = (TextView) view.findViewById(R.id.tv_welcome2);
        message = (TextView) view.findViewById(R.id.tv_message2);

        welcome.setTypeface(pixelFont);
        message.setTypeface(pixelFont);

    return view;

    }

}
