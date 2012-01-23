package au.id.tedp.nertz;

import android.content.Context;

class HumanPlayer extends Player {
    private GameView gameview;

    HumanPlayer(Context ctx, Game game) {
        super("Player");
        gameview = new GameView(ctx, this, game);
    }

    public GameView getGameView() {
        return gameview;
    }

    public void notifyOfMove(Move move) {
        gameview.invalidate();
    }
}
