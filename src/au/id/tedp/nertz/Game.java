package au.id.tedp.nertz;

import android.content.Context;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

class Game {
    private HumanPlayer human;
    private ArrayList<AiPlayer> cpus;
    private Lake lake;

    public static final int AI_PLAYERS = 2;

    public Game(Context ctx, Bundle saved) throws EmptyPileException {
        android.util.Log.d("Nertz", "Game saved bundle " + (saved == null ? "is null" : "is not null"));
        if (saved != null)
            lake = saved.getParcelable("Lake");
        if (lake == null)
            lake = new Lake();
        human = new HumanPlayer(ctx, this, lake, saved != null ? saved.getBundle("HumanPlayer") : null);

        ArrayList<Bundle> aiBundles = null;
        if (saved != null)
            aiBundles = saved.getParcelableArrayList("AiPlayers");

        cpus = new ArrayList<AiPlayer>(AI_PLAYERS);
        for (int i = 0; i < AI_PLAYERS; ++i) {
            Bundle ai_player_bundle = null;
            if (aiBundles != null)
                ai_player_bundle = aiBundles.get(i);
            cpus.add(new AiPlayer(this, lake, ai_player_bundle));
        }

        if (saved == null) {
            human.start();
            for (AiPlayer cpu: cpus)
                cpu.start();
        }
    }

    public void saveState(Bundle b) {
        android.util.Log.d("Nertz", "Saving Game state");
        b.putParcelable("Lake", lake);

        Bundle humanBundle = new Bundle();
        human.writeToBundle(humanBundle);
        b.putBundle("HumanPlayer", humanBundle);

        ArrayList<Bundle> aiBundles = new ArrayList<Bundle>(cpus.size());
        for (int i = 0; i < cpus.size(); ++i) {
            Bundle cpub = new Bundle();
            cpus.get(i).writeToBundle(cpub);
            aiBundles.add(cpub);
        }

        b.putParcelableArrayList("AiPlayers", aiBundles);
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
