package co.edu.unal.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class TicTacToeGame {

    public static final int BOARD_SIZE = 9;

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';

    private Random mRand;

    public TicTacToeGame() {
        // Seed the random number generator
        mRand = new Random();

/*
        char turn = HUMAN_PLAYER;    // Human starts first
        int  win = 0;                // Set to 1, 2, or 3 when game is over

    // Buttons making up the board
    public  Button[] mBoardButtons;

    // Various text displayed
    public TextView mInfoTextView;
        // Keep looping until someone wins or a tie
        while (win == 0)
        {
            displayBoard();

            if (turn == HUMAN_PLAYER)
            {
                getUserMove();
                turn = COMPUTER_PLAYER;
            }
            else
            {
                getComputerMove();
                turn = HUMAN_PLAYER;
            }

            win = checkForWinner();
        }
 */
    }


    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    public void clearBoard(Button[] mBoardButtons) {
        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText(OPEN_SPOT);
            //MainActivity.mBoardButtons[i].setEnabled(true);
            //MainActivity.mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

    }


    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    public int getComputerMove(Button[] mBoardButtons, TextView mInfoTextView){

        int move;

        // First see if there's a move O can make to win
        for (int i = 0; i < BOARD_SIZE; i++) {
            if ((mBoardButtons[i].getText().charAt(0) != HUMAN_PLAYER) && (mBoardButtons[i].getText().charAt(0) != COMPUTER_PLAYER)) {
                char curr = mBoardButtons[i].getText().charAt(0);
                mBoardButtons[i].setText( COMPUTER_PLAYER);
                if (checkForWinner( mBoardButtons,  mInfoTextView) == 3) {
                    System.out.println("Computer is moving to " + (i + 1));
                    return 0;
                }
                else
                    mBoardButtons[i] .setText(curr);
            }
        }

        // See if there's a move O can make to block X from winning
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoardButtons[i].getText().charAt(0) != HUMAN_PLAYER && mBoardButtons[i].getText().charAt(0) != COMPUTER_PLAYER) {
                char curr = mBoardButtons[i].getText().charAt(0);   // Save the current number
                mBoardButtons[i].setText(HUMAN_PLAYER);
                if (checkForWinner( mBoardButtons,  mInfoTextView) == 2) {
                    mBoardButtons[i].setText(COMPUTER_PLAYER);
                    System.out.println("Computer is moving to " + (i + 1));
                    return 0;
                }
                else
                    mBoardButtons[i].setText(curr);
            }
        }

        // Generate random move
        do
        {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoardButtons[move].getText().charAt(0) == HUMAN_PLAYER || mBoardButtons[move].getText().charAt(0) == COMPUTER_PLAYER);

        System.out.println("Computer is moving to " + (move + 1));

        mBoardButtons[move].setText(COMPUTER_PLAYER);
        return 0;
    }

    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    public int checkForWinner(Button[] mBoardButtons, TextView mInfoTextView){

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoardButtons[i].getText().charAt(0) == HUMAN_PLAYER &&
                    mBoardButtons[i+1].getText().charAt(0) == HUMAN_PLAYER &&
                    mBoardButtons[i+2].getText().charAt(0)== HUMAN_PLAYER)
                return 2;
            if (mBoardButtons[i].getText().charAt(0) == COMPUTER_PLAYER &&
                    mBoardButtons[i+1].getText().charAt(0)== COMPUTER_PLAYER &&
                    mBoardButtons[i+2].getText().charAt(0) == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoardButtons[i].getText().charAt(0) == HUMAN_PLAYER &&
                    mBoardButtons[i+3].getText().charAt(0) == HUMAN_PLAYER &&
                    mBoardButtons[i+6].getText().charAt(0)== HUMAN_PLAYER)
                return 2;
            if (mBoardButtons[i].getText().charAt(0) == COMPUTER_PLAYER &&
                    mBoardButtons[i+3].getText().charAt(0) == COMPUTER_PLAYER &&
                    mBoardButtons[i+6].getText().charAt(0)== COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoardButtons[0].getText().charAt(0) == HUMAN_PLAYER &&
                mBoardButtons[4].getText().charAt(0) == HUMAN_PLAYER &&
                mBoardButtons[8].getText().charAt(0) == HUMAN_PLAYER) ||
                (mBoardButtons[2].getText().charAt(0) == HUMAN_PLAYER &&
                        mBoardButtons[4].getText().charAt(0) == HUMAN_PLAYER &&
                        mBoardButtons[6].getText().charAt(0) == HUMAN_PLAYER))
            return 2;
        if ((mBoardButtons[0].getText().charAt(0) == COMPUTER_PLAYER &&
                mBoardButtons[4].getText().charAt(0) == COMPUTER_PLAYER &&
                mBoardButtons[8].getText().charAt(0) == COMPUTER_PLAYER) ||
                (mBoardButtons[2].getText().charAt(0) == COMPUTER_PLAYER &&
                        mBoardButtons[4].getText().charAt(0) == COMPUTER_PLAYER &&
                        mBoardButtons[6].getText().charAt(0) == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoardButtons[i].getText().charAt(0) != HUMAN_PLAYER && mBoardButtons[i].getText().charAt(0) != COMPUTER_PLAYER)
                return 0;
        }

        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }


    /** Set the given player at the given location on the game board.
     *  The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */

    public void setMove(char player, int location,Button[] mBoardButtons, TextView mInfoTextView){
        setMove(player, location, mBoardButtons,  mInfoTextView);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }

}

