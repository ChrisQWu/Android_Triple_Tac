package com.example.chriswu.triple_tac_toe;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
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
    public static final char TIE = 'T';
    public static final int MAX_ROW = 3;
    public static final int MAX_COL = 3;
    private static int prevRow = -1;
    private static int prevCol = -1;
    private static String boardState = ",1----------,1----------,1----------,"//current turn
                                        +"1----------,1----------,1----------,"//0 or 1 for small grid available
                                        +"1----------,1----------,1----------";
    public static boolean two_player = false;

    private static List<String> moveList = new ArrayList<>();

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
    public void instantiate(List<TextView> textViews, List<Button> buttons) {
        setButtons(buttons);
        instantiateSmallGrid();
        assignSmallGrid(textViews);
        chooseFirstPlayer();
        boardState = getTurn()+boardState;
        notifier = textViews.get(textViews.size() - 1);
        setNotifier("Current turn: "+getTurn());
    }

    void setButtons(List<Button> buttons)
    {
        System.out.println("Buttons: "+buttons.size());
        buttons.get(0).setOnClickListener(new ResetClicker());
        buttons.get(1).setOnClickListener(new UndoClicker());
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

    private static void chooseFirstPlayer() {
        current_Turn = Math.random() > 0.5 ? X : O;
    }

    /**
     * Changes the turn and checks for a global win
     * @param row
     * @param col
     */
    public void changeTurn(int row, int col) {
        if (!checkWin()) {
            moveList.add(boardState);
            prevRow = row;
            prevCol = col;
            boardState = getTurn() + markSmallGrids(row, col);
            current_Turn = current_Turn == X ? O : X;
            setNotifier("Current Turn: " + getTurn());
        }
    }

    /**
     * Global win check
     * @return if there are 3 Small_Grids that are the same in a row
     */
    private boolean checkWin() {
        int row = prevRow, col = prevCol;
        System.out.println(boardState);
        if(prevRow==-1) return false;
        if (checkWinHelper(row, col, 0, 1)//horizontal
                || checkWinHelper(row, col, 1, 0)//vertical
                || checkWinHelper(row, col, 1, 1)//positive diagonal
                || checkWinHelper(row, col, -1, 1))//negative diagonal
        {
            if (checkWinHelper(row, col, 0, 1)){
                largeGrid[row][col].markAllTiles(true);
                largeGrid[row][(col+1)%MAX_COL].markAllTiles(true);
                largeGrid[row][(col+2)%MAX_COL].markAllTiles(true);
            }
            else if (checkWinHelper(row, col, 1, 0)){
                largeGrid[row][col].markAllTiles(true);
                largeGrid[(row+1)%MAX_ROW][col].markAllTiles(true);
                largeGrid[(row+2)%MAX_ROW][col].markAllTiles(true);
            }
            else if (checkWinHelper(row, col, 1, 1)){
                largeGrid[row][col].markAllTiles(true);
                largeGrid[(row+1)%MAX_ROW][(col+1)%MAX_COL].markAllTiles(true);
                largeGrid[(row+2)%MAX_ROW][(col+2)%MAX_COL].markAllTiles(true);
            }
            else if (checkWinHelper(row, col, -1, 1)){
                largeGrid[row][col].markAllTiles(true);
                int newRow = row-1<0?MAX_ROW-1:row-1;
                largeGrid[newRow][(col+1)%MAX_COL].markAllTiles(true);
                newRow = newRow-1<0?MAX_ROW-1:newRow-1;
                largeGrid[newRow][(col+2)%MAX_COL].markAllTiles(true);
            }
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
    private String markSmallGrids(int row, int col) {
        String boardState = "";
        if (largeGrid[row][col].getOccupied() != ' ') {//Mark all other Small_Grids
            for (int r = 0; r < MAX_ROW; r++) {
                for (int c = 0; c < MAX_COL; c++) {
                    char occupied = largeGrid[r][c].getOccupied();
                    boardState += ",";
                    boardState += occupied == EMPTY_CHAR?"1":"0";
                    boardState += occupied == EMPTY_CHAR?"-":occupied;
                    boardState += largeGrid[r][c].markGrids(occupied == EMPTY_CHAR);
                }
            }
        } else {//mark the corresponding Small_Grid as legal
            for (int r = 0; r < MAX_ROW; r++) {
                for (int c = 0; c < MAX_COL; c++) {
                    char occupied = largeGrid[r][c].getOccupied();
                    boardState += ",";
                    boardState += r == row && c == col?"1":"0";
                    boardState += occupied==EMPTY_CHAR?'-':occupied;
                    boardState += largeGrid[r][c].markGrids(r == row && c == col);
                }
            }
        }
        return boardState;
    }

    public static char getTurn() {
        return current_Turn;
    }

    private void setNotifier(String message) {
        notifier.setText(message);
    }

    public static void reset(){
        moveList.clear();
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                largeGrid[row][col].resetGrid();
            }
        }
        chooseFirstPlayer();
        boardState = getTurn()+",1----------,1----------,1----------,"//current turn
                +"1----------,1----------,1----------,"//0 or 1 for small grid available
                +"1----------,1----------,1----------";
    }

    public class ResetClicker implements View.OnClickListener {

        @Override
        /**
         * Only makes a move if the color is BLUE
         */
        public void onClick(View view) {
           Game_Controller.reset();
        }
    }

    public class UndoClicker implements View.OnClickListener {


        @Override
        /**
         * Only makes a move if the color is BLUE
         */
        public void onClick(View view) {
            if(moveList.size()>0)
            {
                boardState = moveList.remove(moveList.size()-1);
                String[] separated = boardState.split(",");
                current_Turn = getTurn()=='X'?'O':'X';
                setNotifier("Current Turn: " + getTurn());
                int row = 0, col = 0;
                for (int i = 1; i < separated.length; i++) {
                    largeGrid[row][col].setGrid(separated[i]);
                    col++;
                    if(col>2)
                    {
                        row++;
                        col = 0;
                    }
                }
            }
        }
    }
}
