package au.id.tedp.nertz;

import java.util.ArrayList;
import java.util.List;

class Game {
    private List<Player> players;

    public Game() {
        players = new ArrayList<Player>(2);

        players.add(new Player("User"));
        players.add(new Player("Computer"));

        /* Begin */
        try {
            Lake lake = new Lake();
            for (Player p : players)
                p.setup(lake);
            for (Player p : players)
                p.start();
        }
        catch (EmptyPileException e) {
            // TODO: Why would p.setup() ever throw this?
        }
    }

    public Player getFirstPlayer() {
        return players.get(0);
    }
}
