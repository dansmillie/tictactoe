package com.example.dansmillie.tictactoe;

import android.widget.TableLayout;

/**
 * Created by dansmillie on 1/27/17.
 */

public class IndiGame {

    private static final int DEFAULT_SIZE = 3;
    private int[][] board;
    private TableLayout boardView;
    private int size;

    public IndiGame() {
        board = new int[DEFAULT_SIZE][DEFAULT_SIZE];

    }
}
