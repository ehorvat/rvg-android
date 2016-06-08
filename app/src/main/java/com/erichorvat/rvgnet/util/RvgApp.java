package com.erichorvat.rvgnet.util;

import android.app.Application;

import com.erichorvat.rvgnet.model.Credentials;
import com.erichorvat.rvgnet.model.Game;
import com.erichorvat.rvgnet.model.Question;

import java.util.ArrayList;

/**
 * Created by erichorvat on 1/30/15.
 */
public class RvgApp extends Application {

    Credentials credentials;

    ArrayList<Question> questions;

    ArrayList<Game> games;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }
}
