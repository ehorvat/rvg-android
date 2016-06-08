package com.erichorvat.rvgnet.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.adapter.AnswersInflater;
import com.erichorvat.rvgnet.adapter.BaseInflaterAdapter;
import com.erichorvat.rvgnet.model.Answer;
import com.erichorvat.rvgnet.model.Question;
import com.erichorvat.rvgnet.util.Constants;
import com.erichorvat.rvgnet.viewgroups.AnswerQuestionHeader;
import com.erichorvat.rvgnet.viewgroups.AnswerQuestionHeader_;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * Created by erichorvat on 2/16/15.
 */
@EActivity(R.layout.answer_layout)
public class AnswerActivity extends SherlockActivity {

    @Extra("questionExtra")
    Question question;

    @Extra("pos")
    int pos;

    @ViewById
    TextView tvTitle;

    @ViewById
    TextView tvBody;

    @ViewById
    ListView lvAnswers;

    @ViewById
    EditText etAnswer;

    @ViewById
    TextView tvTstamp;

    @ViewById
    Button bPost;

    private ArrayList<Answer> answers;

    private BaseInflaterAdapter<Answer> answers_adapter;

    private String username;

    DB snappydb;

    Typeface awesome, lato, roboto;

    AnswerQuestionHeader header;

    @AfterViews
    void init(){

        header = AnswerQuestionHeader_.build(this);

        header.setActivity(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(header);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        awesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        lato = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");
        roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");


        try {
            snappydb = DBFactory.open(this);

            //username = snappydb.get("username");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        answers_adapter = new BaseInflaterAdapter<Answer>(new AnswersInflater(getApplicationContext()), null);

        if(question.getAnswers()!=null){

            answers = question.getAnswers();

            answers_adapter.addItems(answers, false);
        }

        lvAnswers.setAdapter(answers_adapter);

        tvTitle.setText(question.getTitle());
        tvTitle.setTypeface(roboto);

        tvBody.setText(question.getBody());
        tvBody.setTypeface(lato);

        tvTstamp.setText(question.getTstamp());
        tvTstamp.setTypeface(lato);

        bPost.setTypeface(awesome);
        bPost.setText(getResources().getString(R.string.icon_paper_pane) + " " + bPost.getText().toString());

    }

    @Click(R.id.bPost)
    @Background
    void onPost(){

        clearAnswerField();

        hideSoftKeyboard();

        String answer = etAnswer.getText().toString();

        if(answer.length() > 0){
            try{
                postAnswer(answer);

            }catch(JSONException e){
                e.printStackTrace();
            } catch (SnappydbException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void postAnswer(String answer) throws JSONException, SnappydbException {

        snappydb = DBFactory.open(getApplicationContext());

        String url = Constants.URL_ANSWER+"?answer="
                +answer+"&username="
                +username+"&qid="
                +question.getId();

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Add the String message converter
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP GET request, marshaling the response to a String
        String result = restTemplate.getForObject(url, String.class, "Android");

        JSONObject obj = new JSONObject(result);

        int success = obj.getInt("success");

        if(success != 0){
            Answer newAnswer = new Answer(answer, username, obj.getString("tstamp"));

            Question questions[] =  snappydb.getObjectArray("questions", Question.class);

            Question question = questions[pos];

            if(question.getAnswers()==null){
                ArrayList<Answer> answers = new ArrayList<Answer>();
                answers.add(newAnswer);
                question.setAnswers(answers);
            }else{
                questions[pos].getAnswers().add(newAnswer);
            }


            snappydb.put("questions", questions);

            notifyListView(newAnswer);

        }

        snappydb.close();
    }

    @UiThread
    void notifyListView(Answer newAnswer){
        answers_adapter.addItem(newAnswer, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void hideSoftKeyboard(){
        if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etAnswer.getWindowToken(), 0);
        }
    }

    @UiThread
    void clearAnswerField(){
        etAnswer.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AnswerActivity.this, ForumActivity_.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }
}
