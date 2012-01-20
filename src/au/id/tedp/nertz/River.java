package au.id.tedp.nertz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class River extends TargetArea {
    private ArrayList<TableauPile> piles;

    public static final int NUM_PILES = 4;

    /**
     * Construct a river with the given cards.
     */
    public River(Collection<Card> cards) {
        piles = new ArrayList<TableauPile>(cards.size());
        for (Card card: cards)
            piles.add(new TableauPile(card));
    }

    public List<TableauPile> getPiles() {
        return piles;
    }

    public TableauPile get(int n) {
        return piles.get(n);
    }

    public int size() {
        return piles.size();
    }
}
