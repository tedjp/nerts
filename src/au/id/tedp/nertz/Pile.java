package au.id.tedp.nertz;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class Pile {
    protected Deque<Card> facedown, faceup;

    private final int DEFAULT_DECKSIZE = 13;

    public Pile() {
        this.facedown = new ArrayDeque<Card>(DEFAULT_DECKSIZE);
        this.faceup = new ArrayDeque<Card>(DEFAULT_DECKSIZE);
    }

    public Pile(Collection<Card> facedown, Collection<Card> faceup) {
        this.facedown = new ArrayDeque<Card>(facedown);
        this.faceup = new ArrayDeque<Card>(faceup);
    }

    public Pile(Collection<Card> facedown, int faceUpCapacity) {
        this.facedown = new ArrayDeque<Card>(facedown);
        this.faceup = new ArrayDeque<Card>(faceUpCapacity);
    }

    public Pile(Card faceup) {
        // Starting with a single face-up card means we'll probably never add
        // any face-down cards.
        this.facedown = new ArrayDeque<Card>(0);
        this.faceup = new ArrayDeque<Card>(DEFAULT_DECKSIZE);
        this.faceup.push(faceup);
    }

    public Pile(int faceDownCapacity, int faceUpCapacity) {
        facedown = new ArrayDeque<Card>(faceDownCapacity);
        faceup = new ArrayDeque<Card>(faceUpCapacity);
    }

    public boolean isEmpty() {
        return (facedown.isEmpty() && faceup.isEmpty());
    }

    /**
     * Take the top face-down card and put it face-up.
     * Throws an exception if there is already a card face-up on top of
     * the pile.
     */
    public void flipTopCard() throws EmptyPileException {
        if (!faceup.isEmpty())
            throw new EmptyPileException("Cannot flip top card: top card is already face-up");

        faceup.push(facedown.pop());
    }

    public BitmapDrawable topCardImage(Resources res) {
        if (!faceup.isEmpty()) {
            Card topCard = faceup.peek();
            return DeckGraphics.getBitmapDrawable(res, topCard);
        }
        if (!facedown.isEmpty()) {
            return DeckGraphics.getCardBack(res);
        }
        return null;
    }

    public Card pop() throws EmptyPileException {
        if (!faceup.isEmpty())
            return faceup.pop();

        throw new EmptyPileException("Pile is empty");
    }

    public boolean isFaceDownEmpty() {
        return facedown.isEmpty();
    }

    public boolean isFaceUpEmpty() {
        return faceup.isEmpty();
    }

    public void push(Card c) throws CardSequenceException {
        faceup.push(c);
    }

    public Deque<Card> getFaceUpCards() {
        return faceup;
    }
}
