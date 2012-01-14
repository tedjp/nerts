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

    public enum Color {
        BLACK, RED
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

    private Deck deck;
    private Suit suit;
    private Face face;

    public Card(Deck deck, Suit suit, Face face) {
        this.deck = deck;
        this.suit = suit;
        this.face = face;
    }

    public java.lang.String toString() {
        return face + " of " + suit;
    }

    public Suit getSuit() {
        return suit;
    }

    public Color getColor() {
        if (suit == Suit.HEARTS || suit == Suit.DIAMONDS)
            return Color.RED;
        return Color.BLACK;
    }

    public Face getFace() {
        return face;
    }

    public int getValue() {
        return face.getValue();
    }

    public Deck getDeck() {
        return deck;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + suit.ordinal();
        result = 31 * result + face.ordinal();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Card))
            return false;

        Card c = (Card) o;

        return face == c.face && suit == c.suit;
    }
}
