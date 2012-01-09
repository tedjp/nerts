package au.id.tedp.nertz;

/**
 * Class that represents a pile of cards of a single suit that must be
 * sequential, starting from Ace through King.
 */

public class SequentialSuitPile extends Pile {
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

    public void push(Card card) throws CardSequenceException {
        if (faceup.isEmpty()) {
            suit = card.getSuit();
            faceup.push(card);
        } else {
            if (card.getSuit() != suit ||
                    card.getValue() != faceup.peek().getValue() + 1)
            {
                throw new CardSequenceException(card, faceup.peek());
            }
            faceup.push(card);
        }
    }
}
