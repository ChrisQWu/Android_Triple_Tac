package com.example.chriswu.triple_tac_toe;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the logic for the smaller 3x3 squares
 * Handles 3x3 wins on the small grid
 * Notifies the GameController when moves are made and to change the turn
 */

public class Small_Grid {
    private TextView[][] grid = new TextView[Game_Controller.MAX_COL][Game_Controller.MAX_ROW];//stores the smaller tiles
    private List<TextView> textViews = new ArrayList<>();//the TextViews as a list to be used to build the grid
    private Game_Controller game_controller;
    private TextView occupied;

    Small_Grid() {
        game_controller = Game_Controller.getInstance();
    }

    public void setOccupied(TextView textview) {
        occupied = textview;
        occupied.setText("" + game_controller.EMPTY_CHAR);
    }

    public char getOccupied() {
        return occupied.getText().charAt(0);
    }

    public void buildList(List<TextView> textviews) {
        textViews.addAll(textviews);
    }

    /**
     * Creates the Tile[][] using the List<>()
     */
    public void initializeGrid() {
        int row = 0, col = 0;
        for (TextView textView : textViews) {
            Tile tile = new Tile(this, row, col);
            textView.setOnClickListener(new CustomClicker(tile));
            grid[row][col] = textView;
            grid[row][col].setBackgroundColor(Color.BLUE);
            grid[row][col].setText("" + game_controller.EMPTY_CHAR);
            col++;
            if (col >= Game_Controller.MAX_COL) {
                row++;
                col = 0;
            }
        }
    }

    /**
     * Marks the tiles RED if not legal to play
     * Marks the tiles BLUE if legal to play
     *
     * @param on
     */
    public String markGrids(boolean on) {
        String gridState = "";
        for (int row = 0; row < game_controller.MAX_ROW; row++) {
            for (int col = 0; col < game_controller.MAX_COL; col++) {
                if (on && grid[row][col].getText().toString().equals("" + game_controller.EMPTY_CHAR)) {
                    grid[row][col].setBackgroundColor(Color.BLUE);

                }
                else
                {
                    grid[row][col].setBackgroundColor(Color.RED);
                }
                String s = grid[row][col].getText().toString();
                gridState+=s.equals(" ")?"-":s;
            }
        }
        return gridState;
    }

    /**
     * Plays the move according to the corresponding touchListener
     * Set's the box text to the current turn
     * Checks local win
     * Tell the GameController which Small_Grid is next
     *
     * @param point
     */
    public void changeTile(Tuple<Integer, Integer> point) {
        int row = point.row, col = point.col;
        grid[row][col].setText("" + game_controller.getTurn());
        checkWin(row, col);
        game_controller.changeTurn(row, col);
    }

    /**
     * local win check
     *
     * @param row of the Tile in the grid
     * @param col of the Tile in the grid
     * @return if there are 3 Small_Grids that are the same in a row
     */
    private void checkWin(int row, int col) {
        if (checkWinHelper(row, col, 0, 1)//horizontal
                || checkWinHelper(row, col, 1, 0)//vertical
                || checkWinHelper(row, col, 1, 1)//positive diagonal
                || checkWinHelper(row, col, -1, 1))//negative diagonal
        {
            Log.d("Winner", "" + game_controller.getTurn());
            occupied.setText("" + game_controller.getTurn());
            emptyTiles();
        }
        if(checkTie())
        {
            occupied.setText(""+game_controller.TIE);
            emptyTiles();
        }
    }

    /**
     * A helper function that checks two spaces before the tile being checked to after.
     * This checks for the possibilities that the piece is placed at the end of the three in a row
     *
     * @param row    of the Tile in the grid
     * @param col    of the Tile in the grid
     * @param rowInc -1,0,1 direction in the row
     * @param colInc -1,0,1 direction in the column
     * @return true if there is a global win
     */
    private boolean checkWinHelper(int row, int col, int rowInc, int colInc) {
        String middle = grid[row][col].getText().toString();
        int count = 0;
        for (int i = -2; i <= 2; i++) {
            int tempRow = row + i * rowInc;
            int tempCol = col + i * colInc;
            if (tempRow < game_controller.MAX_ROW && tempRow >= 0//bounds check
                    && tempCol < game_controller.MAX_COL && tempCol >= 0) {
                if (!grid[tempRow][tempCol].getText().toString().equals(middle)
                        || grid[tempRow][tempCol].getText().toString().equals("" + game_controller.EMPTY_CHAR)) {
                    count = 0;
                } else {
                    count++;
                    if (count >= 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkTie()
    {
        for (int row = 0; row < game_controller.MAX_ROW; row++) {
            for (int col = 0; col < game_controller.MAX_COL; col++) {
                System.out.println("Grid: Row: "+row+"\tCol: "+col+"  "+grid[row][col].getText());
                if(grid[row][col].getText().toString().equals(""+game_controller.EMPTY_CHAR))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private void emptyTiles()
    {
        for (int row = 0; row < game_controller.MAX_ROW; row++) {
            for (int col = 0; col < game_controller.MAX_COL; col++) {
                grid[row][col].setText("" + game_controller.EMPTY_CHAR);
            }
        }
    }

    public void resetGrid()
    {
        occupied.setText("" + game_controller.EMPTY_CHAR);
        for (int row = 0; row < game_controller.MAX_ROW; row++) {
            for (int col = 0; col < game_controller.MAX_COL; col++) {
                grid[row][col].setBackgroundColor(Color.BLUE);
                grid[row][col].setText("" + game_controller.EMPTY_CHAR);
            }
        }
    }

    public void markAllTiles(boolean on)
    {
        int color = on?Color.BLUE:Color.RED;
        for (int row = 0; row < game_controller.MAX_ROW; row++) {
            for (int col = 0; col < game_controller.MAX_COL; col++) {
                grid[row][col].setBackgroundColor(color);
                grid[row][col].setText("" + game_controller.EMPTY_CHAR);
            }
        }
    }

    public void setGrid(String gridState)
    {
        System.out.println("Small Board: "+gridState);
        char[] chars = gridState.toCharArray();
        char on = chars[0];
        char c = chars[1]=='-'?game_controller.EMPTY_CHAR:chars[1];
        occupied.setText("" + c);
        int i = 2;
        for (int row = 0; row < Game_Controller.MAX_ROW; row++) {
            for (int col = 0; col < Game_Controller.MAX_COL; col++) {
                c = chars[i]=='-'?Game_Controller.EMPTY_CHAR:chars[i];
                grid[row][col].setText("" + c);
                if (on == '1' && c==Game_Controller.EMPTY_CHAR) {
                    grid[row][col].setBackgroundColor(Color.BLUE);
                }
                else
                {
                    grid[row][col].setBackgroundColor(Color.RED);
                }
                i++;
            }
        }
    }
}
