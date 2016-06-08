package com.erichorvat.rvgnet.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by erichorvat on 2/7/15.
 */
public class Question implements Serializable{

    int qid;

    public String title, body, platform, username;

    public String tstamp;

    public ArrayList<Answer> answers;

    public Question(){

    }

    public Question(int qid, String title, String body, String platform, String username, String tstamp){
        this.qid=qid;
        this.title = title;
        this.body = body;
        this.platform = platform;
        this.username = username;
        this.tstamp = tstamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTstamp() {
        return tstamp;
    }

    public void setTstamp(String tstamp) {
        this.tstamp = tstamp;
    }

    public void setAnswers(ArrayList<Answer> answers){
        this.answers=answers;
    }

    public ArrayList<Answer> getAnswers(){
        return answers;
    }

    public int getId() {
        return qid;
    }

    public void setId(int qid) {
        this.qid = qid;
    }
}
