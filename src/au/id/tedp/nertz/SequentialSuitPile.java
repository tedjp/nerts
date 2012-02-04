package au.id.tedp.nertz;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that represents a pile of cards of a single suit that must be
 * sequential, starting from Ace through King.
 */

public class SequentialSuitPile extends TargetPile implements Parcelable {
    private Card.Suit suit;

    public SequentialSuitPile() {
        super(0, 13);
        suit = null;
    }

    public boolean isComplete() {
        if (faceup.isEmpty())
            return false;
        return (faceup.peek().getFace() == Card.Face.KING);
    }

    public synchronized void push(Card card) throws CardSequenceException {
        if (faceup.isEmpty()) {
            // Only accept Aces
            if (!isValidMove(card)) {
                throw new CardSequenceException(card);
            }
            suit = card.getSuit();
            faceup.push(card);
        } else {
            if (!isValidMove(card)) {
                throw new CardSequenceException(card, faceup.peek());
            }
            faceup.push(card);
        }
    }

    public boolean isValidMove(Card c) {
        if (isEmpty())
            return (c.getFace() == Card.Face.ACE);

        return (c.getSuit() == this.suit
                && c.getValue() == faceup.peek().getValue() + 1);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        super.writeToParcel(p, flags);
        p.writeString(suit.toString());
    }

    protected SequentialSuitPile(Parcel p) {
        super(p);
        suit = Card.Suit.fromString(p.readString());
    }

    public static final Parcelable.Creator<SequentialSuitPile> CREATOR = new Parcelable.Creator<SequentialSuitPile>() {
        public SequentialSuitPile createFromParcel(Parcel in) {
            return new SequentialSuitPile(in);
        }

        public SequentialSuitPile[] newArray(int size) {
            return new SequentialSuitPile[size];
        }
    };

    public String toString() {
        return String.format("SequentialSuitPile with %s on top",
                faceup.isEmpty() ? "nothing" : faceup.peek().toString());
    }
}
