package au.id.tedp.nertz;

import java.util.LinkedList;
import java.util.List;

public class Lake extends TargetArea {
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
    public SequentialSuitPile createEmptyPile() {
        SequentialSuitPile pile = new SequentialSuitPile();
        piles.add(pile);
        return pile;
    }

    /** Given a particular card, return the appropriate Lake Pile for it.
     * Returns null if there is no appropriate pile.
     * If an Ace is passed, a new pile will be created.
     */
    @Override
    public TargetPile findTargetPile(Card c) {
        TargetPile pile = super.findTargetPile(c);
        if (pile != null)
            return pile;

        if (c.getFace() == Card.Face.ACE)
            return createEmptyPile();

        return null;
    }

}
