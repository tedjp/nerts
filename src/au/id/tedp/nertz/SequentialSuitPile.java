package au.id.tedp.nertz;

/**
 * Class that represents a pile of cards of a single suit that must be
 * sequential, starting from Ace through King.
 */

public class SequentialSuitPile extends Pile implements TargetPile {
    private Card.Suit suit;

    public SequentialSuitPile() {
        super(0, 13);
        suit = null;
    }

    public boolean isComplete() {
        if (faceup.isEmpty())
            return false;
        return (faceup.peek().getFace() == Card.Face.KING);
    }

    public synchronized void push(Card card) throws CardSequenceException {
        if (faceup.isEmpty()) {
            // Only accept Aces
            if (!isValidMove(card)) {
                throw new CardSequenceException(card);
            }
            suit = card.getSuit();
            faceup.push(card);
        } else {
            if (!isValidMove(card)) {
                throw new CardSequenceException(card, faceup.peek());
            }
            faceup.push(card);
        }
    }

    public boolean isValidMove(Card c) {
        if (isEmpty())
            return (c.getFace() == Card.Face.ACE);

        return (c.getSuit() == this.suit
                && c.getValue() == faceup.peek().getValue() + 1);
    }
}
