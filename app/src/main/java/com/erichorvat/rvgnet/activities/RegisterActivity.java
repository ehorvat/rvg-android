package com.erichorvat.rvgnet.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.model.Credentials;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by erichorvat on 2/4/15.
 */
@EActivity(R.layout.register_activity)
public class RegisterActivity extends Activity {

    @ViewById
    EditText etUsername;

    @ViewById
    EditText etConfPass;

    @ViewById
    EditText etPass;

    @ViewById
    Button bRegister;

    @ViewById
    TextView tvFeedback;

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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Click(R.id.bRegister)
    void onRegister(){


        if (etUsername.length() <= 0 || etPass.length() <= 0 || etConfPass.length() <= 0) {
               showFeedBack("All Fields Required");
        }else if(!etPass.getText().toString().equals(etConfPass.getText().toString())) {
                showFeedBack("Passwords don't match");
        }else {
               //Spring rest android

            register();

        }
    }

    @UiThread
    void showFeedBack(String message){
        tvFeedback.setVisibility(View.VISIBLE);
        tvFeedback.setTextColor(Color.parseColor("#EE0000"));
        tvFeedback.setText(getResources().getString(R.string.icon_x) + " " + message);
    }

    @Background
    void register(){

        Credentials credentials = new Credentials(etUsername.getText().toString(), etPass.getText().toString());

        // The connection URL
        String url = "http://192.168.1.91:80/rvg-webservice/register.php";

        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Add the Jackson and String message converters
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // Make the HTTP POST request, marshaling the request to JSON, and the response to a String
        String response = restTemplate.postForObject(url, credentials, String.class);

        Log.v("response", response);

    }


}
