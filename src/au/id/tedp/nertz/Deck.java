package au.id.tedp.nertz;

import java.lang.String;
import java.lang.StringBuilder;
import java.util.Random;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new LinkedList<Card>();
        for (Card.Suit suit: Card.Suit.values()) {
            for (Card.Face face: Card.Face.values()) {
                cards.add(new Card(suit, face));
            }
        }
    }

    public void shuffle() {
        Random r = new Random();

        List<Card> newDeck = new LinkedList<Card>();

        while (!cards.isEmpty()) {
            int cardnum = r.nextInt(cards.size());
            newDeck.add(cards.remove(cardnum));
        }
        this.cards = newDeck;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        Iterator<Card> it = cards.iterator();
        while (it.hasNext()) {
            Card card = it.next();
            str.append(card.toString());
            if (it.hasNext())
                str.append("\n");
        }
        return str.toString();
    }
}
