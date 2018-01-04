package com.example.chriswu.triple_tac_toe;

/**
 * Created by Chris Wu on 1/4/2018.
 */

public class Quadruple <W,X,Y,Z> {
    public final W smallRow;
    public final X smallCol;
    public final Y tileRow;
    public final Z tileCol;
    public Quadruple(W smallRow, X smallCol, Y tileRow, Z tileCol) {
        this.smallRow = smallRow;
        this.smallCol = smallCol;
        this.tileRow = tileRow;
        this.tileCol = tileCol;
    }
}
