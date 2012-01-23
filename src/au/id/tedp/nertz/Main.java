package au.id.tedp.nertz;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.os.Bundle;

public class Main extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        Game game = new Game(this);
        setContentView(game.getHumanPlayer().getGameView());
    }
}
