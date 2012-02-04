package au.id.tedp.nertz;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.LinkedList;
import java.util.List;

public class Lake extends TargetArea implements Parcelable {
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

    /**
     * Find a suitable pile for the card.
     * If the card is an Ace, a new pile will be created.
     * If no pile is found, null is returned.
     * DO NOT CALL THIS FROM A NON-UI THREAD since it modifies the Lake
     * by adding a new, empty pile.
     */
    public TargetPile findTargetPileOrCreateNew(Card c) {
        TargetPile pile = findTargetPile(c);
        if (pile != null)
            return pile;
        if (c.getFace() == Card.Face.ACE)
            return createEmptyPile();
        return null;
    }

    public GameMove findMoveToLake(Card c, Pile fromPile) {
        TargetPile target = findTargetPile(c);
        if (target != null)
            return new CardMove(fromPile, target);
        if (c.getFace() == Card.Face.ACE)
            return new LakeNewPileMove(this, fromPile);
        return null;
    }

    public int size() {
        return piles.size();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        p.writeTypedList(piles);
    }

    protected Lake(Parcel p) {
        this();
        p.readTypedList(piles, SequentialSuitPile.CREATOR);
    }

    public static final Parcelable.Creator<Lake> CREATOR = new Parcelable.Creator<Lake>() {
        public Lake createFromParcel(Parcel in) {
            return new Lake(in);
        }

        public Lake[] newArray(int size) {
            return new Lake[size];
        }
    };
}
