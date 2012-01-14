package au.id.tedp.nertz;

import java.lang.String;
import java.util.ArrayList;

class Player {
    private String name;
    private Deck deck;
    private Stream stream;
    private ArrayList<TableauPile> river;
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

        river = new ArrayList<TableauPile>(4);
        for (int i = 0; i < 4; ++i) {
            TableauPile tp = new TableauPile();
            // Card will be flipped when game starts
            tp.addFaceDown(deck.nextCard());
            river.add(tp);
        }

        stream = new Stream();
        Dealer.dealRemaining(deck, stream);
        this.lake = lake;
    }

    public void start() {
        // XXX: Check game state
        try {
            for (TableauPile pile : river)
                pile.flipTopCard();
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

    public ArrayList<TableauPile> getRiver() {
        return river;
    }
}
