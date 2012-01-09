package au.id.tedp.nertz;

import java.util.Stack;

public class SolitairePile {
    private Stack<Card> facedown, faceup;

    public SolitairePile() {
        facedown = new Stack<Card>();
        faceup = new Stack<Card>();
    }

    public boolean empty() {
        return (facedown.empty() && faceup.empty());
    }

    public void addFaceDown(Card c) {
        facedown.push(c);
    }

    public void addFaceUp(Card c) throws CardSequenceException {
        if (faceup.empty()) {
            faceup.push(c);
        } else {
            Card top = faceup.peek();
            if (!isValidMove(top, c)) {
                throw new CardSequenceException(top, c);
            }
            faceup.push(c);
        }
    }

    private static boolean isValidMove(Card top, Card newCard) {
        return (top.getColor() != newCard.getColor()
                && newCard.getValue() + 1 == top.getValue());
    }
}
