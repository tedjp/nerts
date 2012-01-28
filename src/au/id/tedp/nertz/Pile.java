package au.id.tedp.nertz;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Collection;

public class Pile implements Parcelable {
    protected Stack<Card> facedown, faceup;
    protected String name;

    private final int DEFAULT_DECKSIZE = 13;

    public Pile() {
        this.facedown = new Stack<Card>();
        this.faceup = new Stack<Card>();
    }

    public Pile(Collection<Card> facedown, Collection<Card> faceup) {
        this.facedown = new Stack<Card>();
        this.facedown.addAll(facedown);
        this.faceup = new Stack<Card>();
        this.faceup.addAll(faceup);
    }

    public Pile(Collection<Card> facedown, int faceUpCapacity) {
        this.facedown = new Stack<Card>();
        this.facedown.addAll(facedown);
        this.faceup = new Stack<Card>();
    }

    public Pile(Card faceup) {
        // Starting with a single face-up card means we'll probably never add
        // any face-down cards.
        this.facedown = new Stack<Card>();
        this.faceup = new Stack<Card>();
        this.faceup.push(faceup);
    }

    public Pile(int faceDownCapacity, int faceUpCapacity) {
        facedown = new Stack<Card>();
        faceup = new Stack<Card>();
    }

    public boolean isEmpty() {
        return (facedown.isEmpty() && faceup.isEmpty());
    }

    /**
     * Take the top face-down card and put it face-up.
     * Throws an exception if there is already a card face-up on top of
     * the pile.
     */
    public void flipTopCard() throws EmptyPileException {
        if (!faceup.isEmpty())
            throw new EmptyPileException("Cannot flip top card: top card is already face-up");

        faceup.push(facedown.pop());
    }

    public BitmapDrawable topCardImage(Resources res) {
        if (!faceup.isEmpty()) {
            Card topCard = faceup.peek();
            return DeckGraphics.getBitmapDrawable(res, topCard);
        }
        if (!facedown.isEmpty()) {
            return DeckGraphics.getCardBack(res);
        }
        return null;
    }

    public Card pop() throws EmptyPileException {
        if (!faceup.isEmpty())
            return faceup.pop();

        throw new EmptyPileException("Pile is empty");
    }

    public boolean isFaceDownEmpty() {
        return facedown.isEmpty();
    }

    public boolean isFaceUpEmpty() {
        return faceup.isEmpty();
    }

    public void push(Card c) throws CardSequenceException {
        faceup.push(c);
    }

    public Stack<Card> getFaceUpCards() {
        return faceup;
    }

    public Card peek() {
        try {
            return faceup.peek();
        }
        catch (EmptyStackException e) {
            return null;
        }
    }

    public int size() {
        return facedown.size() + faceup.size();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        p.writeTypedList(facedown);
        p.writeTypedList(faceup);
        p.writeString(name);
    }

    protected Pile(Parcel p) {
        p.readTypedList(facedown, Card.CREATOR);
        p.readTypedList(faceup, Card.CREATOR);
        name = p.readString();
    }

    public static final Parcelable.Creator<Pile> CREATOR = new Parcelable.Creator<Pile>() {
        public Pile createFromParcel(Parcel in) {
            return new Pile(in);
        }

        public Pile[] newArray(int size) {
            return new Pile[size];
        }
    };
}
