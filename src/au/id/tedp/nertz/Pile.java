package au.id.tedp.nertz;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import java.util.ArrayDeque;
import java.util.Deque;

public class Pile {
    protected Deque<Card> facedown, faceup;

    public Pile() {
        facedown = new ArrayDeque<Card>(13);
        faceup = new ArrayDeque<Card>(13);
    }

    public Pile(int faceDownCapacity, int faceUpCapacity) {
        facedown = new ArrayDeque<Card>(faceDownCapacity);
        faceup = new ArrayDeque<Card>(faceUpCapacity);
    }

    public boolean isEmpty() {
        return (facedown.isEmpty() && faceup.isEmpty());
    }

    public void addFaceDown(Card c) {
        facedown.push(c);
    }

    public void addFaceUp(Card c) {
        faceup.push(c);
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
}
