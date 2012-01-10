package au.id.tedp.nertz;

import java.lang.String;
import java.lang.StringBuilder;
import java.util.Random;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class Deck {
    private List<Card> cards;
    private Iterator<Card> iterator;
    private Player owner;

    public Deck(Player owner) {
        this.owner = owner;
        cards = new LinkedList<Card>();
        for (Card.Suit suit: Card.Suit.values()) {
            for (Card.Face face: Card.Face.values()) {
                cards.add(new Card(this, suit, face));
            }
        }

        iterator = cards.iterator();
    }

    public void shuffle() {
        Random r = new Random();

        List<Card> newDeck = new LinkedList<Card>();

        while (!cards.isEmpty()) {
            int cardnum = r.nextInt(cards.size());
            newDeck.add(cards.remove(cardnum));
        }
        this.cards = newDeck;

        iterator = cards.iterator();
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

    public Card nextCard() {
        return iterator.next();
    }

    public boolean hasNextCard() {
        return iterator.hasNext();
    }

    public Player getOwner() {
        return owner;
    }
}
