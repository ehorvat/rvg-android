package com.erichorvat.rvgnet.model;

/**
 * Created by erichorvat on 1/27/15.
 */
public class Platform {

    String name;

    boolean isHighlighted;

    public Platform(String name, boolean isHighlighted){
        this.name=name;
        this.isHighlighted=isHighlighted;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
