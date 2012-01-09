package au.id.tedp.nertz;

import java.util.ArrayDeque;
import java.util.Deque;

public class Pile {
    protected Deque<Card> facedown, faceup;

    public Pile() {
        facedown = new ArrayDeque<Card>(13);
        faceup = new ArrayDeque<Card>(13);
    }

    public Pile(int faceDownCapacity, int faceUpCapacity) {
        facedown = new ArrayDeque<Card>(faceDownCapacity);
        faceup = new ArrayDeque<Card>(faceUpCapacity);
    }

    public boolean isEmpty() {
        return (facedown.isEmpty() && faceup.isEmpty());
    }
}
