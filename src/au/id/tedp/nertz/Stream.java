package au.id.tedp.nertz;

import java.util.Collection;
import java.util.Deque;

public class Stream extends Pile {
    public Stream(Collection<Card> cards) {
        /* Since the stream can have all its cards either face down or
         * face up, provide enough storage for both cases. */
        super(cards, 35);
    }

    public void flipThree() throws EmptyPileException {
        for (int i = 0; i < 3; ++i) {
            if (!facedown.isEmpty()) {
                faceup.push(facedown.pop());
            }
            else {
                if (i == 0)
                    throw new EmptyPileException("Stream's face-down pile is empty");
                // It doesn't matter if there are no more face-down cards
                // for card 2 or card 3.
            }
        }
    }

    /**
     * Flip over face-up cards so that they are all face down in the same
     * order.
     * Throws EmptyPileException if the face-down pile is not empty.
     */
    public void restartPile() throws EmptyPileException {
        if (!facedown.isEmpty())
            throw new EmptyPileException("Face-down pile is not empty");

        while (!faceup.isEmpty())
            facedown.push(faceup.pop());
    }
}
