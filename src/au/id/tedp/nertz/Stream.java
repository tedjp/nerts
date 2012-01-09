package au.id.tedp.nertz;

import java.util.LinkedList;
import java.util.List;

public class Stream extends Pile {
    public void flipThree() throws EmptyPileException {
        for (int i = 0; i < 3; ++i) {
            if (!facedown.isEmpty()) {
                faceup.addFirst(facedown.getFirst());
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