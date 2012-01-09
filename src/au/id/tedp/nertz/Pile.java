package au.id.tedp.nertz;

import java.util.LinkedList;
import java.util.Deque;

public class Pile {
    protected Deque<Card> facedown, faceup;

    public Pile() {
        facedown = new LinkedList<Card>();
        faceup = new LinkedList<Card>();
    }

    public boolean isEmpty() {
        return (facedown.isEmpty() && faceup.isEmpty());
    }
}
