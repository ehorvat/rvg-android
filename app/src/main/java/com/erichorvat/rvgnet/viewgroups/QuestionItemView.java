package com.erichorvat.rvgnet.viewgroups;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.model.Question;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by erichorvat on 2/9/15.
 */
@EViewGroup(R.layout.question_row)
public class QuestionItemView extends RelativeLayout{

    Context c;

    @ViewById
    TextView tvTitle;

   /* @ViewById
    TextView tvBody;

    @ViewById
    TextView tvTstamp;
    */

    @ViewById
    TextView tvPlatform;


    @ViewById
    TextView tvCount;

    Typeface awesome, roboto;

    @AfterViews
    void init(){
        tvTitle.setTypeface(roboto);
        //tvBody.setTypeface(lato);
        //tvTstamp.setTypeface(lato);

        tvPlatform.setTypeface(awesome);
        tvCount.setTypeface(awesome);
    }
    public QuestionItemView(Context context) {
        super(context);
        this.c = context;

        awesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
        roboto = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    public void bind(Question question) {
        tvTitle.setText(question.getTitle());
        tvTitle.setTextColor(getResources().getColor(R.color.blue01));
        //tvBody.setText(question.getBody());
        tvPlatform.setText(getContext().getResources().getString(R.string.icon_gamepad) + " " + question.getPlatform());
        //tvTstamp.setText(question.getTstamp().toString());
        if(question.getAnswers()!=null){
            tvCount.setText(c.getResources().getString(R.string.icon_comments) + " " + question.getAnswers().size());
        }else{
            tvCount.setText(c.getResources().getString(R.string.icon_comments) + " " + 0);

        }
    }
}
