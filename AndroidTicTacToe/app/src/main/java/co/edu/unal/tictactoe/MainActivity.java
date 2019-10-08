package co.edu.unal.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static final int DIALOG_ABOUT_ID=1;
    static final int DIALOG_QUIT_ID = 2;

    // Represents the internal state of the game
    private TicTacToeGame mGame;
    private BoardView mBoardView;
    private boolean mGameOver;
    private boolean mSoundOn;
    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputerMediaPlayer;

    private SharedPreferences mPrefs;

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

        mTextAndroid = findViewById(R.id.ptsAndroid);
        mTextHuman = findViewById(R.id.ptsHuman);
        mTextTie = findViewById(R.id.ptsTie);

        mGame = new TicTacToeGame();
        mBoardView = findViewById(R.id.board);
        mBoardView.setGame(mGame);
        // Listen for touches on the board
        mBoardView.setOnTouchListener(mTouchListener);

        mTextTie.setText("0");
        mTextHuman.setText("0");
        mTextAndroid.setText("0");
        // Restore the scores from the persistent preference data source
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean("sound", true);
        String difficultyLevel = mPrefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_harder));
        if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        else
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);

        startNewGame();
    }
    // Set up the game board.
    private void startNewGame() {
        mGameOver= false;
        mGame.clearBoard();
        mBoardView.invalidate();   // Redraw the board

        mInfoTextView.setText(R.string.turn_human);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bip);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bib);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                return true;
            case R.id.about:
                showDialog(DIALOG_ABOUT_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CANCELED) {
            // Apply potentially new settings

            mSoundOn = mPrefs.getBoolean("sound", true);

            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_harder));

            if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            else
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
           /* case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                int selected=2;
                TicTacToeGame.DifficultyLevel level= mGame.getDifficultyLevel();
                if(level == TicTacToeGame.DifficultyLevel.Easy) selected=0;
                else if(level == TicTacToeGame.DifficultyLevel.Harder) selected=1;
                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog
                                // TODO: Set the diff level of mGame based on which item was selected.
                                TicTacToeGame.DifficultyLevel new_level = TicTacToeGame.DifficultyLevel.Easy;
                                if (item == 1) new_level = TicTacToeGame.DifficultyLevel.Harder;
                                else if (item == 2)
                                    new_level = TicTacToeGame.DifficultyLevel.Expert;
                                if(mGame.setDifficultyLevel(new_level))
                                    startNewGame();

                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();

                break;*/
            case DIALOG_ABOUT_ID:
                builder = new AlertDialog.Builder(this);
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                //builder.setIcon(R.drawable.icon);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();

                break;
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;
        }



        return dialog;
    }

    // Listen for touches on the board
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos))	{

                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner(   );
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_android);
                    int move = mGame.getComputerMove(   );

                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner(   );
                }

                if (winner == 0)
                    mInfoTextView.setText(R.string.turn_human);
                else{
                    if (winner == 1) {
                        mInfoTextView.setText(R.string.result_tie);
                        mGame.mPtsTie+=1;
                        mTextTie.setText(String.valueOf(mGame.mPtsTie));
                    }
                    else if (winner == 2) {
                        mGameOver=true;
                        String defaultMessage = getResources().getString(R.string.result_human_wins);
                        mInfoTextView.setText(mPrefs.getString("victory_message",defaultMessage));
                        mGame.mPtsHuman += 1;
                        mTextHuman.setText(String.valueOf(mGame.mPtsHuman));
                    }
                    else {
                        mGameOver=true;
                        mInfoTextView.setText(R.string.result_android_wins);
                        mGame.mPtsAndroid+=1;
                        mTextAndroid.setText(String.valueOf(mGame.mPtsAndroid));
                    }
                    System.out.println(mGame.mPtsHuman + " " + mGame.mPtsTie + " " + mGame.mPtsAndroid);
                }
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    private boolean setMove(char player, int location) {
        if (mGame.setMove(player, location)) {
            if(player== TicTacToeGame.HUMAN_PLAYER){
                mHumanMediaPlayer.start();

            }
            else{

               /* Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //Log.v(LOG_TAG, "Hello");
                        mComputerMediaPlayer.start();
                    }
                }, 2000);*/
            }
            mBoardView.invalidate();   // Redraw the board

            return true;
        }
        //mComputerMediaPlayer.start();
        return false;
    }

}
