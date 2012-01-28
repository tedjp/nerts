package au.id.tedp.nertz;

import android.app.Activity;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Log;

public class Main extends Activity
{
    private Game game;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
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
}
