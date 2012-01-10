package au.id.tedp.nertz;

public class TableauPile extends Pile {
    public TableauPile() {
        super(0, 13);
    }

    public void play(Card c) throws CardSequenceException {
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
