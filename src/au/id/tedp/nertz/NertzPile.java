package au.id.tedp.nertz;

public class NertzPile extends Pile {
    public NertzPile() {
        super(12, 1);
    }

    public Card takeCard() {
        Card c = faceup.pop();
        faceup.push(facedown.pop());
        return c;
    }
}
