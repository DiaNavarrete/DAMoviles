package co.edu.unal.tictactoe;

import android.graphics.Color;
import android.view.View;

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
    public void clearBoard() {
        // Reset all buttons
        for (int i = 0; i < MainActivity.mBoardButtons.length; i++) {
            MainActivity.mBoardButtons[i].setText(OPEN_SPOT);
            MainActivity.mBoardButtons[i].setEnabled(true);
            MainActivity.mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

    }


    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    public int getComputerMove(){

        int move;

        // First see if there's a move O can make to win
        for (int i = 0; i < BOARD_SIZE; i++) {
            if ((MainActivity.mBoardButtons[i].getText().charAt(0) != HUMAN_PLAYER) && (MainActivity.mBoardButtons[i].getText().charAt(0) != COMPUTER_PLAYER)) {
                char curr = MainActivity.mBoardButtons[i].getText().charAt(0);
                MainActivity.mBoardButtons[i].setText( COMPUTER_PLAYER);
                if (checkForWinner() == 3) {
                    System.out.println("Computer is moving to " + (i + 1));
                    return 0;
                }
                else
                    MainActivity.mBoardButtons[i] .setText(curr);
            }
        }

        // See if there's a move O can make to block X from winning
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (MainActivity.mBoardButtons[i].getText().charAt(0) != HUMAN_PLAYER && MainActivity.mBoardButtons[i].getText().charAt(0) != COMPUTER_PLAYER) {
                char curr = MainActivity.mBoardButtons[i].getText().charAt(0);   // Save the current number
                MainActivity.mBoardButtons[i].setText(HUMAN_PLAYER);
                if (checkForWinner() == 2) {
                    MainActivity.mBoardButtons[i].setText(COMPUTER_PLAYER);
                    System.out.println("Computer is moving to " + (i + 1));
                    return 0;
                }
                else
                    MainActivity.mBoardButtons[i].setText(curr);
            }
        }

        // Generate random move
        do
        {
            move = mRand.nextInt(BOARD_SIZE);
        } while (MainActivity.mBoardButtons[move].getText().charAt(0) == HUMAN_PLAYER || MainActivity.mBoardButtons[move].getText().charAt(0) == COMPUTER_PLAYER);

        System.out.println("Computer is moving to " + (move + 1));

        MainActivity.mBoardButtons[move].setText(COMPUTER_PLAYER);
        return 0;
    }

    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    public int checkForWinner(){

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (MainActivity.mBoardButtons[i].getText().charAt(0) == HUMAN_PLAYER &&
                    MainActivity.mBoardButtons[i+1].getText().charAt(0) == HUMAN_PLAYER &&
                    MainActivity.mBoardButtons[i+2].getText().charAt(0)== HUMAN_PLAYER)
                return 2;
            if (MainActivity.mBoardButtons[i].getText().charAt(0) == COMPUTER_PLAYER &&
                    MainActivity.mBoardButtons[i+1].getText().charAt(0)== COMPUTER_PLAYER &&
                    MainActivity.mBoardButtons[i+2].getText().charAt(0) == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (MainActivity.mBoardButtons[i].getText().charAt(0) == HUMAN_PLAYER &&
                    MainActivity.mBoardButtons[i+3].getText().charAt(0) == HUMAN_PLAYER &&
                    MainActivity.mBoardButtons[i+6].getText().charAt(0)== HUMAN_PLAYER)
                return 2;
            if (MainActivity.mBoardButtons[i].getText().charAt(0) == COMPUTER_PLAYER &&
                    MainActivity.mBoardButtons[i+3].getText().charAt(0) == COMPUTER_PLAYER &&
                    MainActivity.mBoardButtons[i+6].getText().charAt(0)== COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((MainActivity.mBoardButtons[0].getText().charAt(0) == HUMAN_PLAYER &&
                MainActivity.mBoardButtons[4].getText().charAt(0) == HUMAN_PLAYER &&
                MainActivity.mBoardButtons[8].getText().charAt(0) == HUMAN_PLAYER) ||
                (MainActivity.mBoardButtons[2].getText().charAt(0) == HUMAN_PLAYER &&
                        MainActivity.mBoardButtons[4].getText().charAt(0) == HUMAN_PLAYER &&
                        MainActivity.mBoardButtons[6].getText().charAt(0) == HUMAN_PLAYER))
            return 2;
        if ((MainActivity.mBoardButtons[0].getText().charAt(0) == COMPUTER_PLAYER &&
                MainActivity.mBoardButtons[4].getText().charAt(0) == COMPUTER_PLAYER &&
                MainActivity.mBoardButtons[8].getText().charAt(0) == COMPUTER_PLAYER) ||
                (MainActivity.mBoardButtons[2].getText().charAt(0) == COMPUTER_PLAYER &&
                        MainActivity.mBoardButtons[4].getText().charAt(0) == COMPUTER_PLAYER &&
                        MainActivity.mBoardButtons[6].getText().charAt(0) == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (MainActivity.mBoardButtons[i].getText().charAt(0) != HUMAN_PLAYER && MainActivity.mBoardButtons[i].getText().charAt(0) != COMPUTER_PLAYER)
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

    public void setMove(char player, int location){
        setMove(player, location);
        MainActivity.mBoardButtons[location].setEnabled(false);
        MainActivity.mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            MainActivity.mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            MainActivity.mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }
    public class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }
        @Override
        public void onClick(View view) {
            if (MainActivity.mBoardButtons[location].isEnabled()) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                // If no winner yet, let the computer make a move
                int winner = checkForWinner();
                if (winner == 0) {
                    MainActivity.mInfoTextView.setText("It's Android's turn.");
                    int move = getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = checkForWinner();
                }

                if (winner == 0)
                    MainActivity.mInfoTextView.setText("It's your turn.");
                else if (winner == 1)
                    MainActivity.mInfoTextView.setText("It's a tie!");
                else if (winner == 2)
                    MainActivity.mInfoTextView.setText("You won!");
                else
                    MainActivity.mInfoTextView.setText("Android won!");
            }
        }

    }

}

