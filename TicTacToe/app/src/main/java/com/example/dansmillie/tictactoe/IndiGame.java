package com.example.dansmillie.tictactoe;

import android.widget.TableLayout;

/**
 * Created by dansmillie on 1/27/17.
 */

public class IndiGame {

    private static final int DEFAULT_SIZE = 3;
    private static final int X = -1, PLAYER = -1;
    private static final int O = 1;
    private int[][] board;
    private TableLayout boardView;
    private int size;
    private int winner = 0;

    public IndiGame() {
        board = new int[DEFAULT_SIZE][DEFAULT_SIZE];

    }
    public boolean isSet(int i, int j) {
        return board[i][j] != 0;
    }

    public int getWinner() {
        return winner;
    }

    public void set(int i, int j, int mark) {
        board[i][j] = mark;

        if (didPlayerWin()) {
            winner = X;
        } else if (didCompWin()){
            winner = O;
        }
    }
    public boolean didPlayerWin() {
        return didWin(X);
    }

    public boolean didCompWin() {
        return didWin(O);
    }

    private boolean didWin(int mark) {
        int goal = mark * size;
        //check rows and cols
        for (int i = 0; i < size; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < size; j++) {
                rowSum += board[i][j];
                colSum += board[j][i];
            }
            if (rowSum == goal || colSum == goal) {
                return true;
            }
        }

        //check diagonals
        int lrSum = 0;
        int rlSum = 0;
        for (int i = 0; i < size; i++) {
            lrSum += board[i][i];
            rlSum += board[i][size - (i + 1)];
        }
        if (lrSum == goal || rlSum == goal) {
            return true;
        }


        return false;
    }

}
