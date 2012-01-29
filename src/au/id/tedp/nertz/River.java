package au.id.tedp.nertz;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class River extends TargetArea implements Parcelable {
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

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        p.writeTypedList(piles);
    }

    protected River(Parcel p) {
        piles = new ArrayList<TableauPile>(NUM_PILES);
        p.readTypedList(piles, TableauPile.CREATOR);
    }

    public static final Parcelable.Creator<River> CREATOR = new Parcelable.Creator<River>() {
        public River createFromParcel(Parcel in) {
            return new River(in);
        }

        public River[] newArray(int size) {
            return new River[size];
        }
    };
}
