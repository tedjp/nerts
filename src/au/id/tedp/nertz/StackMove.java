package au.id.tedp.nertz;

import java.util.Stack;

class StackMove implements GameMove {
    public StackMove(River river, TableauPile source, TableauPile dest) {
        this.river = river;
        this.source = source;
        this.dest = dest;
    }

    private TableauPile source, dest;
    private River river;

    public void execute() throws InvalidMoveException {
        try {
            int end = source.size();
            Stack<Card> cards = source.getFaceUpCards();
            for (int i = 0; i < end; ++i)
                dest.push(cards.remove(0));
        }
        catch (CardSequenceException e) {
            throw new InvalidMoveException(e.getMessage());
        }
    }

    public String toString() {
        return String.format("Move %s on top of %s", source.toString(),
                dest.toString());
    }
}
