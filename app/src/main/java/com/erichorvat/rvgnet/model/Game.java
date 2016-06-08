package com.erichorvat.rvgnet.model;

import java.io.Serializable;

/**
 * Created by erichorvat on 1/29/15.
 */
public class Game implements Serializable{

    String title;

    String platform;

    String imgURL;

    String picture;

    double costAmount, tradeAmount;

    int bend_qty, newport_qty, brookings_qty, tillamook_qty;

    public Game(String title, String platform, double costAmount, double tradeAmount,
                int bend_qty, int newport_qty, int brookings_qty, int tillamook_qty, String imgURL, String picture){
        this.title=title;
        this.platform=platform;
        this.costAmount=costAmount;
        this.tradeAmount=tradeAmount;
        this.bend_qty=bend_qty;
        this.newport_qty=newport_qty;
        this.brookings_qty=brookings_qty;
        this.tillamook_qty=tillamook_qty;
        this.imgURL=imgURL;
        this.picture=picture;
    }
    public Game(String title, String platform, double costAmount, double tradeAmount){
        this.title=title;
        this.platform=platform;
        this.costAmount=costAmount;
        this.tradeAmount=tradeAmount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public double getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(double costAmount) {
        this.costAmount = costAmount;
    }

    public double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }


    public int getBend_qty() {
        return bend_qty;
    }

    public void setBend_qty(int bend_qty) {
        this.bend_qty = bend_qty;
    }

    public int getNewport_qty() {
        return newport_qty;
    }

    public void setNewport_qty(int newport_qty) {
        this.newport_qty = newport_qty;
    }

    public int getBrookings_qty() {
        return brookings_qty;
    }

    public void setBrookings_qty(int brookings_qty) {
        this.brookings_qty = brookings_qty;
    }

    public int getTillamook_qty() {
        return tillamook_qty;
    }

    public void setTillamook_qty(int tillamook_qty) {
        this.tillamook_qty = tillamook_qty;
    }

    @Override
    public String toString() {
        return "Title: " + title + " Platform: " + platform + " Cost: " + costAmount + " Trade: " + tradeAmount;
    }
}
