package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.widget.CompoundButton;


public class QuizActivity extends AppCompatActivity {
    private static final String TAG="QuizActivity";
    private static final String KEY_INDEX="index";
    private static final String KEY_ISCHEATER="is_cheater";
    private static final String KEY_CHEATTOKEN="cheat_token";
    private static final String KEY_COUNT_CORRECT="count_correct";
    private static final String KEY_SUMOFCOUNTANS="sum_answer";
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Button mResetButton;
    private Button mCheatButton;
    private Button mResultSummaryButton;
    private TextView mQuestionTextView;
    private int countCorrect=0;
    private int sumofcountans=0;
    private int mCheattoken=0;
    private static final int REQUEST_CODE_CHEAT=0;
    private boolean mIsCheater;
    private Switch themeSwitch; //for dark mode switch
    public static final String MyPREFERENCES="nightModePres";
    public static final String  KEY_ISNIGHTMODE="isNightMode";
    SharedPreferences sharedpreferences;






    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),

    };
    private int mCurrentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
        if(savedInstanceState != null){
            //when user rotate screen, the value below will be saved.
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            countCorrect = savedInstanceState.getInt(KEY_COUNT_CORRECT, 0);
            sumofcountans= savedInstanceState.getInt(KEY_SUMOFCOUNTANS, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_ISCHEATER, false);
            mCheattoken = savedInstanceState.getInt(KEY_CHEATTOKEN, 0);

        }
        //updating textview
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        //sharedpreferences for night mode
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       //themeSwitch
        themeSwitch = (Switch)findViewById(R.id.theme_switch);
        //this method will check if the night mode is on or not
        checkNightModeActivated();
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveNightModeStat(true);
                    recreate();
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveNightModeStat(false);
                    recreate();
                }
            }
        });

        //t and f button
        mTrueButton=(Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sumofountans is to track how many questions user answered, if user click true or false, sumofcountans will +1
                // and to prevent multiple answers, when user chose the answer, t and f button will be disable
                sumofcountans+=1;
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
                checkAnswer(true);
                if(sumofcountans==6){ //if user provided all the answers, then a toast will be printed.
                    Toast gradetoast = Toast.makeText(getApplicationContext(), "your grade is:"+ countCorrect*10+"/60", Toast.LENGTH_LONG);
                    gradetoast.setGravity(Gravity.CENTER,0,0);
                    gradetoast.show();
                    mNextButton.setEnabled(false); //if user answered all question then next button will be disable to prevent repeated questions.
                }


            }

        });
        mFalseButton=(Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //basically the same code with the true button, i wanna put it in a method and then call the method in t and f button but
                // i don't know why cannot, so i better recode it out again. sorry
                sumofcountans+=1;
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
                checkAnswer(false);
                if(sumofcountans==6){
                    Toast gradetoast = Toast.makeText(getApplicationContext(), "your grade is:"+ countCorrect*10+"/60", Toast.LENGTH_LONG);
                    gradetoast.setGravity(Gravity.CENTER,0,0);
                    gradetoast.show();
                    mNextButton.setEnabled(false);
                }}



        });
        mNextButton=(ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               //when user clicks next, then the currentIndex will be +1, and the text view will change with the updateQuestion method called
                mCurrentIndex = (mCurrentIndex+1)% mQuestionBank.length;
                mIsCheater = false;  //if user did not click show answer on the cheatActivity, then mIsCheater will be false
                mTrueButton.setEnabled(true); //if user click next, then true and false button will be enabled.
                mFalseButton.setEnabled(true);
                updateQuestion();




            }
        });
        //previous button code at here
        mPrevButton=(ImageButton)findViewById(R.id.prev_button);
        //when user clicks previous button, the currentindex will be deduct 1 and back to the previous question with the updateQuestion method
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex=(mCurrentIndex-1)%mQuestionBank.length;
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
                updateQuestion();



            }
        });
        updateQuestion();
        mResetButton=(Button)findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //once user clicks reset, everything will be back to 0. and the button below set to enable
                mIsCheater=false;
                sumofcountans=0;
                countCorrect=0;
                mCurrentIndex=0;
                mCheattoken=0;
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
                mCheatButton.setEnabled(true);
                mNextButton.setEnabled(true);
                updateQuestion();
            }
        });
        mCheatButton=(Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cheat answers
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                //so that it can launch cheatActivity with passing the extra to another activity
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);

                          //mCheattoken += 1;
                          /*if (mCheattoken < 4) {
                              Toast.makeText(getApplicationContext(), "you have used" + "" + mCheattoken + "/3", Toast.LENGTH_LONG).show();
                              if (mCheattoken == 3) {
                                  mCheatButton.setEnabled(false);
                              }
                          } else {
                              mCheatButton.setEnabled(false);
                          }*/






                /*Cheatclicked= true;
                int CountCheatClicked=0;
                if(Cheatclicked= true){
                    CountCheatClicked ++;
                }
                if(CountCheatClicked <= 3){
                    //show answer logic will be coded here
                }
                else{mCheatButton.setEnabled(false);}*/
            }
        });
        mResultSummaryButton=(Button)findViewById(R.id.resultSummary_button);
        mResultSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //pass 3 value to result summary page
                Intent intentResult = new Intent(getApplicationContext(), ResultSummaryActivity.class);
                intentResult.putExtra("COUNT_CORRECT_ANS", countCorrect);
                intentResult.putExtra("COUNT_CHEAT_ATTEMPT", mCheattoken);
                intentResult.putExtra("CURRENT_QUESTION", sumofcountans);
                startActivity(intentResult);


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ //to handle the result
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_CODE_CHEAT){
            if(data==null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            mCheattoken+=1; //if ppl clicks show answer button, then cheat token will +1. when cheattoken reached 3, then the cheat button will disable
            if (mCheattoken < 4) { //when user clicks showAnswer button on cheat activity, the toast below will be shown
                Toast.makeText(getApplicationContext(), "The cheat token/s you have used:" + "" + mCheattoken + "/3", Toast.LENGTH_LONG).show();
                if (mCheattoken == 3) {
                    mCheatButton.setEnabled(false);
                }
            } else {
                mCheatButton.setEnabled(false);
            }
        }
    }
    //for saving the night mode
    private void saveNightModeStat(boolean nightMode){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(KEY_ISNIGHTMODE, nightMode);
        editor.apply();
    }
    //for night mode
    public void checkNightModeActivated(){
        if(sharedpreferences.getBoolean(KEY_ISNIGHTMODE, false)){
            themeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            themeSwitch.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    //check the activity life cycle when user clicked onstart(), onResume(), onPause(), onStop() or onDestroy()
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"onStart() called");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "OnResume() called");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() called");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //save these variables while user rotate screen
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_ISCHEATER, mIsCheater);
        savedInstanceState.putInt(KEY_CHEATTOKEN, mCheattoken);
        savedInstanceState.putInt(KEY_COUNT_CORRECT, countCorrect);
        savedInstanceState.putInt(KEY_SUMOFCOUNTANS, sumofcountans);

    }
    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop() called");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }
    private void updateQuestion(){
        int question= mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);




    }
    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId=0;
        if(mIsCheater){
            messageResId=R.string.judgement_toast;
        }else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                countCorrect+=1;
                //countCorrect is to count how many answers are correct provided by the user
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast toast =Toast.makeText(QuizActivity.this,
                messageResId,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }








}
