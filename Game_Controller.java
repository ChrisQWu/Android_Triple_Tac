package com.example.chriswu.triple_tac_toe;

import android.widget.TextView;

import java.util.List;

/**
 * Singleton class meant to handle the large grid logic.
 *   Handles the overall win condition, 3 in a row with small grids
 *   Handles valid small grid moves
 */

class Game_Controller {
    private static final Game_Controller ourInstance = new Game_Controller();

    public static final char EMPTY_CHAR = ' ';
    public static final char X = 'X';
    public static final char O = 'O';
    public static final int MAX_ROW = 3;
    public static final int MAX_COL = 3;

    static Game_Controller getInstance() {
        return ourInstance;
    }

    private static char current_Turn;

    private static final Small_Grid[][] largeGrid = new Small_Grid[MAX_COL][MAX_ROW];

    private TextView notifier;

    /**
     * First makes a Small_Grid instance for each element in the largeGrid[][]
     * Second assigns the Small_Grids with TextViews that correspond to their position
     * Third randomly selects a player X or O
     * Fourth sets the large TextView above to let the player's know whose turn it is
     * @param textViews
     */
    public void instantiate(List<TextView> textViews) {
        instantiateSmallGrid();
        assignSmallGrid(textViews);
        chooseFirstPlayer();
        notifier = textViews.get(textViews.size() - 1);
        setNotifier("Current turn: "+getTurn());
    }

    /**
     * Makes an instance of Small_Grid for every cell in the largeGrid[][]
     */
    void instantiateSmallGrid() {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                largeGrid[row][col] = new Small_Grid();
            }
        }
    }

    /**
     * Gives the Small_Grid the TextViews for the winner tile and the 3x3 board then initializes
     * each tile's TextGrid with a Tile instance
     * @param textViews
     */
    void assignSmallGrid(List<TextView> textViews) {
        int row, col;
        int cap = 0;

        for (row = 0; row < MAX_ROW; row++) {
            for (col = 0; col < MAX_COL; col++) {
                largeGrid[row][col].buildList(textViews.subList(cap * 9, (cap + 1) * 9));//The 3x3 TextView
                largeGrid[row][col].setOccupied(textViews.get(cap + 81));//The TextView that displays the local winner
                largeGrid[row][col].initializeGrid();//initialize TextViews
                cap++;//Moves up the index counter
            }
        }
    }

    private void chooseFirstPlayer() {
        current_Turn = Math.random() > 0.5 ? X : O;
    }

    /**
     * Changes the turn and checks for a global win
     * @param row
     * @param col
     */
    public void changeTurn(int row, int col) {
        current_Turn = current_Turn == X ? O : X;
        setNotifier("Current Turn: " + getTurn());
        if (!checkWin(row, col)) {
            markSmallGrids(row, col);
        }

    }

    /**
     * Global win check
     * @param row of the Small_Grid in the largeGrid
     * @param col of the Small_Grid in the largeGrid
     * @return if there are 3 Small_Grids that are the same in a row
     */
    private boolean checkWin(int row, int col) {
        if (checkWinHelper(row, col, 0, 1)//horizontal
                || checkWinHelper(row, col, 1, 0)//vertical
                || checkWinHelper(row, col, 1, 1)//positive diagonal
                || checkWinHelper(row, col, -1, 1))//negative diagonal
        {
            setNotifier("Winner: " + getTurn());
            return true;
        } else return false;
    }

    /**
     * A helper function that checks two spaces before the tile being checked to after.
     * This checks for the possibilities that the piece is placed at the end of the three in a row
     * @param row of the Small_Grid in the largeGrid
     * @param col of the Small_Grid in the largeGrid
     * @param rowInc -1,0,1 direction in the row
     * @param colInc -1,0,1 direction in the column
     * @return true if there is a global win
     */
    private boolean checkWinHelper(int row, int col, int rowInc, int colInc) {
        char middle = largeGrid[row][col].getOccupied();
        int count = 0;
        for (int i = -2; i <= 2; i++) {
            int tempRow = row + i * rowInc;
            int tempCol = col + i * colInc;
            if (tempRow < MAX_ROW && tempRow >= 0//bounds check
                    && tempCol < MAX_COL && tempCol >= 0) {
                if (largeGrid[tempRow][tempCol].getOccupied() != middle
                        || largeGrid[tempRow][tempCol].getOccupied() == EMPTY_CHAR) {
                    count = 0;
                } else {//counts for 3 in a row
                    count++;
                    if (count >= 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Marks the next legal move to play.
     * If the placement on the Small_Grid corresponds to a legal Small_Grid on the largeGrid, mark it viable.
     * Else mark all other legal Small_Grid moves
     * @param row of the Small_Grid in the largeGrid
     * @param col of the Small_Grid in the largeGrid
     */
    private void markSmallGrids(int row, int col) {
        if (largeGrid[row][col].getOccupied() != ' ') {//Mark all other Small_Grids
            for (int r = 0; r < MAX_ROW; r++) {
                for (int c = 0; c < MAX_COL; c++) {
                    largeGrid[r][c].markGrids(largeGrid[r][c].getOccupied() == ' ');
                }
            }
        } else {//mark the corresponding Small_Grid as legal
            for (int r = 0; r < MAX_ROW; r++) {
                for (int c = 0; c < MAX_COL; c++) {
                    largeGrid[r][c].markGrids(r == row && c == col);
                }
            }
        }
    }

    public char getTurn() {
        return current_Turn;
    }

    private void setNotifier(String message) {
        notifier.setText(message);
    }

}
