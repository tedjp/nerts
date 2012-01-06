package au.id.tedp.nertz;

public class Card extends Object {
    /* Suits:
     * 1: Hearts
     * 2: Diamonds
     * 3: Clubs
     * 4: Spades
     */
    private int suit;
    /* Faces:
     * 1: Ace
     * 2-10: Numeric
     * 11: Jack
     * 12: Queen
     * 13: King
     */
    private int face;

    public Card(int suit, int face) {
        /* TODO: Throw exception on invalid values */
        this.suit = suit;
        this.face = face;
    }

    public java.lang.String toString() {
        String suitstr = null, facestr;
        if (suit == 1)
            suitstr = "Hearts";
        else if (suit == 2)
            suitstr = "Diamonds";
        else if (suit == 3)
            suitstr = "Clubs";
        else if (suit == 4)
            suitstr = "Spades";

        if (face == 1)
            facestr = "Ace";
        else if (face >= 2 && face <= 10)
            facestr = String.valueOf(face);
        else if (face == 11)
            facestr = "Jack";
        else if (face == 12)
            facestr = "Queen";
        else if (face == 13)
            facestr = "King";
        else
            facestr = null;

        if (face == 14)
            return "Joker";

        return facestr + " of " + suitstr;
    }

    //public boolean equals
}
