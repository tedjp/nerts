package au.id.tedp.nertz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Main extends Activity
{
    private Game game;
    AlertDialog winnerDialog;
    private static final int WIN_SCORE = 100;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        newGame(savedInstanceState);
    }

    public void newGame(Bundle savedInstanceState) {
        winnerDialog = null;
        try {
            int humanScore = 0;
            Pair<String,String> opponentNames = null;
            if (game != null) {
                humanScore = game.getScoreKeeper().getHumanScore();
                if (humanScore >= WIN_SCORE) {
                    // Player beat these opponents. Pick new opponents.
                    humanScore = 0;
                    Pair<String,String> existingOppNames;
                    existingOppNames = game.getOpponentNames();
                    do {
                        // Get new opponents and ensure they're not the same as
                        // the previous opponents
                        opponentNames = OpponentNames.getPair();
                    } while (existingOppNames == opponentNames);
                } else {
                    // Keep existing opponents
                    opponentNames = game.getOpponentNames();
                }
            }
            game = new Game(this, savedInstanceState, humanScore, opponentNames);
            setContentView(game.getHumanPlayer().getGameView());
        } catch (EmptyPileException e) {
            String failMsg = "Failed to start game: " + e.getMessage();
            Toast toast = Toast.makeText(this, failMsg, Toast.LENGTH_SHORT);
            toast.show();
            Log.e("Nertz", failMsg);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        game.saveState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.new_game:
            newGame(null);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit game?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Main.this.finish();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void declareWinner(Player winner) {
        if (winnerDialog != null) {
            // Don't stack another one
            return;
        }

        String buttonText;
        if (game.getPlayerScore() >= WIN_SCORE)
            buttonText = "Next Opponents";
        else
            buttonText = "Next Round";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(String.format("%s won!", winner.getName()))
            // XXX: Show scores
            .setCancelable(false)
            .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Main.this.newGame(null);
                }
            });
        winnerDialog = builder.create();
        winnerDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        game.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        game.onResume();
    }
}
