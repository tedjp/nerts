package au.id.tedp.nertz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import java.util.Map;

public class DeckGraphics {
    public static BitmapDrawable getBitmapDrawable(Resources res, Card c) {
        int v = c.getFace().getValue();
        Card.Suit s = c.getSuit();

        // This is a pretty bad way to do it, but it works.

        if (s == Card.Suit.HEARTS) {
            switch (v) {
                case 1:
                    return (BitmapDrawable) res.getDrawable(R.drawable.h1);
                case 2:
                    return (BitmapDrawable) res.getDrawable(R.drawable.h2);
                case 3:
                    return (BitmapDrawable) res.getDrawable(R.drawable.h3);
                case 4:
                    return (BitmapDrawable) res.getDrawable(R.drawable.h4);
                case 5:
                    return (BitmapDrawable) res.getDrawable(R.drawable.h5);
                case 6:
                    return (BitmapDrawable) res.getDrawable(R.drawable.h6);
                case 7:
                    return (BitmapDrawable) res.getDrawable(R.drawable.h7);
                case 8:
                    return (BitmapDrawable) res.getDrawable(R.drawable.h8);
                case 9:
                    return (BitmapDrawable) res.getDrawable(R.drawable.h9);
                case 10:
                    return (BitmapDrawable) res.getDrawable(R.drawable.h10);
                case 11:
                    return (BitmapDrawable) res.getDrawable(R.drawable.hj);
                case 12:
                    return (BitmapDrawable) res.getDrawable(R.drawable.hq);
                case 13:
                    return (BitmapDrawable) res.getDrawable(R.drawable.hk);
            }
        } else if (s == Card.Suit.DIAMONDS) {
            switch (v) {
                case 1:
                    return (BitmapDrawable) res.getDrawable(R.drawable.d1);
                case 2:
                    return (BitmapDrawable) res.getDrawable(R.drawable.d2);
                case 3:
                    return (BitmapDrawable) res.getDrawable(R.drawable.d3);
                case 4:
                    return (BitmapDrawable) res.getDrawable(R.drawable.d4);
                case 5:
                    return (BitmapDrawable) res.getDrawable(R.drawable.d5);
                case 6:
                    return (BitmapDrawable) res.getDrawable(R.drawable.d6);
                case 7:
                    return (BitmapDrawable) res.getDrawable(R.drawable.d7);
                case 8:
                    return (BitmapDrawable) res.getDrawable(R.drawable.d8);
                case 9:
                    return (BitmapDrawable) res.getDrawable(R.drawable.d9);
                case 10:
                    return (BitmapDrawable) res.getDrawable(R.drawable.d10);
                case 11:
                    return (BitmapDrawable) res.getDrawable(R.drawable.dj);
                case 12:
                    return (BitmapDrawable) res.getDrawable(R.drawable.dq);
                case 13:
                    return (BitmapDrawable) res.getDrawable(R.drawable.dk);
            }
        } else if (s == Card.Suit.CLUBS) {
            switch (v) {
                case 1:
                    return (BitmapDrawable) res.getDrawable(R.drawable.c1);
                case 2:
                    return (BitmapDrawable) res.getDrawable(R.drawable.c2);
                case 3:
                    return (BitmapDrawable) res.getDrawable(R.drawable.c3);
                case 4:
                    return (BitmapDrawable) res.getDrawable(R.drawable.c4);
                case 5:
                    return (BitmapDrawable) res.getDrawable(R.drawable.c5);
                case 6:
                    return (BitmapDrawable) res.getDrawable(R.drawable.c6);
                case 7:
                    return (BitmapDrawable) res.getDrawable(R.drawable.c7);
                case 8:
                    return (BitmapDrawable) res.getDrawable(R.drawable.c8);
                case 9:
                    return (BitmapDrawable) res.getDrawable(R.drawable.c9);
                case 10:
                    return (BitmapDrawable) res.getDrawable(R.drawable.c10);
                case 11:
                    return (BitmapDrawable) res.getDrawable(R.drawable.cj);
                case 12:
                    return (BitmapDrawable) res.getDrawable(R.drawable.cq);
                case 13:
                    return (BitmapDrawable) res.getDrawable(R.drawable.ck);
            }
        } else {
            switch (v) {
                case 1:
                    return (BitmapDrawable) res.getDrawable(R.drawable.s1);
                case 2:
                    return (BitmapDrawable) res.getDrawable(R.drawable.s2);
                case 3:
                    return (BitmapDrawable) res.getDrawable(R.drawable.s3);
                case 4:
                    return (BitmapDrawable) res.getDrawable(R.drawable.s4);
                case 5:
                    return (BitmapDrawable) res.getDrawable(R.drawable.s5);
                case 6:
                    return (BitmapDrawable) res.getDrawable(R.drawable.s6);
                case 7:
                    return (BitmapDrawable) res.getDrawable(R.drawable.s7);
                case 8:
                    return (BitmapDrawable) res.getDrawable(R.drawable.s8);
                case 9:
                    return (BitmapDrawable) res.getDrawable(R.drawable.s9);
                case 10:
                    return (BitmapDrawable) res.getDrawable(R.drawable.s10);
                case 11:
                    return (BitmapDrawable) res.getDrawable(R.drawable.sj);
                case 12:
                    return (BitmapDrawable) res.getDrawable(R.drawable.sq);
                case 13:
                    return (BitmapDrawable) res.getDrawable(R.drawable.sk);
            }
        }
        // XXX: throw exception
        return null;
    }
}
