package com.erichorvat.rvgnet.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.adapter.BaseInflaterAdapter;
import com.erichorvat.rvgnet.adapter.QuestionsInflater;
import com.erichorvat.rvgnet.model.Answer;
import com.erichorvat.rvgnet.model.Question;
import com.erichorvat.rvgnet.util.Constants;
import com.erichorvat.rvgnet.viewgroups.ForumHeader;
import com.erichorvat.rvgnet.viewgroups.ForumHeader_;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * Created by erichorvat on 2/7/15.
 */
@EActivity(R.layout.forum_activity)
public class ForumActivity extends SherlockActivity{


    @ViewById
    ListView lvQuestions;

    @ViewById
    TextView tvPages;

    @ViewById
    Button bPrev;

    @ViewById
    Button bNext;

    @ViewById
    TextView tvRecycleIcon;

    Animation a;

    int pageCount, increment;

    ArrayList<Question> mQuestions;

    BaseInflaterAdapter<Question> questions_adapter;

    private static int NUM_GAMES_PER_PAGE = 10;

    ForumHeader header;

    DB snappydb;

    Typeface awesome, lato;

    @AfterViews
    void init(){

       a = AnimationUtils.loadAnimation(ForumActivity.this, R.anim.custom_spinner);


        header = ForumHeader_.build(this);

        header.setActivity(this);

        awesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        lato = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");

        mQuestions = new ArrayList<Question>();

        ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(header);

        bNext.setTypeface(awesome);
        bPrev.setTypeface(awesome);
        bPrev.setEnabled(false);
        tvRecycleIcon.setTypeface(awesome);

        tvRecycleIcon.setAnimation(a);

        startAnimation();

        loadQuestions();

    }

    @Background
    void loadQuestions(){
        // The connection URL

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Add the String message converter
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP GET request, marshaling the response to a String
        String response = restTemplate.getForObject(Constants.URL_QUESTIONS, String.class, "Android");

        try {
            JSONObject r = new JSONObject(response);

            int success = r.getInt("success");

            if(success != 0){


                snappydb = DBFactory.open(ForumActivity.this); //create or open an existing databse using the default name

                //snappydb.put("username", r.getString("username"));


                JSONArray jQuestions = r.getJSONArray("posts");
                Question questions [] = new Question[jQuestions.length()];

                /*java.util.Calendar cal = java.util.Calendar.getInstance();
                String dateInString = new java.text.SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss").format(cal.getTime());
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd/MM/yyyy/hh:mm:ss");
                Date parsedDate = formatter.parse(dateInString);*/

                for(int i = 0; i<jQuestions.length(); i++){

                    JSONObject obj = jQuestions.getJSONObject(i);
                    Question question = new Question(obj.getInt("qid"),obj.getString("title"), obj.getString("body"),
                            obj.getString("platform"), obj.getString("username"),
                            obj.getString("tstamp"));

                    mQuestions.add(question);

                    if(obj.getJSONArray("answers").length() > 0){
                        ArrayList<Answer> answers = new ArrayList<Answer>();

                        JSONArray jsonAnswers = obj.getJSONArray("answers");

                        for(int j = 0; j<jsonAnswers.length(); j++){

                            JSONObject jsonAnswer = jsonAnswers.getJSONObject(j);

                            Answer answer = new Answer(jsonAnswer.getInt("aid"), jsonAnswer.getString("answer"),
                                    jsonAnswer.getInt("qid"), jsonAnswer.getString("username"), jsonAnswer.getString("tstamp"));

                            answers.add(answer);
                        }
                        question.setAnswers(answers);

                    }

                    questions[i] = question;

                }



                snappydb.put("questions", questions);


                snappydb.close();


                int val = mQuestions.size()%NUM_GAMES_PER_PAGE;
                val = val==0?0:1;
                pageCount = mQuestions.size()/NUM_GAMES_PER_PAGE+val;

                loadList(0);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            //Start questions activity
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }


    @ItemClick
    void lvQuestions(int position){

        int pos = position + (10*increment);

        Question question = mQuestions.get(pos);

        AnswerActivity_.IntentBuilder_ ib = AnswerActivity_.intent(this);
        ib.extra("pos", pos);
        ib.extra("questionExtra",mQuestions.get(pos));
        ib.start();
        finish();

        overridePendingTransition(R.anim.right_slide_in,
                R.anim.left_slide_out);
    }

    @Click(R.id.bPrev)
    void previous(){
        if(pageCount!=1){
            increment--;
            CheckEnable();
            loadList(increment);
        }
    }

    @Click(R.id.bNext)
    void next(){
        if(pageCount!=1){
            increment++;
            CheckEnable();
            loadList(increment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ForumActivity.this, HomeActivity_.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Background
    void loadList(int number){
        final ArrayList<Question> sort = new ArrayList<Question>();

        changePage(number);

                int start = number * NUM_GAMES_PER_PAGE;
                for (int i = start; i < (start) + NUM_GAMES_PER_PAGE; i++) {
                    if (i < mQuestions.size()) {
                        sort.add(mQuestions.get(i));
                    } else {
                        break;
                    }
                }


            questions_adapter = new BaseInflaterAdapter<Question>(new QuestionsInflater(getApplicationContext()), null);

            questions_adapter.clear(true);

            notifyAdapter(sort, true);
    }



    /**
     * Method for enabling and disabling Buttons
     */
    private void CheckEnable()
    {
        if(increment+1 == pageCount)
        {
            bNext.setEnabled(false);
            bNext.setClickable(false);
            bPrev.setEnabled(true);
            bPrev.setClickable(true);
        }
        else if(increment == 0)
        {
            bPrev.setEnabled(false);
            bNext.setEnabled(true);
            bNext.setClickable(true);
        }
        else
        {
            bPrev.setEnabled(true);
            bNext.setEnabled(true);
            bNext.setClickable(true);
        }
    }

    @UiThread
    void clearList(){
        mQuestions.clear();
        questions_adapter.clear(true);
    }

    @UiThread
    void startAnimation(){
        tvRecycleIcon.setVisibility(View.VISIBLE);
        tvRecycleIcon.startAnimation(a);
    }

    @UiThread
    void stopAnimation(){
        tvRecycleIcon.setVisibility(View.GONE);
        tvRecycleIcon.clearAnimation();
    }

    @UiThread
    void changePage(int number){
        tvPages.setText("Page " + (number + 1) + " of " + pageCount);
    }

    @UiThread
    void notifyAdapter(ArrayList<Question> questions, boolean notify){
        lvQuestions.setAdapter(questions_adapter);


        if (questions.size() != 0) {
            bNext.setEnabled(true);
        }

        questions_adapter.addItems(questions, true);

        stopAnimation();
    }

    @UiThread
    void changeTitleColorBlue(int position){
        TextView v = (TextView) lvQuestions.getChildAt(position);
        v.setTextColor(getResources().getColor(R.color.blue01));
    }

    @Background
    void loadFromLocal(){

        try {
            mQuestions.clear();

            snappydb = DBFactory.open(ForumActivity.this); //create or open an existing databse using the default name

            Question questions[] = snappydb.getObjectArray("questions", Question.class);

            for(Question question : questions){
                mQuestions.add(question);
            }


            int val = mQuestions.size()%NUM_GAMES_PER_PAGE;
            val = val==0?0:1;
            pageCount = mQuestions.size()/NUM_GAMES_PER_PAGE+val;

            loadList(0);

        } catch (SnappydbException e) {
            e.printStackTrace();
        }


    }

    boolean hasLocalQuestions(){

        boolean hasQuestions = false;

        try {
            snappydb = DBFactory.open(ForumActivity.this); //create or open an existing databse using the default name

            Question questions[] = snappydb.getObjectArray("questions", Question.class);

            if(questions.length > 0 ) {
                hasQuestions = true;
                mQuestions.clear();
                for(Question question : questions){
                    mQuestions.add(question);
                }
            }

        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        return hasQuestions;
    }

}
