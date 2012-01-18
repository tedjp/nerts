package au.id.tedp.nertz;

import java.util.LinkedList;
import java.util.List;

public class Lake {
    private List<SequentialSuitPile> piles;

    public Lake() {
        piles = new LinkedList<SequentialSuitPile>();
    }

    public List<SequentialSuitPile> getPiles() {
        return piles;
    }

    /**
     * Create a new Pile in the Lake starting with the given Card.
     * @return The new Pile.
     */
    public Pile createPile(Card firstCard) throws CardSequenceException {
        SequentialSuitPile pile = new SequentialSuitPile();
        pile.push(firstCard);
        piles.add(pile);
        return pile;
    }

    /**
     * Create a new Pile in the Lake with no specific suit, but suitable
     * for forCard to be the first card in it.
     * @return The new Pile.
     */
    public Pile createEmptyPile() {
        SequentialSuitPile pile = new SequentialSuitPile();
        piles.add(pile);
        return pile;
    }
}
