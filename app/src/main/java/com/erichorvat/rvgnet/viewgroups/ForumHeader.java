package com.erichorvat.rvgnet.viewgroups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.activities.AskQuestionActivity_;
import com.erichorvat.rvgnet.activities.HomeActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by erichorvat on 2/7/15.
 */
@EViewGroup(R.layout.forum_header)
public class ForumHeader extends RelativeLayout{

    Activity a;

    @ViewById
    TextView tvNewQuestion;

    @ViewById
    TextView tvBackToHome;

    Typeface awesome, lato;

    @AfterViews
    void init(){


       tvNewQuestion.setTypeface(awesome);
       tvBackToHome.setTypeface(awesome);

    }

    public ForumHeader(Context context) {
        super(context);

        awesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
        lato = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf");

    }

    public void setActivity(Activity a){
        this.a = a;
    }

    @Click(R.id.tvNewQuestion)
    void newQuestion(){
        Intent i = new Intent(a, AskQuestionActivity_.class);
        a.startActivity(i);
        a.finish();
        a.overridePendingTransition(R.anim.right_slide_in,
                R.anim.left_slide_out);
    }
    /*@Click(R.id.tvLogin)
    void login(){
        Intent i = new Intent(a, LoginActivity_.class);
        a.startActivity(i);
    }*/

    @Click(R.id.tvBackToHome)
    void backToHome(){
        Intent i = new Intent(a, HomeActivity_.class);
        a.startActivity(i);
        a.overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
        a.finish();

    }


}
