package au.id.tedp.nertz;

import java.lang.Exception;

public class CardSequenceException extends Exception {
    public CardSequenceException(String detailMessage) {
        super(detailMessage);
    }

    public CardSequenceException(Card top, Card newCard) {
        super("Cannot play " + newCard + " on " + top);
    }
}
