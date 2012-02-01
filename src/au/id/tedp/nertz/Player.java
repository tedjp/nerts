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

    public void playMove(Move move) throws EmptyPileException, CardSequenceException {
        Card card = move.source.pop();
        move.dest.push(card);
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
}
