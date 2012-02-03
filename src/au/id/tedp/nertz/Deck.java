package au.id.tedp.nertz;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Collection;
import java.util.Random;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class Deck {
    private List<Card> cards;
    private int cardnum;

    public Deck(int num) {
        cards = new LinkedList<Card>();
        for (Card.Suit suit: Card.Suit.values()) {
            for (Card.Face face: Card.Face.values()) {
                cards.add(new Card(num, suit, face));
            }
        }

        cardnum = 0;
    }

    public void shuffle() {
        Random r = new Random();

        List<Card> newDeck = new LinkedList<Card>();

        while (!cards.isEmpty()) {
            int cardnum = r.nextInt(cards.size());
            newDeck.add(cards.remove(cardnum));
        }
        this.cards = newDeck;

        cardnum = 0;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < cards.size(); ++i) {
            str.append(cards.get(i).toString());
            if (i < cards.size() - 1)
                str.append("\n");
        }
        return str.toString();
    }

    public Card dealCard() {
        return cards.get(cardnum++);
    }

    public Collection<Card> dealCards(int num) {
        Collection<Card> deal = cards.subList(cardnum, cardnum + num);
        cardnum += num;
        return deal;
    }

    public Collection<Card> dealRemaining() {
        Collection<Card> deal = cards.subList(cardnum, cards.size());
        return deal;
    }
}
