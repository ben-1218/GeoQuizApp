package com.bignerdranch.android.geoquiz;

public class Question {
    //attributes
    private int mTextResId;
    private boolean mAnswerTrue;
    //constructor
    public Question(int textResId, boolean answerTrue){
        this.mTextResId= textResId;
        this.mAnswerTrue=answerTrue;
    }
    //getter and setter

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
