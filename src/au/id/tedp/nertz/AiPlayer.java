package au.id.tedp.nertz;

class AiPlayer extends Player {
    public AiPlayer() {
        super("CPU");
    }

    private Move findMove() {
        NertzPile np = getNertzPile();
        Lake lake = getLake();
        River river = getRiver();
        TargetPile target;
        Card nc = np.peek();

        if (nc != null) {
            // Nertz -> Lake
            target = lake.findTargetPile(nc);
            if (target != null)
                return new Move(getNertzPile(), target, nc);

            // Nertz -> River
            target = river.findTargetPile(nc);
            if (target != null)
                return new Move(np, target, nc);
        }

        // River -> Lake
        for (Pile riverPile: river.getPiles()) {
            Card riverCard = riverPile.peek();
            if (riverCard != null) {
                target = lake.findTargetPile(riverCard);
                if (target != null)
                    return new Move(riverPile, target, riverCard);
            }
        }

        // Stream -> Lake
        Stream stream = getStream();
        Card sc = stream.peek();
        if (sc != null) {
            target = lake.findTargetPile(sc);
            if (target != null)
                return new Move(stream, target, sc);

            // Stream -> River
            target = river.findTargetPile(sc);
            if (target != null)
                return new Move(stream, target, sc);
        }

        // Try all cards in the river if the cards on top of it
        // were moved to another pile
        // TODO

        return null;
    }
}
