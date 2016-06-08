package com.erichorvat.rvgnet.db;

import android.database.Cursor;

import com.erichorvat.rvgnet.model.Game;
import com.erichorvat.rvgnet.model.Platform;
import com.erichorvat.rvgnet.model.Store;

import java.util.ArrayList;

/**
 * Created by erichorvat on 1/29/15.
 */
public class DBAdapter{

    DBHelper helper;

    public DBAdapter(DBHelper helper){
        this.helper=helper;
    }

    public DBHelper getHelper(){
        return this.helper;
    }

    public ArrayList<Game> getGames(Store store, Platform platform) {

        Cursor res = helper.getGames(store.getSqlName(), platform.getName());

        ArrayList<Game> games = new ArrayList<Game>();

        res.moveToFirst();
        while (res.moveToNext()) {
            Game game = new Game(res.getString(1), res.getString(2), res.getFloat(3), res.getFloat(4));
            games.add(game);
        }

        return games;
    }

    public boolean tableExists(){

        boolean exists = false;

        if(helper.getTableCount()>0){
            exists = true;
        }

        return exists;
    }


    public void insertGame(Game game){
        helper.insertGame(game);
    }

}
