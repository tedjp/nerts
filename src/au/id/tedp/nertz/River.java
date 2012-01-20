package au.id.tedp.nertz;

import java.util.ArrayList;
import java.util.List;

class River extends TargetArea {
    private ArrayList<TableauPile> piles;

    /**
     * Construct a river with 4 empty TableauPiles.
     */
    public River() {
        piles = new ArrayList<TableauPile>(4);
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
