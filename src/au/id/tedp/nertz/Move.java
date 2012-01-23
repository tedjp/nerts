package au.id.tedp.nertz;

class Move {
    public Move(Pile source, TargetPile dest) {
        this.source = source;
        this.dest = dest;
    }

    public Pile source;
    public TargetPile dest;

    public void execute() throws EmptyPileException, CardSequenceException {
        dest.push(source.pop());
    }

    public Card getCard() {
        return source.peek();
    }
}
