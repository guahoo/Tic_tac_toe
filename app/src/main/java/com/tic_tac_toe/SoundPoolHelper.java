package com.tic_tac_toe;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashSet;
import java.util.Set;

public class SoundPoolHelper extends SoundPool {
    private Set<Integer> mLoaded;
    private Context mContext;
    int soundId;

    public SoundPoolHelper(int maxStreams, Context context,int soundId) {
        this(maxStreams, AudioManager.STREAM_MUSIC, 0, context,soundId);
    }

    public SoundPoolHelper(int maxStreams, int streamType, int srcQuality, Context context,int soundId) {
        super(maxStreams, streamType, srcQuality);
        mContext = context;
        mLoaded = new HashSet<Integer>();
        mLoaded.add(soundId);
        setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            mLoaded.add(sampleId);
            play(sampleId);
        });
    }

    public void play(int soundID) {
        AudioManager audioManager = (AudioManager) mContext.getSystemService( Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        float volume = actualVolume / maxVolume;
        // Is the sound loaded already?
        if (mLoaded.contains(soundID)) {
            play(soundID, volume, volume, 1, 0, 1f);
        }
    }
}