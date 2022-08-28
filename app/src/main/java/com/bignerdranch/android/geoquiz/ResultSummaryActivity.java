package com.bignerdranch.android.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_summary);
        TextView questionAnswered = (TextView)findViewById(R.id.questionAnswered);
        TextView ScoreDisplay = (TextView)findViewById(R.id.ScoreDisplay);
        TextView cheatAttemptDisplay = (TextView)findViewById(R.id.cheatAttemptDisplay);
        //retrieve the values from quizactivity
        int totalScore = getIntent().getIntExtra("COUNT_CORRECT_ANS", 0)*10;
        int cheatAttempt = getIntent().getIntExtra("COUNT_CHEAT_ATTEMPT", 0);
        int totalquestionAnswered= getIntent().getIntExtra("CURRENT_QUESTION", 0);
       //update the textview with the latest values user made
        ScoreDisplay.setText("total score:"+ totalScore+ " "+ "out of 60!");
        cheatAttemptDisplay.setText("total cheat attempt:" + " "+ cheatAttempt+"/3");
        questionAnswered.setText("question answered:"+ totalquestionAnswered+"/6");

    }
}
