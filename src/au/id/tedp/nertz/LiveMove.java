package au.id.tedp.nertz;

class LiveMove extends Move {
    private Card card;

    public LiveMove(Pile from, TargetPile to, Card liveCard) {
        super(from, to);
        this.card = liveCard;
    }

    @Override
    public void execute() throws CardSequenceException {
        dest.push(card);
    }

    @Override
    public Card getCard() {
        return card;
    }
}
