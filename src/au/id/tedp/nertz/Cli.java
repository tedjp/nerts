package au.id.tedp.nertz;

import java.lang.String;

public class Cli {
    public static void main(String args[]) {
        Deck deck = new Deck();
        deck.shuffle();
        System.out.println(deck.toString());
    }
}
