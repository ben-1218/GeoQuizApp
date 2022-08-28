package com.bignerdranch.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
private static final String TAG = "CheatActivity";
private static final String KEY_RESULT = "RESULT";
private static final String EXTRA_ANSWER_IS_TRUE= "com.bignerdranch.android.geoquiz.answer_is_true";
private boolean mAnswerIsTrue;
private TextView mAnswerTextView;
private Button mShowAnswerButton;
private static final String EXTRA_ANSWER_SHOWN="com.bignerdranch.android.geoquiz.answer_shown";

//this method will be use in quiz activity cheat button listener
public static Intent newIntent(Context packageContext, boolean answerIsTrue){
    Intent intent = new Intent(packageContext, CheatActivity.class);
    intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);

    return intent;
}
//this method will help to decode the result intent
public static boolean wasAnswerShown(Intent result){
    return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_cheat);
        //to retrieve the value from extra
        mAnswerIsTrue=getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswerButton=(Button)findViewById(R.id.show_answer_button);
        //when user click the show answer button, then it will show the answer
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }
                else{mAnswerTextView.setText(R.string.false_button);}
                setAnswerShownResult(true);



            }
        });
    }
    //this method will tell the quizactivity if user clicks the show answer button
    private void setAnswerShownResult(boolean isAnswerShown){
    Intent data = new Intent();
    data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
    setResult(RESULT_OK, data);
    }
//to save the result if user clicks show answer button
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
    super.onSaveInstanceState(savedInstanceState);
    savedInstanceState.putString(KEY_RESULT, EXTRA_ANSWER_SHOWN);

    }

}
