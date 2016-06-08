package com.erichorvat.rvgnet.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.viewgroups.LoginHeader;
import com.erichorvat.rvgnet.viewgroups.LoginHeader_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

/**
 * Created by erichorvat on 2/4/15.
 */
@OptionsMenu(R.menu.menu_home)
@EActivity(R.layout.login_activity)
public class LoginActivity extends SherlockActivity implements Switch.OnCheckedChangeListener{

    boolean remember;

    private static final String PREFS = "prefs";

    private static final String PREF_REMEMBER = "remember";

    private static final String PREF_USERNAME = "username";

    private static final String PREF_PASSWORD = "password";

    //Main Activity Views
    @ViewById
    EditText etUsername;

    @ViewById
    EditText etPass;

    @ViewById
    TextView tvPassRecover;

    @ViewById
    TextView tvFeedback;

    @ViewById
    Switch sRememberMe;

    @ViewById
    TextView tvSignUp;

    LoginHeader header;

    @AfterViews
    void initViews(){

        header = LoginHeader_.build(this, null);
        header.setActivity(LoginActivity.this);

        //RelativeLayout header = (RelativeLayout) View.inflate(this, R.layout.login_header, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(header);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface avenir = Typeface.createFromAsset(getAssets(),"fonts/Lato-Light.ttf");
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        remember = isChecked;

        if(!remember){
            getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                    .remove("PREF_EMAIL")
                    .remove("PREF_PASSWORD").putBoolean(PREF_REMEMBER, false).commit();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
}
