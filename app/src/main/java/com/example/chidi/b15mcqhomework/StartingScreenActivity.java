package com.example.chidi.b15mcqhomework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StartingScreenActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_INT =2; //it could be any + integer value

    //we need an int where we save our high score in. we need a reference to our hight score text view to show our high score
    public static final String  SHARED_PREFERENCES = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore"; //This is the key under which we save
    // our high score in shared preferences
    private TextView textViewHighscore; // text view on which we will display our high score

    private int highscore;//Integer to be used to capture the value of this high score


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);


        textViewHighscore = findViewById(R.id.text_view_highscore);

        loadHighscore();

        Button buttonStartQuiz = findViewById(R.id.button_quiz_start);


        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz();
            }
        });
    }

    //Private, because we only need access to this method in this class.
    private void startQuiz() {
        Intent intent = new Intent(StartingScreenActivity.this, QuizActivity.class);
        //startActivity(intent);//in order to save the score when we come to the end of the app, we need to startActivityForResult
        //and also pass a regress code.
        startActivityForResult(intent, REQUEST_CODE_INT);
    }

    //we override the onActivity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_INT){
            if(resultCode == RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if(score > highscore){
                    updateHighscore(score);
                }
            }
        }

    }

    private void loadHighscore(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Highscore: " + highscore);
    }
    private void updateHighscore(int highscorenew){
        highscore = highscorenew;
        textViewHighscore.setText("Highscore: " + highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }
}
