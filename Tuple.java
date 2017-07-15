package com.example.chriswu.triple_tac_toe;

/**
 * Created by Chris Wu on 7/10/2017.
 */

public class Tuple<X, Y> {
    public final X row;
    public final Y col;
    public Tuple(X row, Y col) {
        this.row = row;
        this.col = col;
    }
}
