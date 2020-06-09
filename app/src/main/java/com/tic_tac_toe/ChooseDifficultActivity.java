package com.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import static com.tic_tac_toe.StartActivity.CLICKSOUNDID;

public class ChooseDifficultActivity extends AppCompatActivity {
    public static final String EASY="EASY";
    public static final String MEDIUM="MEDIUM";
    public static final String HARD="HARD";
    public static final String DIFFICULT="DIFFICULT";
    public static final String PVE = "PVE";
    public static final String GAME_TYPE = "GAME_TYPE";
    public static final String PREFERENCES= "PREFERENCES";
    public static final String IS_SOUND_ON="IS_SOUND_ON";
    SharedPreferences sharedPreferences;
    ImageButton easyButton,mediumButton,hardButton,soundButton,backButton;
    boolean sound_on_off;
    SoundPlayingV2 soundPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficult);
        sharedPreferences = this.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        easyButton = findViewById(R.id.easy);
        mediumButton = findViewById(R.id.medium);
        hardButton = findViewById(R.id.hard);
        soundButton=findViewById(R.id.button_sound);
        backButton=findViewById(R.id.button_back);
        sound_on_off=sharedPreferences.getBoolean(IS_SOUND_ON,true);
        soundPlaying=new SoundPlayingV2(soundButton,sharedPreferences,this,sound_on_off);


        easyButton.setOnClickListener(v -> onClick(EASY));
        mediumButton.setOnClickListener(v -> onClick(MEDIUM));
        hardButton.setOnClickListener(v -> onClick(HARD));
        soundButton.setOnClickListener(v ->soundPlaying.buttonSoundOnClick());
        backButton.setOnClickListener(v->back());
    }



    void onClick(String s){
        soundPlaying.playSoundOnClick(CLICKSOUNDID);
        Intent i= new Intent(this,MainActivity.class);
        i.putExtra(GAME_TYPE,PVE);
        i.putExtra(DIFFICULT,s);
        startActivity(i);
    }

    void back(){
        soundPlaying.playSoundOnClick(CLICKSOUNDID);
        Intent i= new Intent(this,StartActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        back();
    }
}
