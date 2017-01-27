package com.example.dansmillie.tictactoe;

import android.support.v7.app.ActionBar;
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
    private IndiGame[][] board;
    private int[][] realBoard;
    private boolean isPlayerTurn;
    private TableLayout boardView;
    private Random rand = new Random();
    private HashSet<IndexQuad> availableCells;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onStart(null);
    }

    public boolean onStart(View view) {
        board = new IndiGame[SIZE][SIZE];
        realBoard = new int[SIZE][SIZE];
        isPlayerTurn = true;
        boardView = (TableLayout)findViewById(R.id.board);
        availableCells = new HashSet<IndexQuad>();

        TextView status = (TextView)findViewById(R.id.status);
        status.setText(USER_TURN);

        for (int i = 0; i < SIZE; i++) {
            TableRow oRow = (TableRow) boardView.getChildAt(i);
            for (int j = 0; j < SIZE; j++) {
                TableLayout indGame = (TableLayout) oRow.getChildAt(j);
                for (int k = 0; k < SIZE; k++) {
                    TableRow iRow = (TableRow) indGame.getChildAt(k);
                    for (int l = 0; l < SIZE; l++) {
                        TextView cell = (TextView)iRow.getChildAt(l);
                        cell.setText(R.string.none);
                        cell.setOnTouchListener(new TouchListener(i, j, k, l, cell));
                        availableCells.add(new IndexQuad(i, j, k, l));
                    }
                }

            }
        }

        return true;
    }

    public boolean didPlayerWin() {
        return didWin(X);
    }

    public boolean didCompWin() {
        return didWin(O);
    }

    public boolean playerWonSquare(int i, int j) {
        return board[i][j].getWinner() == X;
    }

    public boolean compWonSquare(int i, int j) {
        return board[i][j].getWinner() == O;
    }

    private boolean didWin(int mark) {
        int goal = mark * SIZE;
        //check rows and cols
        for (int i = 0; i < SIZE; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < SIZE; j++) {
                rowSum += realBoard[i][j];
                colSum += realBoard[j][i];
            }
            if (rowSum == goal || colSum == goal) {
                return true;
            }
        }

        //check diagonals
        int lrSum = 0;
        int rlSum = 0;
        for (int i = 0; i < SIZE; i++) {
            lrSum += realBoard[i][i];
            rlSum += realBoard[i][SIZE - (i + 1)];
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
        int oRow = 0, oCol = 0, iRow = 0, iCol = 0;
        int pick = rand.nextInt(availableCells.size());
        Iterator<IndexQuad> iter = availableCells.iterator();
        int count = 0;
        while (iter.hasNext()) {
            IndexQuad curr = iter.next();
            if (count == pick) {
                oRow = curr.oRow;
                oCol = curr.oCol;
                iRow = curr.iRow;
                iCol = curr.iCol;
                iter.remove();
                break;
            }
            count++;
        }


        //update gameState
        board[oRow][oCol].set(iRow, iCol, O);
        TableRow orowView = (TableRow) boardView.getChildAt(oRow);
        TableLayout innerGame = (TableLayout) orowView.getChildAt(oCol);
        TableRow iRowView = (TableRow) innerGame.getChildAt(iRow);
        TextView cell = (TextView) iRowView.getChildAt(iCol);
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

        private int oRow;
        private int oCol;
        private int iRow;
        private int iCol;
        private View view;

        public TouchListener(int or, int oc, int ir, int ic, TextView v) {
            oRow = or;
            oCol = oc;
            iRow = ir;
            iCol = ic;
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
                if(!board[oRow][oCol].isSet(iRow, iCol)) {
                    board[oRow][oCol].set(iRow, iCol, X);
                    cell.setText(R.string.X);
                    Iterator<IndexQuad> iter = availableCells.iterator();
                    while (iter.hasNext()) {
                        IndexQuad curr = iter.next();
                        if (curr.oCol == oCol && curr.oRow == oRow && curr.iCol == iCol && curr.iRow == iRow) {
                            iter.remove();
                            break;
                        }
                    }

                    isPlayerTurn = false;
                    if (playerWonSquare(oRow, oCol)) {
                        realBoard[oRow][oCol] = X;
                    }
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

    private class IndexQuad {
        public int oRow;
        public int oCol;
        public int iRow;
        public int iCol;

        public IndexQuad(int i, int j, int k, int l) {
            oRow = i;
            oCol = j;
            iRow = k;
            iCol = l;
        }


    }


}
