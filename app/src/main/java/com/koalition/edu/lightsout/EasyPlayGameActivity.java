package com.koalition.edu.lightsout;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class EasyPlayGameActivity extends Activity {

    ImageView room1Box;
    ImageView room2Box;
    ImageView room3Box;
    ImageView room4Box;

    ImageView switchButton1;
    ImageView switchButton2;
    ImageView switchButton3;
    ImageView switchButton4;

    ImageView freezeTimeButton;
    ImageView brownoutButton;

    TextView moneyTextView;
    TextView scoreTextView;
    TextView deductionTextView;
    TextView freezeTimeCountTextView;
    TextView brownoutCountTextView;
    TextView centerTextView;
    Typeface pixelFont;

    ImageView freezeScreenImageView;
    Animation freezeFadeoutAnim;
//    ImageView streakImageView;
    Animation streakFadeoutAnim;

    ImageView designImageView;

    private MediaPlayer mediaPlayer;
    private int numOfRooms;

    // GAME VARIABLES
    // timer for randomizing every randomizeSpeed
    int RANDOMIZE_SPEED = 1500;
    int RANDOMIZE_COUNTER = 1;
    int POINTS_LOST = 1;
    int POINTS_GAINED = 10;
    int POSSIBLE_LIGHTS_ON = 1;
    int STARTING_COINS = 100;



    int currentDesign;

    //HUD
    private int time;
    private int moneyValue;
    private int scoreValue;

    // For controlling thread
    private boolean running;

    //Freeze powerup stuff
    private boolean ifFrozen;
    private int endOfFreezing;

    static int VIBRATION_DURATION = 350;
    // kung nakailan na siyang sunod sunod
    int streakValue;

    ArrayList<Switch> switches;
    private Random statusRandom;

    Animation slideDownAnim;
    Animation fadeOutAnim;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    ValueAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_play_game);

        final ImageView backgroundOne = (ImageView) findViewById(R.id.background_sky1);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_sky2);

        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(80000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();

        AudioPlayer.playMusic(getApplicationContext(), R.raw.chill);
        running = true;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        room1Box = (ImageView) findViewById(R.id.easy_room1);
        room2Box = (ImageView) findViewById(R.id.easy_room2);
        room3Box = (ImageView) findViewById(R.id.easy_room3);
        room4Box = (ImageView) findViewById(R.id.easy_room4);
        switchButton1 = (ImageView) findViewById(R.id.easy_button1);
        switchButton2 = (ImageView) findViewById(R.id.easy_button2);
        switchButton3 = (ImageView) findViewById(R.id.easy_button3);
        switchButton4 = (ImageView) findViewById(R.id.easy_button4);

        designImageView = (ImageView) findViewById(R.id.house_design);

        freezeTimeButton = (ImageView) findViewById(R.id.iv_freeze_time);
        brownoutButton = (ImageView) findViewById(R.id.iv_brown_out);
        moneyTextView = (TextView) findViewById(R.id.tv_money);
        scoreTextView = (TextView) findViewById(R.id.tv_easy_score);
        freezeTimeCountTextView = (TextView) findViewById(R.id.tv_freeze_count);
        brownoutCountTextView = (TextView) findViewById(R.id.tv_brown_out_count);
        deductionTextView = (TextView) findViewById(R.id.tv_deduction);
        centerTextView = (TextView) findViewById(R.id.center_text);

        pixelFont = Typeface.createFromAsset(getAssets(),"fonts/pixelmix.ttf");
        moneyTextView.setTypeface(pixelFont);
        scoreTextView.setTypeface(pixelFont);
        freezeTimeCountTextView.setTypeface(pixelFont);
        brownoutCountTextView.setTypeface(pixelFont);
        deductionTextView.setTypeface(pixelFont);
        centerTextView.setTypeface(pixelFont);



        slideDownAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_down_deduction);
        fadeOutAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);

        freezeScreenImageView = (ImageView) findViewById(R.id.freeze_screen);
        freezeScreenImageView.setVisibility(ImageView.INVISIBLE);
        freezeFadeoutAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.freeze_fadeout);

//        streakImageView = (ImageView) findViewById(R.id.streak_view);
        centerTextView.setVisibility(ImageView.INVISIBLE);
        streakFadeoutAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.streak_fadeout);

        // DESGIN STUFF
        currentDesign = sharedPreferences.getInt("CurrentDesign", 0);
        switch (currentDesign) {
            case 0: break;
            case 3: designImageView.setImageResource(android.R.color.transparent);break;
            case 4: designImageView.setImageResource(R.drawable.green_custom); break;
            case 5: designImageView.setImageResource(R.drawable.nipa_custom);break;
        }

        numOfRooms = 4;
        editor.putString("lastPlayedDifficulty", "easy");
        editor.apply();

        // HUD
        moneyValue = STARTING_COINS;
        scoreValue = 0;
        freezeTimeCountTextView.setText(String.valueOf(sharedPreferences.getInt("powerup1Count", 0)));
        brownoutCountTextView.setText(String.valueOf(sharedPreferences.getInt("powerup2Count", 0)));
        updateHUD(moneyValue, scoreValue);

        // powerups stuff fuck you
        ifFrozen = false;
        endOfFreezing = -1;

        statusRandom = new Random();
        // timer for randomizing every randomizeSpeed
        time = 0;

        refreshSwitches();


        switchButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        switchButton1.setImageResource(R.drawable.switchoff_clk);
                        //=====Write down your Finger Pressed code here
                        if (switches.get(0).isRoomState() == true) {
                            // TODO add score
                            if (switches.get(0).getIsSwitchedByAI() == true) {
                                scoreValue += POINTS_GAINED;
                                animateTextView(scoreValue - POINTS_GAINED, scoreValue, scoreTextView);
                                AudioPlayer.playSFX(getApplicationContext(), R.raw.upsfx);
                                updateHUD(moneyValue, scoreValue);
                                streakValue++;
                                checkIfStreakBonus(streakValue);
                                switches.get(0).setIsSwitchedByAI(false);
                            }
                            switches.get(0).setRoomState(false);
                            turnOffRoom(switches.get(0).getRoomNumber());

                        } else if (switches.get(0).isRoomState() == false) {
                            streakValue = 0;
                            AudioPlayer.playSFX(getApplicationContext(), R.raw.downsfx);

                            switches.get(0).setRoomState(true);
                            turnOnRoom(switches.get(0).getRoomNumber());

                            // VIBRATOR TURN ON
                            Vibrator vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            vibrator.vibrate(VIBRATION_DURATION);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:

                        switchButton1.setImageResource(R.drawable.switchoff);
                        //=====Write down you code Finger Released code here
                        return true;
                    }
                return false;
            }
        });

        switchButton2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        switchButton2.setImageResource(R.drawable.switchoff_clk);

                        //=====Write down your Finger Pressed code here
                        if (switches.get(1).isRoomState() == true) {
                            // TODO add score
                            if (switches.get(1).getIsSwitchedByAI() == true) {
                                scoreValue += POINTS_GAINED;
                                animateTextView(scoreValue - POINTS_GAINED, scoreValue, scoreTextView);
                                AudioPlayer.playSFX(getApplicationContext(), R.raw.upsfx);
                                updateHUD(moneyValue, scoreValue);
                                streakValue++;
                                checkIfStreakBonus(streakValue);
                                switches.get(1).setIsSwitchedByAI(false);
                            }
                            switches.get(1).setRoomState(false);
                            turnOffRoom(switches.get(1).getRoomNumber());

                        } else if (switches.get(1).isRoomState() == false) {
                            streakValue = 0;

                            switches.get(1).setRoomState(true);
                            turnOnRoom(switches.get(1).getRoomNumber());

                            AudioPlayer.playSFX(getApplicationContext(), R.raw.downsfx);

                            // VIBRATOR TURN ON
                            Vibrator vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            vibrator.vibrate(VIBRATION_DURATION);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        switchButton2.setImageResource(R.drawable.switchoff);

                        //=====Write down you code Finger Released code here
                        return true;
                }
                return false;
            }
        });

        switchButton3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        switchButton3.setImageResource(R.drawable.switchoff_clk);

                        //=====Write down your Finger Pressed code here
                        if (switches.get(2).isRoomState() == true) {
                            // TODO add score
                            if (switches.get(2).getIsSwitchedByAI() == true) {
                                scoreValue += POINTS_GAINED;
                                animateTextView(scoreValue - POINTS_GAINED, scoreValue, scoreTextView);
                                AudioPlayer.playSFX(getApplicationContext(), R.raw.upsfx);
                                updateHUD(moneyValue, scoreValue);
                                streakValue++;
                                checkIfStreakBonus(streakValue);
                                switches.get(2).setIsSwitchedByAI(false);
                            }
                            switches.get(2).setRoomState(false);
                            turnOffRoom(switches.get(2).getRoomNumber());

                        } else if (switches.get(2).isRoomState() == false) {
                            streakValue = 0;

                            switches.get(2).setRoomState(true);
                            turnOnRoom(switches.get(2).getRoomNumber());
                            AudioPlayer.playSFX(getApplicationContext(), R.raw.downsfx);

                            // VIBRATOR TURN ON
                            Vibrator vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            vibrator.vibrate(VIBRATION_DURATION);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        switchButton3.setImageResource(R.drawable.switchoff);

                        //=====Write down you code Finger Released code here
                        return true;
                }
                return false;
            }
        });

        switchButton4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        switchButton4.setImageResource(R.drawable.switchoff_clk);

                        //=====Write down your Finger Pressed code here
                        if (switches.get(3).isRoomState() == true) {
                            // TODO add score
                            if (switches.get(3).getIsSwitchedByAI() == true) {
                                AudioPlayer.playSFX(getApplicationContext(), R.raw.upsfx);
                                scoreValue += POINTS_GAINED;
                                animateTextView(scoreValue - POINTS_GAINED, scoreValue, scoreTextView);
                                updateHUD(moneyValue, scoreValue);
                                streakValue++;
                                checkIfStreakBonus(streakValue);
                                switches.get(3).setIsSwitchedByAI(false);
                            }
                            switches.get(3).setRoomState(false);
                            turnOffRoom(switches.get(3).getRoomNumber());

                        } else if (switches.get(3).isRoomState() == false) {
                            streakValue = 0;

                            switches.get(3).setRoomState(true);
                            turnOnRoom(switches.get(3).getRoomNumber());
                            AudioPlayer.playSFX(getApplicationContext(), R.raw.downsfx);

                            // VIBRATOR TURN ON
                            Vibrator vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            vibrator.vibrate(VIBRATION_DURATION);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        switchButton4.setImageResource(R.drawable.switchoff);

                        //=====Write down you code Finger Released code here
                        return true;
                }
                return false;
            }
        });

        freezeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateFreezeTime();
            }
        });

        brownoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateBrownOut();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void checkIfStreakBonus(int streakValue) {
        if( streakValue >= 15 ) {
            scoreValue += 30;
            animateTextView(scoreValue - 30, scoreValue, scoreTextView);

            centerTextView.setText("Your streak is 15");
            centerTextView.startAnimation(streakFadeoutAnim);
            centerTextView.setVisibility(View.INVISIBLE);

            updateHUD(moneyValue, scoreValue);
        } else if (streakValue >= 10) {
            scoreValue += 20;
            animateTextView(scoreValue - 20, scoreValue, scoreTextView);

            centerTextView.setText("Your streak is 10");
            centerTextView.startAnimation(streakFadeoutAnim);
            centerTextView.setVisibility(View.INVISIBLE);

            updateHUD(moneyValue, scoreValue);
        } else
        if (streakValue >= 5) {
            scoreValue += 10;
            animateTextView(scoreValue - 10, scoreValue, scoreTextView);

            centerTextView.setText("Your streak is 5");
            centerTextView.startAnimation(streakFadeoutAnim);
            centerTextView.setVisibility(View.INVISIBLE);

            updateHUD(moneyValue, scoreValue);
        }
    }

    public void turnOffRoom(int roomNumber) {
        AudioPlayer.playSFX(getApplicationContext(), R.raw.switchsfx);
        switch (roomNumber) {
            case 1:
                room1Box.setImageResource(R.drawable.room1off);
                break;
            case 2:
                room2Box.setImageResource(R.drawable.room2off);
                break;
            case 3:
                room3Box.setImageResource(R.drawable.room3off);
                break;
            case 4:
                room4Box.setImageResource(R.drawable.room4off);
                break;
        }
    }

    public void turnOnRoom(int roomNumber) {
        AudioPlayer.playSFX(getApplicationContext(), R.raw.switchsfx);
        switch (roomNumber) {
            case 1:
                room1Box.setImageResource(R.drawable.room1on);
                break;
            case 2:
                room2Box.setImageResource(R.drawable.room2on);
                break;
            case 3:
                room3Box.setImageResource(R.drawable.room3on);
                break;
            case 4:
                room4Box.setImageResource(R.drawable.room4on);
                break;
//            case 1: room1Box.setImageResource(R.drawable.homer); break;
//            case 2: room2Box.setImageResource(R.drawable.homer); break;
//            case 3: room3Box.setImageResource(R.drawable.homer); break;
//            case 4: room4Box.setImageResource(R.drawable.homer); break;
        }
    }


    public void refreshSwitches() {
        switches = new ArrayList<>();
        Switch s;
        int[] choicesArray = {1, 2, 3, 4};
        int[] switchChoiceArray = {0, 1, 2, 3};
        choicesArray = shuffleArray(choicesArray);
        switchChoiceArray = shuffleArray(switchChoiceArray);
        System.out.println("HELLO " + switchChoiceArray[0]);
        System.out.println("HELLO " + switchChoiceArray[0]);
        boolean startingRoomState = false;
        boolean startingSwitchState = false;

        for (int i = 0; i < choicesArray.length; i++) {
            s = new Switch(switchChoiceArray[i], choicesArray[i], startingSwitchState, startingRoomState);
            switches.add(s);
        }

        for (int j = 0; j < switches.size(); j++) {
            updateComponents(switches.get(j).getSwitchNumber(), switches.get(j).getRoomNumber(), switches.get(j).isSwitchState(), switches.get(j).isRoomState());

        }
        for (int i = 0; i < switches.size(); i++) {
            switches.get(i).setSwitchNumber(i);

        }
        for (int j = 0; j < switches.size(); j++)
            System.out.println("switchNumber: " + switches.get(j).getSwitchNumber() + " room " + switches.get(j).getRoomNumber());
    }

    public void updateComponents(int switchNumber, int roomNumber, boolean switchState, boolean roomState) {
        switch (switchNumber) {
            case 0:
                if (switchState == true) {
                    switchButton1.setImageResource(R.drawable.switchon);
                    //switches.get(switchNumber).setSwitchState(true);
                } else {
                    switchButton1.setImageResource(R.drawable.switchoff);
                    //switches.get(switchNumber).setSwitchState(false);
                }
                break;
            case 1:
                if (switchState == true) {
                    switchButton2.setImageResource(R.drawable.switchon);
                    //switches.get(switchNumber).setSwitchState(true);
                } else {
                    switchButton2.setImageResource(R.drawable.switchoff);
                    //switches.get(switchNumber).setSwitchState(false);
                }
                break;
            case 2:
                if (switchState == true) {
                    switchButton3.setImageResource(R.drawable.switchon);
                    //switches.get(switchNumber).setSwitchState(true);
                } else {
                    switchButton3.setImageResource(R.drawable.switchoff);
                    //switches.get(switchNumber).setSwitchState(false);
                }
                break;
            case 3:
                if (switchState == true) {
                    switchButton4.setImageResource(R.drawable.switchon);
                    //switches.get(switchNumber).setSwitchState(true);
                } else {
                    switchButton4.setImageResource(R.drawable.switchoff);
                    //switches.get(switchNumber).setSwitchState(false);
                }
                break;
        }

        switch (roomNumber) {
            case 1:
                if (roomState == true) {
                    turnOnRoom(1);
                    switches.get(switchNumber).setRoomState(true);
                } else {
                    turnOffRoom(1);
                    switches.get(switchNumber).setRoomState(false);
                }
                break;
            case 2:
                if (roomState == true) {
                    turnOnRoom(2);
                    switches.get(switchNumber).setRoomState(true);
                } else {
                    turnOffRoom(2);
                    switches.get(switchNumber).setRoomState(false);
                }
                break;
            case 3:
                if (roomState == true) {
                    turnOnRoom(3);
                    switches.get(switchNumber).setRoomState(true);
                } else {
                    turnOffRoom(3);
                    switches.get(switchNumber).setRoomState(false);
                }
                break;
            case 4:
                if (roomState == true) {
                    turnOnRoom(4);
                    switches.get(switchNumber).setRoomState(true);
                } else {
                    turnOffRoom(4);
                    switches.get(switchNumber).setRoomState(false);
                }
                break;
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public int[] shuffleArray(int[] choicesArray) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = choicesArray.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = choicesArray[index];
            choicesArray[index] = choicesArray[i];
            choicesArray[i] = a;
        }
        return choicesArray;
    }

    public boolean getRandomBoolean() {
        return statusRandom.nextBoolean();
    }

    //runs without a timer by reposting this handler at the end of the runnable
    Handler randomizeLitRoomHandler = new Handler();
    Runnable randomizeLitRoomRunnable = new Runnable() {

        @Override
        public void run() {
            if (running) {
                // TODO randomize room status
                randomizeAllRoomStatus();

                randomizeLitRoomHandler.postDelayed(this, RANDOMIZE_SPEED);
            }
        }
    };

    //runs without a timer by reposting this handler at the end of the runnable

    // every when does SCORE and MONEY MINUS update
    Handler hudUpdateHandler = new Handler();
    Runnable hudUpdateRunnable = new Runnable() {

        @Override
        public void run() {
            if (running) {
                int seconds = time;
                // for testing only
//                TextView timeTextView = (TextView) findViewById(R.id.tv_time);
//                timeTextView.setText(String.format("%d", seconds));
                time++;

                // TODO randomize room status
                if (!ifFrozen) {
                    updateMoneyValue();
                } else checkIfFrozenTimesUp();

                if( RANDOMIZE_COUNTER>0 ) {
                    if (moneyValue < STARTING_COINS / 2) {
                        centerTextView.setText("Everything is different now...");
                        AudioPlayer.playMusic(getApplicationContext(), R.raw.intense);
                        animator.setDuration(5000L);
                        centerTextView.startAnimation(freezeFadeoutAnim);
                        centerTextView.setVisibility(View.INVISIBLE);
                        //randomizeAllRoomStatus();
                        refreshSwitches();
                        RANDOMIZE_COUNTER--;
                        RANDOMIZE_SPEED = 1000;
                        POSSIBLE_LIGHTS_ON = 2;
                    }
                }

                if(moneyValue < 0)
                    moneyValue = 0;

                updateHUD(moneyValue, scoreValue);

                int timeToReact = 1000;
                randomizeLitRoomHandler.postDelayed(this, timeToReact);
            }
        }
    };


    private boolean checkIfFrozenTimesUp() {
        if (time == endOfFreezing) {
            ifFrozen = false;
            endOfFreezing = -1;
            return false;
        } else return true;
    }


    public void updateHUD(int updatedMoney, int updatedScore) {
        moneyTextView.setText(String.format("%d", updatedMoney));
        //scoreTextView.setText(String.format("%d", updatedScore));


    }

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

    public void updateMoneyValue() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int totalPointsLost = 0;
        for (Switch switchObject : switches) {
            if (switchObject.getRoomState() == true)
                totalPointsLost += POINTS_LOST;
        }
        playDeductionAnimation(totalPointsLost);

        if (moneyValue - totalPointsLost <= 0) {
            running = false;
            System.out.println("SCORE " + scoreValue);
            editor.putInt("CurrentScore", scoreValue);
            editor.apply();
            System.out.println("SCORE2 " + scoreValue);
            Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
            startActivity(intent);
            finish();
        }
        moneyValue -= totalPointsLost;
    }

    public void playDeductionAnimation(int totalPointsLost) {
        if (totalPointsLost == 0)
            deductionTextView.setVisibility(View.GONE);
        else {
            deductionTextView.setText("-" + String.format("%d", totalPointsLost));
            deductionTextView.startAnimation(slideDownAnim);
            slideDownAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    deductionTextView.startAnimation(fadeOutAnim);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void randomizeAllRoomStatus() {
        // get number of lit rooms
        int numOfLitRooms = 0;
        for (int i = 0; i < switches.size(); i++) {
            Switch switchObject = switches.get(i);
            if (switchObject.isRoomState() == true) {
                numOfLitRooms++;
            }
        }
        //randomize room states
        for (int i = 0; i < switches.size(); i++) {
            Switch switchObject = switches.get(i);
            if (numOfLitRooms < POSSIBLE_LIGHTS_ON) {
                if (switchObject.isRoomState() == false) {
                    Boolean roomState = getRandomBoolean();
                    if (roomState == true) {
                        numOfLitRooms++;
                        switchObject.setRoomState(roomState);
                        switchObject.setIsSwitchedByAI(true);
                        updateComponents(i, switchObject.getRoomNumber(), switchObject.getSwitchState(), switchObject.getRoomState());
                    }
                }
            } else break;
        }
    }

    // POWERUPS
    public void activateFreezeTime() { //because time is money
        int numOfFreezeTime = sharedPreferences.getInt("powerup1Count", 0);
        if (numOfFreezeTime > 0) {

            AudioPlayer.playSFX(getApplicationContext(), R.raw.freezesfx);

            editor.putInt("powerup1Count", numOfFreezeTime - 1);
            editor.apply();
            freezeTimeCountTextView.setText(String.valueOf(numOfFreezeTime-1));
            ifFrozen = true;

            freezeScreenImageView.setImageResource(R.drawable.freeze_screen);
            freezeScreenImageView.startAnimation(freezeFadeoutAnim);
            freezeScreenImageView.setVisibility(ImageView.INVISIBLE);

//            Toast.makeText(getBaseContext(), "LET IT GO?? LET IT GO???",
//                    Toast.LENGTH_SHORT).show();

            // BECOZ 5 SECONDS YUNG TAGAL NG FREEZING
            endOfFreezing = time + 5;
        } else {// VIBRATOR TURN ON
            Vibrator vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            vibrator.vibrate(VIBRATION_DURATION);
        }
    }

    public void activateBrownOut() {
        int numOfBrownOuts = sharedPreferences.getInt("powerup2Count", 0);
        System.out.println("num1 " + numOfBrownOuts);
        if (numOfBrownOuts > 0) {
            AudioPlayer.playSFX(getApplicationContext(), R.raw.brownoutsfx);

            freezeScreenImageView.setImageResource(R.drawable.brownout_screen);
            freezeScreenImageView.startAnimation(streakFadeoutAnim);
            freezeScreenImageView.setVisibility(ImageView.INVISIBLE);

            editor.putInt("powerup2Count", numOfBrownOuts - 1);
            editor.apply();
            brownoutCountTextView.setText(String.valueOf(numOfBrownOuts - 1));
            for (int i = 1; i <= numOfRooms; i++)
                updateComponents(i - 1, i, false, false);
        } else {// VIBRATOR TURN ON
            Vibrator vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            vibrator.vibrate(VIBRATION_DURATION);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        running = false;
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        AudioPlayer.resumeMusic();
        randomizeLitRoomHandler.postDelayed(randomizeLitRoomRunnable, 0);
        hudUpdateHandler.postDelayed(hudUpdateRunnable, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioPlayer.pauseMusic();
        hudUpdateHandler.removeCallbacks(hudUpdateRunnable);
        randomizeLitRoomHandler.removeCallbacks(randomizeLitRoomRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        recycleImageView((ImageView) findViewById(R.id.freeze_screen));
        recycleImageView((ImageView) findViewById(R.id.background_sky1));
        recycleImageView((ImageView) findViewById(R.id.background_sky2));
        recycleImageView((ImageView) findViewById(R.id.house_design));
        recycleImageView((ImageView) findViewById(R.id.easy_room1));
        recycleImageView((ImageView) findViewById(R.id.easy_room2));
        recycleImageView((ImageView) findViewById(R.id.easy_room3));
        recycleImageView((ImageView) findViewById(R.id.easy_room4));

        ((ImageView) findViewById(R.id.freeze_screen)).setImageDrawable(null);
        ((ImageView) findViewById(R.id.background_sky1)).setImageDrawable(null);
        ((ImageView) findViewById(R.id.background_sky2)).setImageDrawable(null);
        ((ImageView) findViewById(R.id.house_design)).setImageDrawable(null);
        ((ImageView) findViewById(R.id.easy_room1)).setImageDrawable(null);
        ((ImageView) findViewById(R.id.easy_room2)).setImageDrawable(null);
        ((ImageView) findViewById(R.id.easy_room3)).setImageDrawable(null);
        ((ImageView) findViewById(R.id.easy_room4)).setImageDrawable(null);
    }

    private void recycleImageView(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }
    }

    //    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "EasyPlayGame Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.koalition.edu.lightsout/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "EasyPlayGame Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.koalition.edu.lightsout/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
}
