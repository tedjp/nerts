package au.id.tedp.nertz;

import android.os.Bundle;
import android.util.Log;

class AiPlayer extends Player {
    private Game game;

    public AiPlayer(Game game, Lake lake, Deck deck, Bundle state) throws EmptyPileException {
        super("CPU", lake, deck, state);
        this.game = game;
    }

    private GameMove findMove() {
        NertzPile np = getNertzPile();
        Lake lake = getLake();
        River river = getRiver();
        TargetPile target;
        Card nc = np.peek();

        if (nc != null) {
            // Nertz -> Lake
            target = lake.findTargetPile(nc);
            if (target != null)
                return new CardMove(np, target);

            // Nertz -> River
            target = river.findTargetPile(nc);
            if (target != null)
                return new CardMove(np, target);
        }

        // River -> Lake
        for (Pile riverPile: river.getPiles()) {
            Card riverCard = riverPile.peek();
            if (riverCard != null) {
                target = lake.findTargetPile(riverCard);
                if (target != null)
                    return new CardMove(riverPile, target);
            }
        }

        // Stream -> Lake
        Stream stream = getStream();
        Card sc = stream.peek();
        if (sc != null) {
            target = lake.findTargetPile(sc);
            if (target != null)
                return new CardMove(stream, target);

            // Stream -> River
            target = river.findTargetPile(sc);
            if (target != null)
                return new CardMove(stream, target);
        }

        // Try all cards in the river if the cards on top of it
        // were moved to another pile
        // TODO

        return null;
    }

    public void makeMove() {
        try {
            GameMove ourmove = findMove();
            if (ourmove != null) {
                Log.d("Nertz", "Found move: " + ourmove.toString());
                ourmove.execute();
            } else {
                Stream stream = getStream();
                if (stream.isFaceDownEmpty()) {
                    Log.d("Nertz", "Restarting stream pile");
                    stream.restartPile();
                } else {
                    if (stream.isFaceUpEmpty() &&
                            stream.cardsTakenThisTimeThrough() == false) {
                        Log.d("Nertz", "Putting top card under");
                        stream.putTopUnder();
                    } else {
                        Log.d("Nertz", "Drawing cards");
                        stream.flipThree();
                    }
                }
            }

            // This should really be in the (ourmove != null) condition,
            // but putting it here makes it more visible by logging it
            // on every turn.
            if (getNertzPile().isEmpty())
                Log.d("Nertz", "AI Player " + this + " calls Nertz!");
        }
        catch (Exception e) {
            Log.e("Nertz", "Failed to play AI move: " + e.getMessage());
        }
    }
}
