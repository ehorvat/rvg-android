package com.erichorvat.rvgnet.model;

import java.io.Serializable;

/**
 * Created by erichorvat on 2/16/15.
 */
public class Answer implements Serializable{

    String answer, username, tStamp;

    int aid, qid;

    public Answer(){

    }

    public Answer(int aid, String answer, int qid, String username, String tStamp){
        this.aid = aid;
        this.answer = answer;
        this.qid = qid;
        this.username = username;
        this.tStamp = tStamp;
    }

    public Answer(String answer, String username, String tStamp){
        this.answer = answer;
        this.username = username;
        this.tStamp = tStamp;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String gettStamp() {
        return tStamp;
    }

    public void settStamp(String tStamp) {
        this.tStamp = tStamp;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }
}


