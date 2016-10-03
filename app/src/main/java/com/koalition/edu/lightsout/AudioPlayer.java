package com.koalition.edu.lightsout;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.provider.Settings;

/**
 * Created by Jolo Simeon on 10/2/2016.
 */
public class AudioPlayer {

    private static MediaPlayer mediaPlayer = null;
    private static SoundPool soundPool = null;
    private static boolean isPlayingMusic = false;
    private static boolean isMusicPaused = false;

    private static boolean musicSetting = false;
    private static boolean SFXSetting = false;

    private static SharedPreferences sharedPreferences;

    public static void getAudioSettings(Context c){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        musicSetting = sharedPreferences.getBoolean("Music", false);
        SFXSetting = sharedPreferences.getBoolean("SFX", false);
    }

    public static void playMusic(Context c, int id){
        getAudioSettings(c);
        System.out.println("LETS SING " + musicSetting);
        // soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);

        if(musicSetting == true && !isPlayingMusic) {
            if (mediaPlayer == null)
                mediaPlayer = MediaPlayer.create(c,id);
            System.out.println("LETS PARTEY");
            isPlayingMusic = true;
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(1.0f, 1.0f);
        }
    }


    public static void pauseMusic(){
        System.out.println("Pause ka muna boi");
        isPlayingMusic = false;
        isMusicPaused = true;
        mediaPlayer.pause();
    }

    public static void stopMusic(){
        isPlayingMusic = false;
        mediaPlayer.stop();
    }

}
