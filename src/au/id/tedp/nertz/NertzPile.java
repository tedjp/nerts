package au.id.tedp.nertz;

import java.util.Collection;

public class NertzPile extends Pile {
    public static final int INITIAL_DEAL = 13;

    public NertzPile(Collection<Card> cards) {
        super(cards, 1);
    }

    public NertzPile(Pile p) {
        super(p);
    }
}
