package au.id.tedp.nertz;

public class SolitairePile extends Pile {
    public SolitairePile() {
        super(0, 13);
    }

    public void addFaceDown(Card c) {
        facedown.push(c);
    }

    public void addFaceUp(Card c) throws CardSequenceException {
        if (faceup.isEmpty()) {
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
