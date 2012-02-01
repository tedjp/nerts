package au.id.tedp.nertz;

import java.util.ArrayList;

class LiveMove extends CardMove {
    private ArrayList<Card> cards;

    public LiveMove(Pile from, TargetPile to, ArrayList<Card> liveCards) {
        super(from, to);
        this.cards = liveCards;
    }

    @Override
    public void execute() throws InvalidMoveException {
        try {
            for (Card card: cards)
                dest.push(card);
        } catch (CardSequenceException e) {
            throw new InvalidMoveException(e.getMessage());
        }
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
