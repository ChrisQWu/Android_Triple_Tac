package com.example.chriswu.triple_tac_toe;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

/**
 * TextView listener to notify the Small_Grid when a move is made.
 */

public class CustomClicker implements View.OnClickListener {
    private Tile tile;

    CustomClicker(Tile tile)
    {
        this.tile = tile;
    }

    @Override
    /**
     * Only makes a move if the color is BLUE
     */
    public void onClick(View view) {
        ColorDrawable cd = (ColorDrawable) view.getBackground();
        int colorCode = cd.getColor();
        if(colorCode == Color.BLUE)tile.getSmall_grid().changeTile(tile.getPoint());
    }
}
