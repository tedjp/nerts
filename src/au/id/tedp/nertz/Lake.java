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
}
