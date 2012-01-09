package au.id.tedp.nertz;

public class Card {
    public enum Suit {
        HEARTS {
            public String toString() {
                return "Hearts";
            }
        },
        DIAMONDS {
            public String toString() {
                return "Diamonds";
            }
        },
        CLUBS {
            public String toString() {
                return "Clubs";
            }
        },
        SPADES {
            public String toString() {
                return "Spades";
            }
        }
    };

    public enum Face {
        ACE {
            public String toString() {
                return "Ace";
            }
        },
        TWO {
            public String toString() {
                return "2";
            }
        },
        THREE {
            public String toString() {
                return "3";
            }
        },
        FOUR {
            public String toString() {
                return "4";
            }
        },
        FIVE {
            public String toString() {
                return "5";
            }
        },
        SIX {
            public String toString() {
                return "6";
            }
        },
        SEVEN {
            public String toString() {
                return "7";
            }
        },
        EIGHT {
            public String toString() {
                return "8";
            }
        },
        NINE {
            public String toString() {
                return "9";
            }
        },
        TEN {
            public String toString() {
                return "10";
            }
        },
        JACK {
            public String toString() {
                return "Jack";
            }
        },
        QUEEN {
            public String toString() {
                return "Queen";
            }
        },
        KING {
            public String toString() {
                return "King";
            }
        },
    };

    private Suit suit;
    private Face face;

    public Card(Suit suit, Face face) {
        this.suit = suit;
        this.face = face;
    }

    public java.lang.String toString() {
        return face + " of " + suit;
    }

    //public boolean equals
}
