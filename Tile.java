package com.example.chriswu.triple_tac_toe;


/**
 * Groups the coordinate point of a TextView with it's Small_Grid controller
 */

public class Tile{
    private Tuple<Integer, Integer> point;
    private Small_Grid small_grid;

    Tile(Small_Grid small_grid,int row, int col)
    {
        this.small_grid = small_grid;
        point = new Tuple<>(row,col);
    }

    public Tuple<Integer,Integer> getPoint() {
        return point;
    }

    public Small_Grid getSmall_grid(){
        return small_grid;
    }
}
