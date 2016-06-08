package com.erichorvat.rvgnet.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.erichorvat.rvgnet.interfaces.IAdapterViewInflater;
import com.erichorvat.rvgnet.model.Question;
import com.erichorvat.rvgnet.viewgroups.QuestionItemView;
import com.erichorvat.rvgnet.viewgroups.QuestionItemView_;

import org.androidannotations.annotations.EBean;

/**
 * Created by erichorvat on 2/7/15.
 */
@EBean
public class QuestionsInflater implements IAdapterViewInflater<Question> {


    Context c;

    public QuestionsInflater(Context c){
        this.c = c;
    }

    @Override
    public View inflate(BaseInflaterAdapter<Question> adapter, int position, View convertView, ViewGroup parent) {
        QuestionItemView questionItemView;
        if (convertView == null) {
            questionItemView = QuestionItemView_.build(c);
        } else {
            questionItemView = (QuestionItemView) convertView;
        }

        questionItemView.bind(adapter.getTItem(position));

        return questionItemView;
    }
}
