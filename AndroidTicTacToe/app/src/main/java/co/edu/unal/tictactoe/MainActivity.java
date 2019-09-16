package co.edu.unal.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    // Represents the internal state of the game
    private TicTacToeGame mGame;

    // Buttons making up the board
    private  Button[] mBoardButtons;

    // Various text displayed
    private TextView mInfoTextView;

    private TextView mTextHuman;
    private TextView mTextTie;
    private TextView mTextAndroid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfoTextView = findViewById(R.id.information);
        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = findViewById(R.id.one);
        mBoardButtons[1] = findViewById(R.id.two);
        mBoardButtons[2] = findViewById(R.id.three);
        mBoardButtons[3] = findViewById(R.id.four);
        mBoardButtons[4] = findViewById(R.id.five);
        mBoardButtons[5] = findViewById(R.id.six);
        mBoardButtons[6] = findViewById(R.id.seven);
        mBoardButtons[7] = findViewById(R.id.eight);
        mBoardButtons[8] = findViewById(R.id.nine);
        mTextAndroid = findViewById(R.id.ptsAndroid);
        mTextHuman = findViewById(R.id.ptsHuman);
        mTextTie = findViewById(R.id.ptsTie);

        mGame = new TicTacToeGame();
        mTextTie.setText("0");
        mTextHuman.setText("0");
        mTextAndroid.setText("0");
        startNewGame();
    }
    // Set up the game board.
    private void startNewGame() {

        mGame.clearBoard(mBoardButtons);
        for (int i = 0; i < mBoardButtons.length; i++)
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        // Human goes first
        mInfoTextView.setText(R.string.turn_human);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startNewGame();
        return true;
    }


    public class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }
        //@Override
        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled()) {
                mGame.setMove(TicTacToeGame.HUMAN_PLAYER, location, mBoardButtons,  mInfoTextView);

                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner( mBoardButtons,  mInfoTextView);
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_android);
                    int move = mGame.getComputerMove( mBoardButtons,  mInfoTextView);
                    mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, move, mBoardButtons,  mInfoTextView);
                    winner = mGame.checkForWinner( mBoardButtons,  mInfoTextView);
                }

                if (winner == 0)
                    mInfoTextView.setText(R.string.turn_human);
                else{
                    for (int i = 0; i < mBoardButtons.length; i++)
                        mBoardButtons[i].setEnabled(false);
                    if (winner == 1) {
                        mInfoTextView.setText(R.string.result_tie);
                        mGame.mPtsTie+=1;
                        mTextTie.setText(String.valueOf(mGame.mPtsTie));
                    }
                    else if (winner == 2) {
                        mInfoTextView.setText(R.string.result_human_wins);
                        mGame.mPtsHuman += 1;
                        mTextHuman.setText(String.valueOf(mGame.mPtsHuman));
                    }
                    else {
                        mInfoTextView.setText(R.string.result_android_wins);
                        mGame.mPtsAndroid+=1;
                        mTextAndroid.setText(String.valueOf(mGame.mPtsAndroid));
                    }
                    System.out.println(mGame.mPtsHuman + " " + mGame.mPtsTie + " " + mGame.mPtsAndroid);
                }
            }
        }

    }

}
