package com.example.chidi.b15mcqhomework;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    //From the quizActivity, we will request the questions from our database.
    public static final String EXTRA_SCORE = "extraScore"; //we want to send our final score back to the Starting Screen Activity

    private TextView textViewQuestions;
    private TextView textViewScore;
    private TextView getTextViewQuestionCount;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button next;
    private Button previous;

    private ColorStateList textColorDefaultRb;

    private List<Question> questionList;

    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    //This determines what happens when the next button is clicked; either look up the answer,if the question
//has not yet been answered, or show the next question, if the question has already been answered
    private boolean answered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestions = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        getTextViewQuestionCount = findViewById(R.id.text_view_question_count);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_buttonA);
        rb2 = findViewById(R.id.radio_buttonB);
        rb3 = findViewById(R.id.radio_buttonC);
        rb4 = findViewById(R.id.radio_buttonD);
        next = findViewById(R.id.button_next);
        previous = findViewById(R.id.button_previous);

        textColorDefaultRb = rb4.getTextColors(); //saves the default textcolor of the radio button in the
        // textcolordefaultRb variable


        QuizDbHelper dbHelper = new QuizDbHelper(this); // instantiating our db Helper
        questionList = dbHelper.getAllQuestions();//calling this method creates our database,which did not exist hitherto

        questionCountTotal = questionList.size();
        //to shuffle our question lists, so that we get them in a random other, we write;

      //  Collections.shuffle(questionList);//randomizes the order of the questions

        //when we start this activity, we immediately want to show the first question.
        // this is put into the following logic;

        showNextQuestion();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered){ // if the answer is false or the question has not yet been answered
                    if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked()||rb4.isChecked()){
                        checkAnswer();
                    }
                    else{
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    showNextQuestion();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPreviousQuestion();
            }
        });

    }

    private void showPreviousQuestion() {

        if (questionCounter > 0) {
            currentQuestion = questionList.get(questionCounter);

            //gets the question string for the current question and then sets it as a text in textViewQuestion
            textViewQuestions.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOptionA());
            rb2.setText(currentQuestion.getOptionB());
            rb3.setText(currentQuestion.getOptionC());
            rb4.setText(currentQuestion.getOptionD());

            questionCounter--;
            getTextViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
           answered = true;

            //next.setText("Confirmed");
        } else {
            finishQuiz();
        }

    }

    private void showNextQuestion() {
        //we need to reset the text colors of our radio buttons here, since when we show the answer,
        //we will reset the button to green or red.
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);

        //we want to clear our collection, since when we show our questions, we want all radio buttons
        // to be unselected
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            //gets the question string for the current question and then sets it as a text in textViewQuestion
            textViewQuestions.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOptionA());
            rb2.setText(currentQuestion.getOptionB());
            rb3.setText(currentQuestion.getOptionC());
            rb4.setText(currentQuestion.getOptionD());

            questionCounter++;
            getTextViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;

            next.setText("Confirmed");
        } else {
            finishQuiz();
        }

    }
    private void checkAnswer(){
        answered = true;
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        //This will return the id of whichever radio button was checked, and then we can save this
        //radio button in rbSelected variable.
        //Since we saved the correct answer in the form of a number in our database table,
        //we also want to convert the selected radio button into a number, thus;
        int answer = rbGroup.indexOfChild(rbSelected) +1 ;
        //This returns the index of the selected radio button. we add 1, since our answer starts at 1

        if (answer == currentQuestion.getAnswer()){
            // then our question was answered correctly.
            score++;
            textViewScore.setText("score: "+ score);
        }
        showSolution();
    }

    private void showSolution(){
        // we set all the radio button colors to red.
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (currentQuestion.getAnswer()){

            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestions.setText("Answer A is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestions.setText("Answer B is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestions.setText("Answer C is correct");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                textViewQuestions.setText("Answer D is correct");
                break;
        }

        if (questionCounter < questionCountTotal){
            next.setText("Next");
        }
        else{
            next.setText("Finish");
        }
    }
    private void finishQuiz() {
        //since we want to send our result back to our starting screen activity, we need an intent.
        Intent intentional = new Intent();
        intentional.putExtra(EXTRA_SCORE,score); // just like always
        setResult(RESULT_OK,intentional); // there are int result codes that can be used, like RESULT_OK(-1) etc
        finish();

        //before we included shared preferences, this method only had one line;
        //finish();
    }
}
