package au.id.tedp.nertz;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Collection;
import java.util.EmptyStackException;

public class Stream extends Pile implements Parcelable {
    private int cardsAtStart;

    public Stream(Collection<Card> cards) {
        /* Since the stream can have all its cards either face down or
         * face up, provide enough storage for both cases. */
        super(cards, 35);
        commonConstructor();
    }

    private void commonConstructor() {
        // Pretend there was one more card so we don't show the shift
        // message at the start of the game.
        cardsAtStart = size() + 1;
    }

    public void flipThree() throws EmptyPileException {
        if (faceup.isEmpty()) // Reset counter now that the pile has started
            cardsAtStart = size();

        for (int i = 0; i < 3; ++i) {
            if (!facedown.isEmpty()) {
                faceup.push(facedown.pop());
            }
            else {
                if (i == 0)
                    throw new EmptyPileException("Stream's face-down pile is empty");
                // It doesn't matter if there are no more face-down cards
                // for card 2 or card 3.
            }
        }
    }

    /**
     * Flip over face-up cards so that they are all face down in the same
     * order.
     * Throws EmptyPileException if the face-down pile is not empty.
     */
    public void restartPile() throws EmptyPileException {
        if (!facedown.isEmpty())
            throw new EmptyPileException("Face-down pile is not empty");

        while (!faceup.isEmpty())
            facedown.push(faceup.pop());
    }

    public void putTopUnder() throws EmptyPileException, CardSequenceException {
        if (!faceup.isEmpty())
            throw new CardSequenceException("Tried to put top card under but there are face-up cards");

        try {
            facedown.insertElementAt(facedown.pop(), 0);
            // Avoid the caller thinking the top card can still be put under
            // since that would be cheating
            cardsAtStart = size() + 1;
        }
        catch (EmptyStackException e) {
            throw new EmptyPileException("No face-down cards to take from");
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new EmptyPileException("Failed to insert card at position 0");
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        super.writeToParcel(p, flags);
    }

    private Stream(Parcel p) {
        super(p);
        commonConstructor();
    }

    public static final Parcelable.Creator<Stream> CREATOR = new Parcelable.Creator<Stream>() {
        public Stream createFromParcel(Parcel in) {
            return new Stream(in);
        }

        public Stream[] newArray(int size) {
            return new Stream[size];
        }
    };

    public boolean cardsTakenThisTimeThrough() {
        return size() < cardsAtStart;
    }
}
