package au.id.tedp.nertz;

import android.content.Context;

class HumanPlayer extends Player {
    private GameView gameview;

    HumanPlayer(Context ctx) {
        super("Player");
        gameview = new GameView(ctx, this);
    }

    public GameView getGameView() {
        return gameview;
    }

    public void notifyOfChange() {
        gameview.invalidate();
    }
}
