package com.erichorvat.rvgnet.viewgroups;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erichorvat.rvgnet.R;
import com.erichorvat.rvgnet.model.Answer;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by erichorvat on 2/16/15.
 */
@EViewGroup(R.layout.answer_row)
public class AnswerItemView  extends RelativeLayout {

    @ViewById
    TextView tvAnswer;

    @ViewById
    TextView tvTstamp;

    public AnswerItemView(Context context) {
        super(context);
    }

    public void bind(Answer answer) {
        tvAnswer.setText(answer.getAnswer());
        tvTstamp.setText(answer.gettStamp());
    }

    private Date differentFormat(Answer answer){
        DateFormat readFormat = new SimpleDateFormat( "yyyy-mm-dd hh:mm:ss");

        DateFormat writeFormat = new SimpleDateFormat( "EEEE, MMM d yyyy h:mm aa");
        Date date = null;
        try {
            date = readFormat.parse( answer.gettStamp() );
        } catch ( ParseException e ) {
            e.printStackTrace();
        }
        return date;
    }

}
