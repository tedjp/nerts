package au.id.tedp.nertz;

import android.util.Log;

class AiPlayer extends Player {
    private Game game;

    public AiPlayer(Game game) {
        super("CPU");
        this.game = game;
    }

    private Move findMove() {
        NertzPile np = getNertzPile();
        Lake lake = getLake();
        River river = getRiver();
        TargetPile target;
        Card nc = np.peek();

        if (nc != null) {
            // Nertz -> Lake
            target = lake.findTargetPile(nc);
            if (target != null)
                return new Move(np, target);

            // Nertz -> River
            target = river.findTargetPile(nc);
            if (target != null)
                return new Move(np, target);
        }

        // River -> Lake
        for (Pile riverPile: river.getPiles()) {
            Card riverCard = riverPile.peek();
            if (riverCard != null) {
                target = lake.findTargetPile(riverCard);
                if (target != null)
                    return new Move(riverPile, target);
            }
        }

        // Stream -> Lake
        Stream stream = getStream();
        Card sc = stream.peek();
        if (sc != null) {
            target = lake.findTargetPile(sc);
            if (target != null)
                return new Move(stream, target);

            // Stream -> River
            target = river.findTargetPile(sc);
            if (target != null)
                return new Move(stream, target);
        }

        // Try all cards in the river if the cards on top of it
        // were moved to another pile
        // TODO

        return null;
    }

    public void makeMove() {
        try {
            Move ourmove = findMove();
            if (ourmove != null) {
                Log.d("Nertz", "Found move: " + ourmove.getCard().toString()
                        + " from " + ourmove.source.toString() + " to " +
                        ourmove.dest.toString());
                playMove(ourmove);
            } else {
                // No Move-compatible move available, so draw some cards from the stream
                Log.d("Nertz", "No move, just draw cards");
                Stream stream = getStream();
                if (!stream.isFaceDownEmpty())
                    stream.flipThree();
                else
                    stream.restartPile();
            }
        }
        catch (Exception e) {
            Log.e("Nertz", "Failed to play AI move: " + e.getMessage());
        }
    }
}
