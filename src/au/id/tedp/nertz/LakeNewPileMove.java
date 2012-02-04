package au.id.tedp.nertz;

public class LakeNewPileMove implements GameMove {
    private Lake lake;
    private Pile fromPile;

    public LakeNewPileMove(Lake lake, Pile fromPile) {
        this.lake = lake;
        this.fromPile = fromPile;
    }

    public void execute() throws InvalidMoveException {
        if (fromPile.peek().getFace() != Card.Face.ACE)
            throw new InvalidMoveException("New Lake pile must start with an Ace");

        Card card;
        try {
            card = fromPile.pop();
            if (fromPile instanceof NertzPile) {
                // XXX: Not sure if this will go back face-up, but not a huge deal if it does.
                fromPile.flipTopCard();
            }
            try {
                lake.createPile(card);
            }
            catch (CardSequenceException e) {
                // Put the card back where it came from
                try {
                    fromPile.push(card);
                } catch (CardSequenceException seqe) {
                    throw new InvalidMoveException("Failed to put card back on its pile, it will probably disappear now. Cause:" + seqe.getMessage());
                }
                throw new InvalidMoveException("Failed to create new pile with "
                        + card.toString() + ": " + e.getMessage());
            }
        } catch (EmptyPileException e) {
            throw new InvalidMoveException("Tried to move card from empty pile");
        }
    }

    public String toString() {
        return String.format("Move %s from top of %s to new Lake pile",
                fromPile.isEmpty() ? "nothing" : fromPile.peek().toString(), fromPile.toString());
    }
}
