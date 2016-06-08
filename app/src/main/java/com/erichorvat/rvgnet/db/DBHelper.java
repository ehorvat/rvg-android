package com.erichorvat.rvgnet.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.erichorvat.rvgnet.model.Game;

import java.util.HashMap;

/**
 * Created by erichorvat on 1/29/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RVGDB";

    public static final String GAMES_TABLE_NAME = "games";

    public static final String GAMES_COLUMN_ID = "gid";
    public static final String GAMES_COLUMN_TITLE = "title";
    public static final String GAMES_COLUMN_PLATFORM = "platform";
    public static final String GAMES_COLUMN_PRICE = "price";
    public static final String GAMES_COLUMN_TRADE = "trade";
    public static final String GAMES_BEND = "nb_qty";
    public static final String GAMES_NEWPORT = "np_qty";
    public static final String GAMES_BROOKINGS = "bk_qty";
    public static final String GAMES_TILLAMOOK = "tm_qty";

    SQLiteDatabase db;

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + GAMES_TABLE_NAME);


        db.execSQL(
                "create table " + GAMES_TABLE_NAME +
                        "(" + GAMES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                        + GAMES_COLUMN_TITLE + " text, "
                        + GAMES_COLUMN_PLATFORM + " text, "
                        + GAMES_COLUMN_PRICE + " real, "
                        + GAMES_COLUMN_TRADE + " real, "
                        + GAMES_BEND + " integer, "
                        + GAMES_NEWPORT + " integer, "
                        + GAMES_BROOKINGS + " integer, "
                        + GAMES_TILLAMOOK + " integer"
                        + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db=db;
        db.execSQL("DROP TABLE IF EXISTS " + GAMES_TABLE_NAME);
        onCreate(db);
    }

    public void insertGame(Game game){

        Log.v("inserting", "inserting");

        db = this.getWritableDatabase();
        String sql = "INSERT INTO "+ GAMES_TABLE_NAME +" VALUES (?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
            statement.clearBindings();
            statement.bindString(2, game.getTitle());
            statement.bindString(3, game.getPlatform());
            statement.bindDouble(4, game.getCostAmount());
            statement.bindDouble(5, game.getTradeAmount());
            statement.bindDouble(6, game.getBend_qty());
            statement.bindDouble(7, game.getNewport_qty());
            statement.bindDouble(8, game.getBrookings_qty());
            statement.bindDouble(9, game.getTillamook_qty());
            statement.execute();

        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public Cursor getGames(String store, String platform){
        db = this.getReadableDatabase();

        db.beginTransaction();

        Cursor res = null;


        Log.v("stuff", store + " " + platform);

        if(!platform.equals("All")){
            res =  db.rawQuery( "select * from games where " + store + " >=1 AND platform='" + platform + "';" ,null );
        }else{
            res =  db.rawQuery( "select * from games where " + store + " >=1;" ,null );
        }

        db.endTransaction();

        return res;
    }

    public int getTableCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM games";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0) {
            return 1;
        }else{
            return 0;
        }

    }

    public SQLiteDatabase getDB(){
        return db;
    }

}
