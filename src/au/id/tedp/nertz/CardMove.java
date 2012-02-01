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
            dest.push(source.pop());
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
}
