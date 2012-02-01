package au.id.tedp.nertz;

import android.os.Bundle;
import android.util.Log;

class AiPlayer extends Player {
    private Game game;

    public AiPlayer(Game game, Lake lake, Deck deck, Bundle state) throws EmptyPileException {
        super("CPU", lake, deck, state);
        this.game = game;
    }

    public void makeMove(GameMove move) {
        if (move == null)
            return;

        try {
            if (move != null) {
                Log.d("Nertz", "Executing move: " + move.toString());
                move.execute();
            }

            // This should really be in the (move != null) condition,
            // but putting it here makes it more visible by logging it
            // on every turn.
            if (getNertzPile().isEmpty())
                Log.d("Nertz", "AI Player " + this + " calls Nertz!");
        }
        catch (InvalidMoveException e) {
            Log.e("Nertz", "Failed to play AI move: " + e.getMessage());
        }
    }
}
