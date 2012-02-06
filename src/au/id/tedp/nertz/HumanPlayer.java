package au.id.tedp.nertz;

import android.os.Bundle;

import android.content.Context;

class HumanPlayer extends Player {
    private GameView gameview;

    HumanPlayer(Context ctx, Game game, Lake lake, Deck deck) throws EmptyPileException {
        super("You", lake, deck);
        gameview = new GameView(ctx, this, game);
    }

    HumanPlayer(Context ctx, Game game, Lake lake, Bundle state) {
        super(lake, state);
        gameview = new GameView(ctx, this, game);
    }

    public GameView getGameView() {
        return gameview;
    }
}
