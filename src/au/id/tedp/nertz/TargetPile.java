package au.id.tedp.nertz;

import android.os.Parcel;

public abstract class TargetPile extends Pile {
    public TargetPile(Card c) {
        super(c);
    }

    public TargetPile(int faceDownCapacity, int faceUpCapacity) {
        super(faceDownCapacity, faceUpCapacity);
    }

    public TargetPile(Parcel p) {
        super(p);
    }

    public abstract boolean isValidMove(Card card);
}
