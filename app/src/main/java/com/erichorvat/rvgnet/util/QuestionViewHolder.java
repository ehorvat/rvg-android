package com.erichorvat.rvgnet.util;

import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by erichorvat on 2/12/15.
 */
public class QuestionViewHolder {

    int qid;

    public Spinner sSpinner;

    public EditText title, body;

    public QuestionViewHolder(int qid, Spinner sSpinner, EditText title, EditText body){
        this.sSpinner=sSpinner;
        this.title=title;
        this.body=body;
        this.qid=qid;
    }

    public QuestionViewHolder(Spinner sSpinner, EditText title, EditText body){
        this.sSpinner=sSpinner;
        this.title=title;
        this.body=body;
    }

    public String getSpinnerVal(int position) {
        return (String) sSpinner.getItemAtPosition(position);
    }

    public String getTitleVal() {
        return title.getText().toString();
    }
    public String getBodyVal() {
        return body.getText().toString();
    }
}
