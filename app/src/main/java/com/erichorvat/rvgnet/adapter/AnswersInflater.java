package com.erichorvat.rvgnet.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.erichorvat.rvgnet.interfaces.IAdapterViewInflater;
import com.erichorvat.rvgnet.model.Answer;
import com.erichorvat.rvgnet.viewgroups.AnswerItemView;
import com.erichorvat.rvgnet.viewgroups.AnswerItemView_;

/**
 * Created by erichorvat on 2/16/15.
 */
public class AnswersInflater implements IAdapterViewInflater<Answer> {

    Context c;

    public AnswersInflater(Context c){
        this.c = c;
    }

    @Override
    public View inflate(BaseInflaterAdapter<Answer> adapter, int pos, View convertView, ViewGroup parent) {
        AnswerItemView answerItemView;
        if (convertView == null) {
            answerItemView = AnswerItemView_.build(c);
        } else {
            answerItemView = (AnswerItemView) convertView;
        }

        answerItemView.bind(adapter.getTItem(pos));

        return answerItemView;
    }
}
