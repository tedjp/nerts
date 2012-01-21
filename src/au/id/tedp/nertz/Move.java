package au.id.tedp.nertz;

class Move {
    public Move(Pile source, TargetPile dest, Card card) {
        this.source = source;
        this.dest = dest;
        this.card = card;
    }

    public Pile source;
    public TargetPile dest;
    public Card card;
}
