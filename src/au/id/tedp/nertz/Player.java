package au.id.tedp.nertz;

import android.os.Bundle;
import java.lang.String;
import java.util.ArrayList;

class Player {
    private String name;
    private Deck deck;
    private Stream stream;
    private River river;
    private NertzPile nertzpile;
    private Lake lake;

    public Player(String name, Lake lake, Deck deck, Bundle state) throws EmptyPileException {
        this.name = name;
        this.lake = lake;
        this.deck = deck;

        android.util.Log.d("Nertz", "Player saved bundle " + (state == null ? "is null" : "is not null"));

        if (state != null) {
            readFromBundle(state);
        } else {
            nertzpile = new NertzPile(deck.dealCards(NertzPile.INITIAL_DEAL));
            nertzpile.flipTopCard();

            river = new River(deck.dealCards(River.NUM_PILES));

            stream = new Stream(deck.dealRemaining());
        }
    }

    public String getName() {
        return name;
    }

    public void start() {
        try {
            for (Pile pile: river.getPiles())
                pile.flipTopCard();
            stream.flipThree();
        } catch (EmptyPileException e) {
            // XXX: What to do?
        }
    }

    public Stream getStream() {
        return stream;
    }

    public NertzPile getNertzPile() {
        return nertzpile;
    }

    public River getRiver() {
        return river;
    }

    public Lake getLake() {
        return lake;
    }

    public void writeToBundle(Bundle b) {
        b.putString("name", name);
        b.putParcelable("stream", stream);
        b.putParcelable("river", river);
        b.putParcelable("nertzpile", nertzpile);
    }

    protected void readFromBundle(Bundle b) {
        name = b.getString("name");
        stream = b.getParcelable("stream");
        river = b.getParcelable("river");
        // The parcelable is a Pile, so copy-construct it into a NertzPile
        nertzpile = new NertzPile((Pile) b.getParcelable("nertzpile"));
    }

    public GameMove findMove() {
        NertzPile np = getNertzPile();
        Lake lake = getLake();
        River river = getRiver();
        TargetPile target;
        Card nc = np.peek();

        if (nc != null) {
            // Nertz -> Lake
            GameMove move = lake.findMoveToLake(nc, np);
            if (move != null)
                return move;

            // Nertz -> River
            target = river.findTargetPile(nc);
            if (target != null)
                return new CardMove(np, target);
        }

        // River -> Lake
        for (Pile riverPile: river.getPiles()) {
            Card riverCard = riverPile.peek();
            if (riverCard != null) {
                GameMove move = lake.findMoveToLake(riverCard, riverPile);
                if (move != null)
                    return move;
            }
        }

        // Stream -> Lake
        Stream stream = getStream();
        Card sc = stream.peek();
        if (sc != null) {
            GameMove move = lake.findMoveToLake(sc, stream);
            if (move != null)
                return move;

            // Stream -> River
            target = river.findTargetPile(sc);
            if (target != null)
                return new CardMove(stream, target);
        }

        // Try all cards in the river if the cards on top of it
        // were moved to another pile
        // TODO

        return findStreamMove();
    }

    protected StreamMove findStreamMove() {
        if (stream.isFaceDownEmpty())
            return new StreamMove(stream, StreamMove.Type.RESTART_PILE);

        if (stream.isFaceUpEmpty() && !stream.cardsTakenThisTimeThrough())
            return new StreamMove(stream, StreamMove.Type.TOP_UNDER);

        if (!stream.isFaceDownEmpty())
            return new StreamMove(stream, StreamMove.Type.FLIP_THREE);

        return null;
    }
}
