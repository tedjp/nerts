package au.id.tedp.nertz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Main extends Activity
{
    private Game game;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        newGame(savedInstanceState);
    }

    public void newGame(Bundle savedInstanceState) {
        try {
            game = new Game(this, savedInstanceState);
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
        Log.d("Nertz", "Saving activity state");
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
            .setCancelable(false)
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
}
