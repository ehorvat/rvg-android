package com.erichorvat.rvgnet.viewgroups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.activities.ForumActivity_;
import com.erichorvat.rvgnet.util.QuestionViewHolder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by erichorvat on 2/9/15.
 */
@EViewGroup(R.layout.ask_question_header)
public class AskQuestionHeader extends RelativeLayout {

    @ViewById
    TextView tvBackToForum;

    Activity a;

    QuestionViewHolder holder;

    int position;

    Typeface awesome;

    @AfterViews
    void init(){
        tvBackToForum.setTypeface(awesome);
    }

    public AskQuestionHeader(Context context) {
        super(context);
        awesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
    }

    public void setActivity(Activity a, QuestionViewHolder holder, int position){
        this.a = a;
        this.holder = holder;
        this.position = position;
    }

    @Click(R.id.tvBackToForum)
    void onBack(){
        Intent i = new Intent(a, ForumActivity_.class);
        a.startActivity(i);
        a.overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
        a.finish();
    }

}
