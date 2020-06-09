package com.tic_tac_toe;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.widget.ImageButton;

import static com.tic_tac_toe.ChooseDifficultActivity.IS_SOUND_ON;

public class SoundPlaying {
    boolean sound_on_off;
    ImageButton soundButton;
    SharedPreferences sharedPreferences;
    Context context;

     SoundPlaying(ImageButton soundButton,SharedPreferences sharedPreferences,Context context,boolean sound_on_off){
        this.soundButton=soundButton;
        this.sharedPreferences=sharedPreferences;
        this.context=context;
        this.sound_on_off=sound_on_off;
        soundButton.setImageDrawable(context.getResources().getDrawable(sound_on_off?R.drawable.button_sound_on_states:R.drawable.buttonsounofpressed));


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

    protected void playSoundOnClick() {

        if(sound_on_off) {
            MediaPlayer mp;

            mp = MediaPlayer.create(context, R.raw.clicksound);

            try {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                    mp = MediaPlayer.create(context, R.raw.clicksound);
                }
                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
