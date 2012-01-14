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
        setContentView(R.layout.main);
        Game game = new Game();
        GameView gv = new GameView(this, game.getFirstPlayer());
        setContentView(gv);
        /*
        ImageView emptyImage = (ImageView) findViewById(R.id.emptyImage);
        ImageView streamImage = (ImageView) findViewById(R.id.streamImage);

        Resources res = getResources();
        Drawable blueBack = res.getDrawable(R.drawable.b1fv);
        streamImage.setImageDrawable(blueBack);
        */
    }
}
