package au.id.tedp.nertz;

import java.lang.String;
import java.util.ArrayList;

class Player {
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

        nertzpile = new NertzPile();
        Dealer.dealFaceDown(deck, nertzpile, 13);
        nertzpile.flipTopCard();

        river = new River();

        for (Pile p: river.getPiles()) {
            // Card will be flipped when game starts
            p.addFaceDown(deck.nextCard());
        }

        stream = new Stream();
        Dealer.dealRemaining(deck, stream);
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
}
