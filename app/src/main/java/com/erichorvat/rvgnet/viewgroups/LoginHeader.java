package com.erichorvat.rvgnet.viewgroups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.activities.HomeActivity;
import com.erichorvat.rvgnet.activities.RegisterActivity_;
import com.erichorvat.rvgnet.model.Answer;
import com.erichorvat.rvgnet.model.Credentials;
import com.erichorvat.rvgnet.model.Question;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * Created by erichorvat on 2/4/15.
 */
@EViewGroup(R.layout.login_header)
public class LoginHeader extends RelativeLayout {

    Activity a;

    @ViewById
    protected TextView tvSignUp;

    @ViewById
    protected TextView tvSignIn;

    public LoginHeader(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setActivity(Activity a){
        this.a = a;
    }

    @Click(R.id.tvSignUp)
    void tvSignUp(){
        Intent i = new Intent(getContext(), RegisterActivity_.class);
        getContext().startActivity(i);
    }

    @Click(R.id.tvSignIn)
    void tvSignIn(){

        EditText etUsername = (EditText) a.findViewById(R.id.etUsername);

        EditText etPassword = (EditText) a.findViewById(R.id.etPass);

        authUser(new Credentials(etUsername.getText().toString(), etPassword.getText().toString()));
    }

    @Background
    void authUser(Credentials credentials){

        // The connection URL
       String url = "http://192.168.1.91:80/rvg-webservice/login.php";

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
        String response = restTemplate.postForObject(url, credentials, String.class);

        try {
            JSONObject r = new JSONObject(response);

            int success = r.getInt("success");

            if(success != 0){


                    DB snappydb = DBFactory.open(a); //create or open an existing databse using the default name

                    snappydb.put("username", r.getString("username"));

                JSONArray jQuestions = r.getJSONArray("posts");
                Question questions [] = new Question[jQuestions.length()];

                for(int i = 0; i<jQuestions.length(); i++){
                    JSONObject obj = jQuestions.getJSONObject(i);
                    Question question = new Question(obj.getInt("qid"),obj.getString("title"), obj.getString("body"),
                            obj.getString("platform"), obj.getString("username"),
                            obj.getString("tstamp"));

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


                Intent i = new Intent(a, HomeActivity.class);
                a.startActivity(i);

            }else{
                showFeedback(r.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        Log.v("response", response);

    }

    @UiThread
    void showFeedback(String message){
        TextView tvFeedback = (TextView) a.findViewById(R.id.tvFeedback);
        tvFeedback.setText(getContext().getResources().getString(R.string.icon_x) +  " " + message);
    }

}
