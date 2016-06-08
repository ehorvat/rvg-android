package com.erichorvat.rvgnet.model;

import java.util.ArrayList;

/**
 * Created by erichorvat on 1/26/15.
 */
public class Store {

    String store_name;

    String address;

    String phone_number;


    String sqlName;

    boolean isHighlighted;

    ArrayList<Game> games;

    public Store(String store_name, String address, String phone_number, boolean isHighlighted, String sqlName){
        this.store_name=store_name;
        this.address=address;
        this.phone_number=phone_number;
        this.isHighlighted=isHighlighted;
        this.sqlName=sqlName;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }
}
