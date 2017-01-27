package com.example.dansmillie.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TableLayout;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.ViewGroup;
import java.util.Random;
import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final int SIZE = 3;
    private static final int X = -1, PLAYER = -1;
    private static final int O = 1;
    private int[][] board;
    private boolean isPlayerTurn;
    private TableLayout boardView;
    private Random rand = new Random();
    private HashSet<IndexPair> availableCells;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onStart(null);
    }

    public boolean onStart(View view) {
        board = new int[SIZE][SIZE];
        isPlayerTurn = true;
        boardView = (TableLayout)findViewById(R.id.board);
        availableCells = new HashSet<IndexPair>();

        TextView status = (TextView)findViewById(R.id.status);
        status.setText(USER_TURN);

        for (int i = 0; i < SIZE; i++) {
            TableRow row = (TableRow) boardView.getChildAt(i);
            for (int j = 0; j < SIZE; j++) {
                TextView cell = (TextView) row.getChildAt(j);
                cell.setText(R.string.none);
                cell.setOnTouchListener(new TouchListener(i, j, cell));
                availableCells.add(new IndexPair(i, j));
            }
        }

        return true;
    }

    public boolean isSet(int i, int j) {
        return board[i][j] != 0;
    }

    public boolean didPlayerWin() {
        return didWin(X);
    }

    public boolean didCompWin() {
        return didWin(O);
    }

    private boolean didWin(int mark) {
        int goal = mark * SIZE;
        //check rows and cols
        for (int i = 0; i < SIZE; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < SIZE; j++) {
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
        for (int i = 0; i < SIZE; i++) {
            lrSum += board[i][i];
            rlSum += board[i][SIZE - (i + 1)];
        }
        if (lrSum == goal || rlSum == goal) {
            return true;
        }


        return false;
    }

    public void computerTurn() {

        TextView stat = (TextView) findViewById(R.id.status);
        stat.setText(COMPUTER_TURN);

        if (availableCells.size() == 0) {
            stat.setText("Game ends in a Draw!");
            return;
        }

        //pick a cell
        int row = 0, col = 0;
        int pick = rand.nextInt(availableCells.size());
        Iterator<IndexPair> iter = availableCells.iterator();
        int count = 0;
        while (iter.hasNext()) {
            IndexPair curr = iter.next();
            if (count == pick) {
                row = curr.row;
                col = curr.col;
                iter.remove();
                break;
            }
            count++;
        }


        //update gameState
        board[row][col] = O;
        TableRow rowView = (TableRow) boardView.getChildAt(row);
        TextView cell = (TextView) rowView.getChildAt(col);
        cell.setText(R.string.O);
        if (didCompWin()) {
            stat.setText("Computer Wins!");
        } else if (availableCells.size() == 0) {
            stat.setText("Game ends in a Draw!");
            return;
        } else {
            isPlayerTurn = true;
            stat.setText(USER_TURN);
        }


    }

    private class TouchListener implements View.OnTouchListener {

        private int row;
        private int col;
        private View view;

        public TouchListener(int r, int c, TextView v) {
            row = r;
            col = c;
            view = v;

        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!isPlayerTurn) {
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                TextView cell = (TextView) v;
                TextView stat = (TextView)findViewById(R.id.status);
                if(!isSet(row,col)) {
                    board[row][col] = X;
                    cell.setText(R.string.X);
                    Iterator<IndexPair> iter = availableCells.iterator();
                    while (iter.hasNext()) {
                        IndexPair curr = iter.next();
                        if (curr.col == col && curr.row == row) {
                            iter.remove();
                            break;
                        }
                    }

                    isPlayerTurn = false;
                    if (didPlayerWin()) {
                        stat.setText("Player Wins!");
                    } else {
                        computerTurn();
                    }

                }
                else{
                    stat.setText(stat.getText()+" Please choose a Cell Which is not already Occupied");
                }
            }
            return false;
        }
    }

    private class IndexPair {
        public int row;
        public int col;

        public IndexPair(int i, int j) {
            row = i;
            col = j;
        }


    }


}
