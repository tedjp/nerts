package au.id.tedp.nertz;

import java.util.Stack;

/**
 * Class that represents a pile of cards of a single suit that must be
 * sequential, starting from Ace through King.
 */

public class SequentialSuitPile {
    private Stack<Card> cards;
    private Card.Suit suit;

    public SequentialSuitPile() {
        cards = new Stack<Card>();
        suit = null;
    }

    public boolean isComplete() {
        if (cards.empty())
            return false;
        return (cards.peek().getFace() == Card.Face.KING);
    }

    public void push(Card card) throws CardSequenceException {
        if (cards.empty()) {
            suit = card.getSuit();
            cards.push(card);
        } else {
            if (card.getSuit() != suit ||
                    card.getValue() != cards.peek().getValue() + 1)
            {
                throw new CardSequenceException(card, cards.peek());
            }
            cards.push(card);
        }
    }
}
