package au.id.tedp.nertz;

import java.lang.String;
import java.lang.StringBuilder;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;

public class Deck extends java.lang.Object {
    private String name;
    private ArrayList<Card> cards;

    public Deck(String name) {
        this.name = name;
        cards = new ArrayList<Card>(52);
        for (int suit = 1; suit <= 4; ++suit) {
            for (int face = 1; face <= 13; ++face) {
                cards.add(new Card(suit, face));
            }
        }
    }

    public void shuffle() {
        Random r = new Random();

        ArrayList<Card> newDeck = new ArrayList<Card>(cards.size());

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
