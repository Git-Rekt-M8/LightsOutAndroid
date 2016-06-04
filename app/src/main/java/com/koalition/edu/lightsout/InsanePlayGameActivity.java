package com.koalition.edu.lightsout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class InsanePlayGameActivity extends Activity {


    ImageView room1Box;
    ImageView room2Box;
    ImageView room3Box;
    ImageView room4Box;
    ImageView room5Box;
    ImageView room6Box;

    ImageView switchButton1;
    ImageView switchButton2;
    ImageView switchButton3;
    ImageView switchButton4;
    ImageView switchButton5;
    ImageView switchButton6;

    ImageView freezeTimeButton;
    ImageView brownoutButton;

    ImageView freezeScreenImageView;
    Animation freezeFadeoutAnim;
    ImageView streakImageView;
    Animation streakFadeoutAnim;

    ImageView designImageView;

    TextView moneyTextView;
    TextView scoreTextView;
    TextView deductionTextView;
    TextView freezeTimeCountTextView;
    TextView brownoutCountTextView;

    private MediaPlayer mediaPlayer;
    private int numOfRooms;

    // GAME VARIABLES
    // timer for randomizing every randomizeSpeed
    static int RANDOMIZE_SPEED = 2000;
    int RANDOMIZE_COUNTER = 1;
    static int POINTS_LOST = 1;
    static int POINTS_GAINED = 40;
    static int POSSIBLE_LIGHTS_ON = 4;
    static int STARTING_COINS = 200;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insane_play_game);
        mediaPlayer = MediaPlayer.create(InsanePlayGameActivity.this, R.raw.mainmenu);
        running = true;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        room1Box = (ImageView) findViewById(R.id.easy_room1);
        room2Box = (ImageView) findViewById(R.id.easy_room2);
        room3Box = (ImageView) findViewById(R.id.easy_room3);
        room4Box = (ImageView) findViewById(R.id.easy_room4);
        room5Box = (ImageView) findViewById(R.id.easy_room5);
        room6Box = (ImageView) findViewById(R.id.easy_room6);
        switchButton1 = (ImageView) findViewById(R.id.easy_button1);
        switchButton2 = (ImageView) findViewById(R.id.easy_button2);
        switchButton3 = (ImageView) findViewById(R.id.easy_button3);
        switchButton4 = (ImageView) findViewById(R.id.easy_button4);
        switchButton5 = (ImageView) findViewById(R.id.easy_button5);
        switchButton6 = (ImageView) findViewById(R.id.easy_button6);

        freezeTimeButton = (ImageView) findViewById(R.id.iv_freeze_time);
        brownoutButton = (ImageView) findViewById(R.id.iv_brown_out);
        moneyTextView = (TextView) findViewById(R.id.tv_money);
        scoreTextView = (TextView) findViewById(R.id.tv_easy_score);
        freezeTimeCountTextView = (TextView) findViewById(R.id.tv_freeze_count);
        brownoutCountTextView = (TextView) findViewById(R.id.tv_brown_out_count);
        deductionTextView = (TextView) findViewById(R.id.tv_deduction);
        slideDownAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_down_deduction);
        fadeOutAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);

        designImageView = (ImageView) findViewById(R.id.house_design);

        freezeScreenImageView = (ImageView) findViewById(R.id.freeze_screen);
        freezeScreenImageView.setVisibility(ImageView.INVISIBLE);
        freezeFadeoutAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.freeze_fadeout);

        streakImageView = (ImageView) findViewById(R.id.streak_view);
        streakImageView.setVisibility(ImageView.INVISIBLE);
        streakFadeoutAnim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.streak_fadeout);

        // DESGIN STUFF
        currentDesign = sharedPreferences.getInt("CurrentDesign", 0);
        switch (currentDesign) {
            case 0: break;
            case 1: designImageView.setImageResource(R.drawable.green_custom); break;
            case 2: designImageView.setImageResource(R.drawable.nipa_custom);break;
        }

        numOfRooms = 6;
        editor.putString("lastPlayedDifficulty", "insane");
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
        randomizeLitRoomHandler.postDelayed(randomizeLitRoomRunnable, 0);
        hudUpdateHandler.postDelayed(hudUpdateRunnable, 0);
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
                                updateHUD(moneyValue, scoreValue);
                                streakValue++;
                                checkIfStreakBonus(streakValue);
                                switches.get(0).setIsSwitchedByAI(false);
                            }
                            switches.get(0).setRoomState(false);
                            turnOffRoom(switches.get(0).getRoomNumber());

                        } else if (switches.get(0).isRoomState() == false) {
                            streakValue = 0;

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
                                scoreValue += POINTS_GAINED;
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

        switchButton5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        switchButton5.setImageResource(R.drawable.switchoff_clk);

                        //=====Write down your Finger Pressed code here
                        if (switches.get(4).isRoomState() == true) {
                            // TODO add score
                            if (switches.get(4).getIsSwitchedByAI() == true) {
                                scoreValue += POINTS_GAINED;
                                updateHUD(moneyValue, scoreValue);
                                streakValue++;
                                checkIfStreakBonus(streakValue);
                                switches.get(4).setIsSwitchedByAI(false);
                            }
                            switches.get(4).setRoomState(false);
                            turnOffRoom(switches.get(4).getRoomNumber());

                        } else if (switches.get(4).isRoomState() == false) {
                            streakValue = 0;

                            switches.get(4).setRoomState(true);
                            turnOnRoom(switches.get(4).getRoomNumber());

                            // VIBRATOR TURN ON
                            Vibrator vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            vibrator.vibrate(VIBRATION_DURATION);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        switchButton5.setImageResource(R.drawable.switchoff);

                        //=====Write down you code Finger Released code here
                        return true;
                }
                return false;
            }
        });

        switchButton6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        switchButton6.setImageResource(R.drawable.switchoff_clk);

                        //=====Write down your Finger Pressed code here
                        if (switches.get(5).isRoomState() == true) {
                            // TODO add score
                            if (switches.get(5).getIsSwitchedByAI() == true) {
                                scoreValue += POINTS_GAINED;
                                updateHUD(moneyValue, scoreValue);
                                streakValue++;
                                checkIfStreakBonus(streakValue);
                                switches.get(5).setIsSwitchedByAI(false);
                            }
                            switches.get(5).setRoomState(false);
                            turnOffRoom(switches.get(5).getRoomNumber());

                        } else if (switches.get(5).isRoomState() == false) {
                            streakValue = 0;

                            switches.get(5).setRoomState(true);
                            turnOnRoom(switches.get(5).getRoomNumber());

                            // VIBRATOR TURN ON
                            Vibrator vibrator = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            vibrator.vibrate(VIBRATION_DURATION);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        switchButton6.setImageResource(R.drawable.switchoff);

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

            streakImageView.setImageResource(R.drawable.streak15_txt);
            streakImageView.startAnimation(streakFadeoutAnim);
            streakImageView.setVisibility(ImageView.INVISIBLE);

            updateHUD(moneyValue, scoreValue);
        } else if (streakValue >= 10) {
            scoreValue += 20;

            streakImageView.setImageResource(R.drawable.streak10_txt);
            streakImageView.startAnimation(streakFadeoutAnim);
            streakImageView.setVisibility(ImageView.INVISIBLE);

            updateHUD(moneyValue, scoreValue);
        } else
        if (streakValue >= 5) {
            scoreValue += 10;

            streakImageView.setImageResource(R.drawable.streak5_txt);
            streakImageView.startAnimation(streakFadeoutAnim);
            streakImageView.setVisibility(ImageView.INVISIBLE);

            updateHUD(moneyValue, scoreValue);
        }
    }

    public void turnOffRoom(int roomNumber) {
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
            case 5:
                room5Box.setImageResource(R.drawable.room5off);
                break;
            case 6:
                room6Box.setImageResource(R.drawable.room6off);
                break;
        }
    }

    public void turnOnRoom(int roomNumber) {
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
            case 5:
                room5Box.setImageResource(R.drawable.room5on);
                break;
            case 6:
                room6Box.setImageResource(R.drawable.room6on);
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
        int[] choicesArray = {1, 2, 3, 4, 5, 6};
        int[] switchChoiceArray = {0, 1, 2, 3, 4, 5};
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
            case 4:
                if (switchState == true) {
                    switchButton5.setImageResource(R.drawable.switchon);
                    //switches.get(switchNumber).setSwitchState(true);
                } else {
                    switchButton5.setImageResource(R.drawable.switchoff);
                    //switches.get(switchNumber).setSwitchState(false);
                }
                break;
            case 5:
                if (switchState == true) {
                    switchButton6.setImageResource(R.drawable.switchon);
                    //switches.get(switchNumber).setSwitchState(true);
                } else {
                    switchButton6.setImageResource(R.drawable.switchoff);
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
            case 5:
                if (roomState == true) {
                    turnOnRoom(5);
                    switches.get(switchNumber).setRoomState(true);
                } else {
                    turnOffRoom(5);
                    switches.get(switchNumber).setRoomState(false);
                }
                break;
            case 6:
                if (roomState == true) {
                    turnOnRoom(6);
                    switches.get(switchNumber).setRoomState(true);
                } else {
                    turnOffRoom(6);
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
                        streakImageView.setImageResource(R.drawable.everything_txt);
                        streakImageView.startAnimation(freezeFadeoutAnim);
                        streakImageView.setVisibility(ImageView.INVISIBLE);
                        //randomizeAllRoomStatus();
                        refreshSwitches();
                        RANDOMIZE_COUNTER--;
                        RANDOMIZE_SPEED = 1000;
                    }
                }

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
        scoreTextView.setText(String.format("%d", updatedScore));
    }

    public void updateMoneyValue() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int totalPointsLost = 0;
        for (Switch switchObject : switches) {
            if (switchObject.getRoomState() == true)
                totalPointsLost += POINTS_LOST;
        }
        playDeductionAnimation(totalPointsLost);
        moneyValue -= totalPointsLost;

        if (moneyValue <= 0) {
            running = false;
            System.out.println("SCORE " + scoreValue);
            editor.putInt("CurrentScore", scoreValue);
            editor.apply();
            System.out.println("SCORE2 " + scoreValue);
            Intent intent = new Intent(InsanePlayGameActivity.this, GameOverActivity.class);
            startActivity(intent);
            finish();
        }
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

            editor.putInt("powerup1Count", numOfFreezeTime - 1);
            editor.apply();
            freezeTimeCountTextView.setText(String.valueOf(numOfFreezeTime - 1));
            ifFrozen = true;

            freezeScreenImageView.setImageResource(R.drawable.freeze_screen);
            freezeScreenImageView.startAnimation(freezeFadeoutAnim);
            freezeScreenImageView.setVisibility(ImageView.INVISIBLE);

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
