package au.id.tedp.nertz;

class StreamMove implements GameMove {
    public enum Type {
        RESTART_PILE,
        FLIP_THREE,
        TOP_UNDER
    };

    private Stream stream;
    private Type type;

    public StreamMove(Stream stream, Type moveType) {
        this.stream = stream;
        this.type = moveType;
    }

    public void execute() throws InvalidMoveException {
        try {
            switch (type) {
            case RESTART_PILE:
                stream.restartPile();
                break;
            case FLIP_THREE:
                stream.flipThree();
                break;
            case TOP_UNDER:
                stream.putTopUnder();
                break;
            }
        }
        catch (EmptyPileException e) {
            throw new InvalidMoveException(e.getMessage());
        }
        catch (CardSequenceException e) {
            throw new InvalidMoveException(e.getMessage());
        }
    }

    public String toString() {
        return type.toString();
    }
}
