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
        ACE(1) {
            public String toString() {
                return "Ace";
            }
        },
        TWO(2) {
            public String toString() {
                return "2";
            }
        },
        THREE(3) {
            public String toString() {
                return "3";
            }
        },
        FOUR(4) {
            public String toString() {
                return "4";
            }
        },
        FIVE(5) {
            public String toString() {
                return "5";
            }
        },
        SIX(6) {
            public String toString() {
                return "6";
            }
        },
        SEVEN(7) {
            public String toString() {
                return "7";
            }
        },
        EIGHT(8) {
            public String toString() {
                return "8";
            }
        },
        NINE(9) {
            public String toString() {
                return "9";
            }
        },
        TEN(10) {
            public String toString() {
                return "10";
            }
        },
        JACK(11) {
            public String toString() {
                return "Jack";
            }
        },
        QUEEN(12) {
            public String toString() {
                return "Queen";
            }
        },
        KING(13) {
            public String toString() {
                return "King";
            }
        };

        private int value;
        private Face(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
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
