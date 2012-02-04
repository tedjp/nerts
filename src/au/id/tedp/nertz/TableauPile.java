package au.id.tedp.nertz;

import android.os.Parcel;
import android.os.Parcelable;

public class TableauPile extends TargetPile implements Parcelable {
    public TableauPile(Card firstCard) {
        super(firstCard);
    }

    public void play(Card c) throws CardSequenceException {
        if (faceup.isEmpty()) {
            faceup.push(c);
        } else {
            Card top = faceup.peek();
            if (!isValidMove(top, c)) {
                throw new CardSequenceException(top, c);
            }
            faceup.push(c);
        }
    }

    private static boolean isValidMove(Card top, Card newCard) {
        return (top.getColor() != newCard.getColor()
                && newCard.getValue() + 1 == top.getValue());
    }

    public boolean isValidMove(Card nextCard) {
        if (!faceup.isEmpty())
            return isValidMove(faceup.peek(), nextCard);
        // Any card from the Nertz pile, waste pile, or another pile
        // is valid.
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        super.writeToParcel(p, flags);
    }

    protected TableauPile(Parcel p) {
        super(p);
    }

    public static final Parcelable.Creator<TableauPile> CREATOR = new Parcelable.Creator<TableauPile>() {
        public TableauPile createFromParcel(Parcel in) {
            return new TableauPile(in);
        }

        public TableauPile[] newArray(int size) {
            return new TableauPile[size];
        }
    };

    public String toString() {
        return "TableauPile";
    }
}
