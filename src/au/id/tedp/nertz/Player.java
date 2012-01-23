package au.id.tedp.nertz;

import java.lang.String;
import java.util.ArrayList;

abstract class Player {
    private String name;
    private Deck deck;
    private Stream stream;
    private River river;
    private NertzPile nertzpile;
    private Lake lake;

    public Player(String name) {
        this.name = name;
        deck = new Deck(this);
    }

    public String getName() {
        return name;
    }

    public void setup(Lake lake) throws EmptyPileException {
        deck.shuffle();

        nertzpile = new NertzPile(deck.dealCards(13));
        nertzpile.flipTopCard();

        river = new River(deck.dealCards(River.NUM_PILES));

        stream = new Stream(deck.dealRemaining());
        this.lake = lake;
    }

    public void start() {
        // XXX: Check game state
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

    // XXX: In future the Move could be passed to help optimize handling
    abstract void notifyOfChange();

    public void playMove(Move move) throws EmptyPileException, CardSequenceException {
        Card card = move.source.pop();
        move.dest.push(card);
    }
}
