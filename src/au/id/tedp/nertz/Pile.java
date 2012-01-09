package au.id.tedp.nertz;

import java.util.Stack;
import java.util.EmptyStackException;

public class Pile {
    private Stack<Card> facedown, faceup;

    public Pile() {
        facedown = new Stack<Card>();
        faceup = new Stack<Card>();
    }

    public boolean empty() {
        return (facedown.empty() && faceup.empty());
    }

    public void flipTopCard() throws EmptyStackException {
        faceup.push(facedown.pop());
    }
}
