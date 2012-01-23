package au.id.tedp.nertz;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

class Game {
    private HumanPlayer human;
    private AiPlayer computer;

    public Game(Context ctx) {
        human = new HumanPlayer(ctx);
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

    public HumanPlayer getHumanPlayer() {
        return human;
    }
}
