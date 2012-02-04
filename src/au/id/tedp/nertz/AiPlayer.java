package au.id.tedp.nertz;

import android.os.Bundle;
import android.util.Log;

class AiPlayer extends Player {
    private Game game;

    public AiPlayer(String name, Game game, Lake lake, Deck deck) throws EmptyPileException {
        super(name, lake, deck);
        this.game = game;
    }

    public AiPlayer(Game game, Lake lake, Bundle state) {
        super(lake, state);
        this.game = game;
    }
}
