package com.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import static com.tic_tac_toe.ChooseDifficultActivity.IS_SOUND_ON;
import static com.tic_tac_toe.ChooseDifficultActivity.PREFERENCES;

public class StartActivity extends AppCompatActivity {
    ImageButton pvpButton, pveButton,soundButton;
    public static final String PVP = "PVP";
    public static final String PVE = "PVE";
    public static final String GAME_TYPE = "GAME_TYPE";
    static boolean sound_on_off;
    public static final int CLICKSOUNDID = R.raw.button_click;
    SharedPreferences sharedPreferences;
    SoundPlayingV2 soundPlaying;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        sharedPreferences = this.getSharedPreferences(PREFERENCES,MODE_PRIVATE);
        pvpButton = findViewById(R.id.easy);
        pveButton = findViewById(R.id.medium);
        soundButton=findViewById(R.id.button_sound);


        try {
            sound_on_off=sharedPreferences.getBoolean(IS_SOUND_ON,true);
        }catch (NullPointerException npe){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean(IS_SOUND_ON,true);
            editor.apply();
            sound_on_off=true;
        }
        soundPlaying=new SoundPlayingV2(soundButton,sharedPreferences,this,sound_on_off);
        pveButton.setOnClickListener(v -> onClick(PVE, ChooseDifficultActivity.class));
        pvpButton.setOnClickListener(v -> onClick(PVP, MainActivity.class));
        soundButton.setOnClickListener(v->soundPlaying.buttonSoundOnClick());



    }

    void onClick(String s, Class clazz) {
        soundPlaying.playSoundOnClick(CLICKSOUNDID);
        Intent i = new Intent(this, clazz);
        i.putExtra(GAME_TYPE, s);
        startActivity(i);
    }

}
