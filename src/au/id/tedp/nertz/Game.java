package au.id.tedp.nertz;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

class Game {
    private HumanPlayer human;
    private ArrayList<AiPlayer> cpus;

    public static final int AI_PLAYERS = 3;

    public Game(Context ctx) {
        human = new HumanPlayer(ctx, this);
        cpus = new ArrayList<AiPlayer>(AI_PLAYERS);
        for (int i = 0; i < AI_PLAYERS; ++i)
            cpus.add(new AiPlayer(this));

        /* Begin */
        try {
            Lake lake = new Lake();
            human.setup(lake);
            for (AiPlayer cpu: cpus)
                cpu.setup(lake);

            human.start();
            for (AiPlayer cpu: cpus)
                cpu.start();
        }
        catch (EmptyPileException e) {
            // TODO: Why would p.setup() ever throw this?
        }
    }

    public void onPlayerMove() {
        for (AiPlayer cpu: cpus) {
            cpu.makeMove();
        }
    }

    public void onAiMove() {
        human.onAiMove();
    }

    public HumanPlayer getHumanPlayer() {
        return human;
    }
}
