package com.erichorvat.rvgnet.interfaces;

import com.erichorvat.rvgnet.model.Game;

import java.util.ArrayList;

/**
 * Created by erichorvat on 2/1/15.
 */
public interface OnSearch {

    void onNoMatch(ArrayList<Game> values, int count);

}


