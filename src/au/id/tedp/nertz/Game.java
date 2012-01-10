package au.id.tedp.nertz;

import java.util.ArrayList;
import java.util.List;

class Game {
    private List<Player> players;

    public Game() {
        players = new ArrayList<Player>(2);

        players.add(new Player("User"));
        players.add(new Player("Computer"));
    }
}
