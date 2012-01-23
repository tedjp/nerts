package au.id.tedp.nertz;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

class Game {
    private HumanPlayer human;
    private AiPlayer computer;

    public Game(Context ctx) {
        human = new HumanPlayer(ctx, this);
        computer = new AiPlayer(this);

        /* Begin */
        try {
            Lake lake = new Lake();
            human.setup(lake);
            computer.setup(lake);

            human.start();
            computer.start();
        }
        catch (EmptyPileException e) {
            // TODO: Why would p.setup() ever throw this?
        }
    }

    public void notifyOfMove(Player source, Move move) {
        if (source == human)
            computer.notifyOfMove(move);
        else if (source == computer)
            human.notifyOfMove(move);
    }

    public HumanPlayer getHumanPlayer() {
        return human;
    }
}
