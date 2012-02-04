package au.id.tedp.nertz;

class CardMove implements GameMove {
    public CardMove(Pile source, TargetPile dest) {
        this.source = source;
        this.dest = dest;
    }

    public Pile source;
    public TargetPile dest;

    public void execute() throws InvalidMoveException {
        try {
            Card card = source.pop();
            if (source instanceof NertzPile && !source.isFaceDownEmpty()) {
                try {
                    // XXX: If the push fails then the pop and this flip need to be undone
                    source.flipTopCard();
                } catch (EmptyPileException e) {}
            }
            dest.push(card);
        }
        catch (EmptyPileException e) {
            throw new InvalidMoveException(e.getMessage());
        }
        catch (CardSequenceException e) {
            throw new InvalidMoveException(e.getMessage());
        }
    }

    public Card getCard() {
        return source.peek();
    }

    public String toString() {
        return String.format("Move card %s from %s to %s",
                source.peek().toString(), source.toString(), dest.toString());
    }
}
