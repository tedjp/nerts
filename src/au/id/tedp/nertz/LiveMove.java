package au.id.tedp.nertz;

import java.util.ArrayList;

class LiveMove extends Move {
    private ArrayList<Card> cards;

    public LiveMove(Pile from, TargetPile to, ArrayList<Card> liveCards) {
        super(from, to);
        this.cards = liveCards;
    }

    @Override
    public void execute() throws CardSequenceException {
        for (Card card: cards)
            dest.push(card);
    }

    /**
     * Don't really want anyone calling this since its single Card is not well
     * defined. In some cases the caller just wants the bottom (first) card.
     */
    @Override
    public Card getCard() {
        return cards.get(0);
    }
}
