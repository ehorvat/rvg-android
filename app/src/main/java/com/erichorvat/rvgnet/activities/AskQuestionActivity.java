package com.erichorvat.rvgnet.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.model.Question;
import com.erichorvat.rvgnet.util.Constants;
import com.erichorvat.rvgnet.util.QuestionViewHolder;
import com.erichorvat.rvgnet.viewgroups.AskQuestionHeader;
import com.erichorvat.rvgnet.viewgroups.AskQuestionHeader_;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by erichorvat on 2/9/15.
 */
@EActivity(R.layout.activity_ask_question)
public class AskQuestionActivity extends SherlockActivity{

    @ViewById
    Spinner sPlatform;

    @ViewById
    EditText etTitle;

    @ViewById
    EditText etBody;

    @ViewById
    Button bAsk;

    @ViewById
    TextView tvFeedback;

    AskQuestionHeader header;

    int mPosition;

    Typeface awesome, lato;

    @AfterViews
    void init(){

        mPosition = 0;

        QuestionViewHolder holder = new QuestionViewHolder(sPlatform, etTitle, etBody);


        awesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        lato = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");

        header = AskQuestionHeader_.build(this);


        //RelativeLayout header = (RelativeLayout) View.inflate(this, R.layout.login_header, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(header);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.platform_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sPlatform.setAdapter(adapter);

        sPlatform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        header.setActivity(AskQuestionActivity.this, holder, mPosition);

        bAsk.setTypeface(awesome);
        bAsk.setText(getResources().getString(R.string.icon_paper_pane) + " Ask!");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Click(R.id.bAsk)
    void onAsk(){

        hideSoftKeyboard();

        hideFeedback();

        ask();
    }

    @Background
    void ask(){
        if(etBody.getText().length() > 0 && etTitle.getText().length() > 0 && mPosition!=0) {
            String username = null;

            try {
                DB snappydb = DBFactory.open(getApplicationContext());
                //username = snappydb.get("username");

                String url = Constants.URL_ASK_QUESTION+"?platform="
                        + (String) sPlatform.getItemAtPosition(mPosition) + "&title="
                        + etTitle.getText().toString() + "&body="
                        + etBody.getText().toString() + "&username=anonymous";

                // Create a new RestTemplate instance
                RestTemplate restTemplate = new RestTemplate();

                // Add the String message converter
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                // Make the HTTP GET request, marshaling the response to a String
                String result = restTemplate.getForObject(url, String.class, "Android");

                Log.v("result", result);

                JSONObject obj = new JSONObject(result);

                int success = obj.getInt("success");

                if (success != 0) {

                    /*String tstamp = new Timestamp(System.currentTimeMillis()).toString();
                    Log.v("tstamp", tstamp);
                    Question question = new Question(obj.getInt("qid"), etTitle.getText().toString(), etBody.getText().toString(), (String) sPlatform.getItemAtPosition(mPosition),
                            username, tstamp);
                    Log.v("after assign", question.getTstamp());

                    Question questions[] = null;

                    if(snappydb.exists("questions")){

                        questions = snappydb.getObjectArray("questions", Question.class);

                        questions = appendQuestion(questions, question);

                        snappydb.del("questions");

                    }else{
                        questions = new Question[1];
                        questions[0] = question;
                    }

                    snappydb.put("questions", questions);*/

                    Intent i = new Intent(AskQuestionActivity.this, ForumActivity_.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);


                }

                //snappydb.close();
            } catch (SnappydbException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            showFeedback();
        }
    }

    @UiThread
    void showFeedback(){
        tvFeedback.setVisibility(View.VISIBLE);
        tvFeedback.setText("All Fields Required");
    }

    @UiThread
    void hideFeedback(){
        tvFeedback.setVisibility(View.GONE);
    }

    private Question[] appendQuestion(Question[] questions, Question question) {

        ArrayList<Question> temp = new ArrayList<Question>(Arrays.asList(questions));
        temp.add(question);
        return temp.toArray(new Question[temp.size()]);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AskQuestionActivity.this, ForumActivity_.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }


    private void hideSoftKeyboard(){
        if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText){
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etBody.getWindowToken(), 0);
        }
    }


}
