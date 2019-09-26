package co.edu.unal.tictactoe;

import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class TicTacToeGame {

    // The computer's difficulty levels
    public enum DifficultyLevel {Easy, Harder, Expert};

    // Current difficulty level
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;


    public static final int BOARD_SIZE = 9;

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';
    private char[] mBoard;

    protected int mPtsHuman;
    protected int mPtsTie;
    protected int mPtsAndroid;

    private Random mRand;

    public TicTacToeGame() {
        // Seed the random number generator
        mRand = new Random();
        mPtsHuman=0;
        mPtsTie=0;
        mPtsAndroid=0;
        mBoard= new char[9];

    }
    public DifficultyLevel getDifficultyLevel() {
        return mDifficultyLevel;
    }

    public boolean setDifficultyLevel(DifficultyLevel difficultyLevel) {
        boolean mchange=true;
        if( mDifficultyLevel==difficultyLevel) mchange=false;
        mDifficultyLevel = difficultyLevel;
        return mchange;
    }


    /** Clear the board of all X's and O's by setting all spots to OPEN_SPOT. */
    public void clearBoard() {
        // Reset all buttons
        for (int i = 0; i < mBoard.length; i++) {
            mBoard[i]=OPEN_SPOT;
        }

    }
    public char getBoardOccupant(int i){
        return mBoard[i];
    }

    /** Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    public int getComputerMove( ){

        int move = -1;

        if (mDifficultyLevel == DifficultyLevel.Easy)
            move = getRandomMove();
        else if (mDifficultyLevel == DifficultyLevel.Harder) {
            move = getWinningMove( );
            if (move == -1)
                move = getRandomMove();
        }
        else if (mDifficultyLevel == DifficultyLevel.Expert) {

            // Try to win, but if that's not possible, block.
            // If that's not possible, move anywhere.
            move = getWinningMove( );
            if (move == -1)
                move = getBlockingMove( );
            if (move == -1)
                move = getRandomMove();
        }
//mBoardButtons[move].setText(COMPUTER_PLAYER);
        return move;
    }
    private int getWinningMove( ){
        // First see if there's a move O can make to win
        for (int i = 0; i < BOARD_SIZE; i++) {
            if ((mBoard[i] != HUMAN_PLAYER) && (mBoard[i] != COMPUTER_PLAYER)) {
                mBoard[i]= COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    System.out.println("Computer is moving to " + (i + 1));
                    return i;
                }
                else
                    mBoard[i]=OPEN_SPOT;
            }
        }
        return -1;
    }
    private int getBlockingMove( ){
        // See if there's a move O can make to block X from winning
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                mBoard[i]= HUMAN_PLAYER;
                if (checkForWinner( ) == 2) {
                    mBoard[i]=COMPUTER_PLAYER;
                    System.out.println("Computer is moving to " + (i + 1));
                    return i;
                }
                else
                    mBoard[i]=OPEN_SPOT;
            }
        }
        return -1;
    }
    private int getRandomMove(){
        int move=-1;
        // Generate random move
        do
        {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move]== HUMAN_PLAYER || mBoard[move]== COMPUTER_PLAYER);
        return move;
    }


    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    public int checkForWinner(  ){

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+1] == HUMAN_PLAYER &&
                    mBoard[i+2] == HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+1]== COMPUTER_PLAYER &&
                    mBoard[i+2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+3] == HUMAN_PLAYER &&
                    mBoard[i+6] == HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+3] == COMPUTER_PLAYER &&
                    mBoard[i+6] == COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER))
            return 2;
        if ((mBoard[0]== COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
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

    public boolean setMove(char player, int location){
        if(mBoard[location] == OPEN_SPOT) {
            mBoard[location] = player;
            return true;
        }
        return false;
    }


}

