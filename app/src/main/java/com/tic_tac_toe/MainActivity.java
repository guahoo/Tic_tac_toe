package com.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static com.tic_tac_toe.ChooseDifficultActivity.DIFFICULT;
import static com.tic_tac_toe.ChooseDifficultActivity.EASY;
import static com.tic_tac_toe.ChooseDifficultActivity.IS_SOUND_ON;
import static com.tic_tac_toe.ChooseDifficultActivity.MEDIUM;
import static com.tic_tac_toe.ChooseDifficultActivity.PREFERENCES;
import static com.tic_tac_toe.MainActivity.FieldKind.UNCHECKED;
import static com.tic_tac_toe.MainActivity.FieldKind.X;
import static com.tic_tac_toe.MainActivity.FieldKind.O;
import static com.tic_tac_toe.R.drawable.*;
import static com.tic_tac_toe.StartActivity.CLICKSOUNDID;
import static com.tic_tac_toe.StartActivity.GAME_TYPE;
import static com.tic_tac_toe.StartActivity.PVP;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    boolean sound_on_off;

    ImageButton resetButton, backButton, soundButton;

    ImageView[] win_count_sign_x;
    ImageView[] win_count_sign_o;

    SoundPlayingV2 soundPlaying;
    //ImageButton[] buttons;
    //FieldKind[] field_values = new FieldKind[9];
    TextView winStatusTextView, player_o_label;
    SoundPoolHelper soundPoolHelper;
    String ai = "Ai";


    Artificial_Intelligence aI;
    boolean isCenterCapture;
    boolean isWarning = false;
    boolean isPvP;
    String difficultLevel;
    ArrayList<ImageButton>buttons;


    boolean isWinner;
    FieldKind tic_tac = X;
    private int winCountO = 0;
    private int winCountX = 0;

    Map<ImageButton,FieldKind> buttonFieldKindMap;




    public enum FieldKind {
        O("1", ic_o, field_0),
        X("2", ic_x, field_x),
        UNCHECKED(null, -1, -1);

        String winner;
        int drawableResoure;
        int drawableWinField;

         FieldKind(String winner, int drawableResoure, int drawableWinField) {
            this.winner = winner;
            this.drawableResoure = drawableResoure;
            this.drawableWinField = drawableWinField;
        }

        public String getWinnerText() {
            return String.format("Player %s WIN", winner);
        }

        public int getDrawableResoure() {
            return drawableResoure;
        }

        public int getDrawableWinField() {
            return drawableWinField;
        }

        public FieldKind next() {
            return this == X ? O : X;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        for (ImageButton button : buttons) {
            button.setOnClickListener(v -> setOnclick(button));
        }
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (winCountO == 3 | winCountX == 3) back();
                else reset();
            }
        });
        backButton.setOnClickListener(v -> back());
        soundButton.setOnClickListener(v -> soundPlaying.buttonSoundOnClick());

        //checkWin();


    }

    private void init() {
        sharedPreferences = this.getSharedPreferences(PREFERENCES, MODE_PRIVATE);


        buttonFieldKindMap=new LinkedHashMap<>();
        buttonFieldKindMap.put(findViewById(R.id.button1),UNCHECKED);
        buttonFieldKindMap.put(findViewById(R.id.button2),UNCHECKED);
        buttonFieldKindMap.put(findViewById(R.id.button3),UNCHECKED);
        buttonFieldKindMap.put(findViewById(R.id.button4),UNCHECKED);
        buttonFieldKindMap.put(findViewById(R.id.button5),UNCHECKED);
        buttonFieldKindMap.put(findViewById(R.id.button6),UNCHECKED);
        buttonFieldKindMap.put(findViewById(R.id.button7),UNCHECKED);
        buttonFieldKindMap.put(findViewById(R.id.button8),UNCHECKED);
        buttonFieldKindMap.put(findViewById(R.id.button9),UNCHECKED);

        buttons=(new ArrayList<>(buttonFieldKindMap.keySet()));

        for (int i = 0; i < buttons.size(); ++i) {
            buttonFieldKindMap.put(buttons.get(i),UNCHECKED);
        }

        win_count_sign_x = new ImageView[]{findViewById(R.id.score_x_1), findViewById(R.id.score_x_2), findViewById(R.id.score_x_3)};
        win_count_sign_o = new ImageView[]{findViewById(R.id.score_o_1), findViewById(R.id.score_o_2), findViewById(R.id.score_o_3)};


        resetButton = findViewById(R.id.resetButton);
        winStatusTextView = findViewById(R.id.win_status_text_view);
        backButton = findViewById(R.id.button_back);
        soundButton = findViewById(R.id.button_sound);
        sound_on_off = sharedPreferences.getBoolean(IS_SOUND_ON, true);
        soundPlaying = new SoundPlayingV2(soundButton, sharedPreferences, this, sound_on_off);
        player_o_label = findViewById(R.id.player_o_label);





        stateGameType();
        aI = new Artificial_Intelligence();

        if (!isPvP) {
            player_o_label.setText(ai);
            player_o_label.setTextColor(getResources().getColor(R.color.red));
        }

    }

    private void checkWin() {
        horizontalChecking();
        verticalChecking();
        diagonalChecking();

    }


    private void winSituation(ImageButton a, ImageButton b, ImageButton c) {
        a.setForeground(getDrawable(tic_tac));
        b.setForeground(getDrawable(tic_tac));
        c.setForeground(getDrawable(tic_tac));

        if (tic_tac == X) {
            winStatusTextView.setTextColor(getResources().getColor(R.color.blue));
            winCountX++;
            setColorFilterWinCount(winCountX, win_count_sign_x, x_blue);

        } else {
            winStatusTextView.setTextColor(getResources().getColor(R.color.red));
            winCountO++;
            setColorFilterWinCount(winCountO, win_count_sign_o, o_red);
        }

        winStatusTextView.setVisibility(View.VISIBLE);
        winStatusTextView.setText(tic_tac.getWinnerText());

        for (ImageButton button : buttons) {
            button.setEnabled(false);
        }




        absoluteWin(tic_tac.getDrawableWinField());

    }

    private Drawable getDrawable(FieldKind kind) {
        return getDrawable(kind.getDrawableWinField());
    }

    private void absoluteWin(int id) {


        if (winCountX == 3 | winCountO == 3) {
            resetButton.setImageResource(R.drawable.new_game_button_state);


            for (ImageButton button : buttons
            ) {
                button.setForeground(getDrawable(id));
            }
        }

    }


    private void setColorFilterWinCount(int count, ImageView[] imageViews, int drawable) {
        for (int i = 0; i < count; i++) {
            imageViews[i].setImageResource(drawable);
        }

    }

    private void horizontalChecking() {

        abstractWinChecking(new int[][]{{0, 1, 2}, {3, 4, 5}, {6, 7, 8}});
    }

    private void verticalChecking() {
        abstractWinChecking(new int[][]{{0, 3, 6}, {1, 4, 7}, {2, 5, 8}});

    }

    private void diagonalChecking() {
        abstractWinChecking(new int[][]{{0, 4, 8}, {2, 4, 6}});


    }

    void setOnclick(final ImageButton b) {

        if (!isWinner) {
            soundPlaying.playSoundOnClick(CLICKSOUNDID);

            setBackground(b, tic_tac);
            checkWin();

            tic_tac = tic_tac.next();
            b.setEnabled(false);


            if (!isPvP && !isWinner) {
                for (ImageButton button : buttons
                ) {
                    button.setEnabled(false);

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        aI.move = false;
                        aI.move();
                        isWarning = false;
                        MainActivity.this.unblocking_all_free_fields();
                    }
                }, 500);

            }
        }


    }

    void abstractWinChecking(int[][] intArray) {
        for (int[] innerArray : intArray) {

            ImageButton buttonA = (new ArrayList<ImageButton>(buttonFieldKindMap.keySet()).get(innerArray[0]));
            ImageButton buttonB = (new ArrayList<ImageButton>(buttonFieldKindMap.keySet()).get(innerArray[1]));
            ImageButton buttonC = (new ArrayList<ImageButton>(buttonFieldKindMap.keySet()).get(innerArray[2]));

//            FieldKind aKind = field_values[innerArray[0]];
//            FieldKind bKind = field_values[innerArray[1]];
//            FieldKind cKind = field_values[innerArray[2]];

            FieldKind aKind = buttonFieldKindMap.get(buttonA);
            FieldKind bKind = buttonFieldKindMap.get(buttonB);
            FieldKind cKind = buttonFieldKindMap.get(buttonC);

            if (aKind == bKind && aKind == cKind && aKind != UNCHECKED) {

                isWinner = true;
                winSituation(buttonA, buttonB, buttonC);

            }

        }
    }


    private void reset() {
        soundPlaying.playSoundOnClick(CLICKSOUNDID);


        for (ImageButton button :buttons)

         {
            button.setEnabled(true);
            button.setForeground(null);
            button.setBackground(getDrawable(button_main_states));
        }


        //for (int i = 0; i < field_values.length; field_values[i++] = UNCHECKED);
        for (int i = 0; i < buttonFieldKindMap.size(); buttonFieldKindMap.put(buttons.get(i++),UNCHECKED));
        isWinner = false;
        tic_tac = X;
        isCenterCapture = false;
        isWarning = false;
        winStatusTextView.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            buttonFieldKindMap.entrySet().forEach(imageButtonFieldKindEntry -> {
                System.out.println(imageButtonFieldKindEntry.getKey()+""+imageButtonFieldKindEntry.getValue());
            });
        }
    }

    void back() {
        soundPlaying.playSoundOnClick(CLICKSOUNDID);
        Intent i = new Intent(this, StartActivity.class);
        startActivity(i);
    }

    public void stateGameType() {
        Intent intent = getIntent();
        if (intent.hasExtra(GAME_TYPE)) {
            Bundle extras = getIntent().getExtras();
            assert extras != null;
            String gameType = extras.getString(GAME_TYPE);
            if (intent.hasExtra(DIFFICULT)) {
                difficultLevel = extras.getString(DIFFICULT);
            }
            assert gameType != null;
            if (gameType.equals(PVP)) {
                isPvP = true;
            }
        }
    }

    void setBackground(ImageButton b, FieldKind kind) {
        b.setForeground(getDrawable(kind.getDrawableResoure()));
        b.setBackground(getDrawable(window_on));
        //field_values[getButtonNo(b)] = kind;

        //todo
        buttonFieldKindMap.put(b,kind);
    }

    private int getButtonNo(ImageButton checkingItem) {
        for (int i = 0; i < buttons.size(); ++i) {
            if (checkingItem.equals(buttons.get(i))) {
                return i;
            }
        }
        return -1;
    }

    void unblocking_all_free_fields() {
        for (int value = 0; value < buttonFieldKindMap.size(); value++) {
            if (buttonFieldKindMap.get(buttons.get(value)) == UNCHECKED) {
                buttons.get(value).setEnabled(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }


    class Artificial_Intelligence {

        boolean move;
        boolean cornersCapture;


        void move() {

            if (!move) {
                centerCapture();
                if (!difficultLevel.equals(EASY)) {
                    global_two_field_warning();
                }
                if (isCenterCapture && !move && !isWarning) {
                    cornersCapture();
                }
            }
        }


        void centerCapture() {

            if (buttonFieldKindMap.get(buttons.get(4)) == UNCHECKED) {
                setOnclickAI(buttons.get(4));
            }
            isCenterCapture = true;
        }

        void cornersCapture() {
            cornersCapture = false;
            ArrayList<Integer> cornerFields = new ArrayList<>();
            int[] i = buttonFieldKindMap.get(buttons.get(4)) == X ? new int[]{0, 2, 6, 8} : new int[]{1, 3, 5, 7};
            for (int value : i) {
                cornerFields.add(value);
            }


            while (!cornersCapture && cornerFields.size() != 0) {
                Random random = new Random();
                int r = random.nextInt(cornerFields.size());
                int corner = cornerFields.get(r);

                if (buttonFieldKindMap.get(buttons.get(corner)) == UNCHECKED) {
                    setOnclickAI(buttons.get(corner));
                    cornersCapture = true;
                }
                cornerFields.remove(r);


                if (cornerFields.size() == 0) {
                    global_two_field_warning();
                }

            }
        }


        void global_two_field_warning() {

            for (FieldKind fieldCount : FieldKind.values()) {
                abstract_two_fields_warning(new int[][]{
                        {0, 4, 8},
                        {2, 4, 6},
                        {0, 1, 2},
                        {3, 4, 5},
                        {6, 7, 8},
                        {0, 3, 6},
                        {1, 4, 7},
                        {2, 5, 8},
                }, fieldCount);

                if (fieldCount == X) {

                    cornerCombination_two_fields_warning(new int[][]{
                              {1, 3, 0}
                            , {1, 5, 2}
                            , {7, 3, 6}
                            , {7, 5, 8}
                            , {7, 5, 8}

                    }, fieldCount);
//задействуется, когда противоположный знак занял два поля, соседних с углом


                    if (!difficultLevel.equals(EASY) && !difficultLevel.equals(MEDIUM)) {

                        cornerCombination_two_fields_warning(new int[][]{
                                  {1, 6, 0}
                                , {1, 8, 2}
                                , {0, 5, 2}
                                , {6, 5, 8}
                                , {7, 2, 8}
                                , {7, 0, 6}
                                , {3, 8, 6}
                                , {3, 2, 0}

                        }, fieldCount);

//задействуется, когда
                    }
                }
            }


        }

        void cornerCombination_two_fields_warning(int[][] intArray, FieldKind fc) {
            for (int[] innerArray : intArray) {
                FieldKind aF = buttonFieldKindMap.get(buttons.get(innerArray[0]));
                FieldKind bF = buttonFieldKindMap.get(buttons.get(innerArray[1]));
                FieldKind cF = buttonFieldKindMap.get(buttons.get(innerArray[2]));


                if ((aF == bF && aF == fc)) {

                    if (cF == UNCHECKED && !move) {
                        setOnclickAI(buttons.get(innerArray[2]));
                    }
                }
            }

        }

        void abstract_two_fields_warning(int[][] intArray, FieldKind fc) {
            for (int[] innerArray : intArray) {
                FieldKind aF = buttonFieldKindMap.get(buttons.get(innerArray[0]));
                FieldKind bF = buttonFieldKindMap.get(buttons.get(innerArray[1]));
                FieldKind cF = buttonFieldKindMap.get(buttons.get(innerArray[2]));


                if ((aF == bF && aF == fc || bF == cF && bF == fc || aF == cF && cF == fc)) {

                    if (aF == UNCHECKED && !move) {
                        setOnclickAI(buttons.get(innerArray[0]));
                    } else if (bF == UNCHECKED && !move) {
                        setOnclickAI(buttons.get(innerArray[1]));
                    } else if (cF == UNCHECKED && !move) {
                        setOnclickAI(buttons.get(innerArray[2]));
                    }
                }
            }
        }


        void setOnclickAI(final ImageButton b) {


            if (buttonFieldKindMap.get(buttons.get(getButtonNo(b))) == UNCHECKED) {

                setBackground(b, tic_tac);
                b.setEnabled(false);

                checkWin();
                tic_tac = tic_tac.next();
                move = true;

            }
        }

    }

}






