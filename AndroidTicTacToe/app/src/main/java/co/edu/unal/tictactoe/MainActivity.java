package co.edu.unal.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;


import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    // Represents the internal state of the game
    private TicTacToeGame mGame;
    // Buttons making up the board
    public static Button[] mBoardButtons;

    // Various text displayed
    public static TextView mInfoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mInfoTextView = findViewById(R.id.information);

        mGame = new TicTacToeGame();
        startNewGame();
    }
    // Set up the game board.
    private void startNewGame() {

        mGame.clearBoard();
        // Human goes first
        mInfoTextView.setText("You go first.");

    }




}
