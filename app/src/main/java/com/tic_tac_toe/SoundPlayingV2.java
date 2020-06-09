package com.tic_tac_toe;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.widget.ImageButton;

import static com.tic_tac_toe.ChooseDifficultActivity.IS_SOUND_ON;

public class SoundPlayingV2 {
    boolean sound_on_off;
    ImageButton soundButton;
    SharedPreferences sharedPreferences;
    Context context;
    SoundPoolHelper mSoundPool;

    public void setmSoundLessId(int mSoundLessId) {
        this.mSoundLessId = mSoundPool.load(context, mSoundLessId, 1);
    }

    public int getmSoundLessId() {
        return mSoundLessId;
    }

    private int mSoundLessId;

     SoundPlayingV2(ImageButton soundButton, SharedPreferences sharedPreferences,
                    Context context, boolean sound_on_off){
         this.soundButton = soundButton;
         this.sharedPreferences = sharedPreferences;
         this.context = context;
         this.sound_on_off = sound_on_off;
         soundButton.setImageDrawable(context.getResources().getDrawable(sound_on_off ?
                 R.drawable.button_sound_on_states : R.drawable.buttonsounofpressed));
         //mSoundLessId = mSoundPool.load(context, sound, 1);
         mSoundPool = new SoundPoolHelper(1, AudioManager.STREAM_MUSIC,0, context,getmSoundLessId() );

     }





    void buttonSoundOnClick(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            sound_on_off = !sharedPreferences.getBoolean(IS_SOUND_ON, true);
        }catch (NullPointerException npe){
            sound_on_off=true;
            editor.putBoolean(IS_SOUND_ON,sound_on_off);
        }
        editor.putBoolean(IS_SOUND_ON,sound_on_off);
        editor.apply();
        soundButton.setImageDrawable(context.getResources().getDrawable(sound_on_off?R.drawable.button_sound_on_states:R.drawable.buttonsounofpressed));
    }


    public void playSoundOnClick(int soundId) {
        // setmSoundLessId(soundId);

        if (sound_on_off) {
            mSoundPool.play(mSoundPool.load(context, soundId, 1));
        }
    }
}
