package au.id.tedp.nertz;

import java.util.List;

abstract class TargetArea {
    abstract public List<? extends TargetPile> getPiles();

    public TargetPile findTargetPile(Card card) {
        for (TargetPile p: getPiles()) {
            if (p == null)
                continue;

            if (p.isValidMove(card))
                return p;
        }
        return null;
    }
}
