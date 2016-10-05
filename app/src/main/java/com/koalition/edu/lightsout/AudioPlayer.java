package com.koalition.edu.lightsout;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.provider.Settings;

import java.util.HashMap;

/**
 * Created by Jolo Simeon on 10/2/2016.
 */
public class AudioPlayer {

    private static MediaPlayer mediaPlayer = null;
    private static SoundPool soundPool = null;

    private static int songPlaying;
    private static boolean isPlayingMusic = false;
    private static boolean isMusicPaused = true;

    private static boolean musicSetting = false;
    private static boolean SFXSetting = false;

    private static SharedPreferences sharedPreferences;

    private static HashMap<Integer, Integer> SFXlist = new HashMap<>();

    public static void initSFX(Context c){
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        /*soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });*/


        SFXlist.put(R.raw.upsfx, soundPool.load(c, R.raw.upsfx, 1));
        SFXlist.put(R.raw.downsfx, soundPool.load(c, R.raw.downsfx, 1));
        SFXlist.put(R.raw.switchsfx, soundPool.load(c, R.raw.switchsfx, 1));
    }

    public static void playSFX(Context c, int id){
        getAudioSettings(c);
        if (SFXSetting)
            soundPool.play(SFXlist.get(id), 1f, 1f, 1, 0, 1f);
    }

    public static void getAudioSettings(Context c){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        musicSetting = sharedPreferences.getBoolean("Music", false);
        SFXSetting = sharedPreferences.getBoolean("SFX", false);
    }

    public static void resumeMusic(){
        if (musicSetting && mediaPlayer != null && isMusicPaused == true) {
            isMusicPaused = false;
            isPlayingMusic = true;
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(1.0f, 1.0f);
        }
    }

    public static void playMusic(Context c, int id){
        getAudioSettings(c);
        System.out.println("LETS SING " + musicSetting);

        if(musicSetting && (!isPlayingMusic || songPlaying != id)) {
            if (mediaPlayer != null)
                pauseMusic();
            if (mediaPlayer == null || songPlaying != id) {
                mediaPlayer = MediaPlayer.create(c, id);
                songPlaying = id;
            }
            System.out.println("LETS PARTEY");
            isMusicPaused = false;
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
