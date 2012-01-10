package au.id.tedp.nertz;

public class Dealer {
    private enum Direction { FACE_UP, FACE_DOWN };

    public static void dealFaceDown(Deck d, Pile p, int num) throws EmptyPileException {
        deal(d, p, num, Direction.FACE_DOWN);
    }

    public static void dealFaceUp(Deck d, Pile p, int num) throws EmptyPileException {
        deal(d, p, num, Direction.FACE_UP);
    }

    /**
     * Deal the remaining cards to the given pile, face down.
     * The order of the remaining cards is reversed, but since the pile
     * is expected to be unordered, that should not matter.
     */
    public static void dealRemaining(Deck d, Pile p) {
        while (d.hasNextCard())
            p.addFaceDown(d.nextCard());
    }

    private static void deal(Deck d, Pile p, int num, Direction dir) throws EmptyPileException {
        for (int i = 0; i < num; ++i) {
            Card c = d.nextCard();
            if (dir == Direction.FACE_DOWN)
                p.addFaceDown(c);
            else
                p.addFaceUp(c);
        }
    }
}
