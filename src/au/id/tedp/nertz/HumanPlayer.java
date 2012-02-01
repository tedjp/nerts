package au.id.tedp.nertz;

import android.os.Bundle;

import android.content.Context;

class HumanPlayer extends Player {
    private GameView gameview;

    HumanPlayer(Context ctx, Game game, Lake lake, Deck deck, Bundle state) throws EmptyPileException {
        super("Player", lake, deck, state);
        gameview = new GameView(ctx, this, game);
    }

    public GameView getGameView() {
        return gameview;
    }

    public void onAiMove() {
        gameview.invalidate();
    }
}
