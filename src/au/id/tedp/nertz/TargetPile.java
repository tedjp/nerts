package au.id.tedp.nertz;

public abstract class TargetPile extends Pile {
    public TargetPile(Card c) {
        super(c);
    }

    public TargetPile(int faceDownCapacity, int faceUpCapacity) {
        super(faceDownCapacity, faceUpCapacity);
    }

    public abstract boolean isValidMove(Card card);
}
