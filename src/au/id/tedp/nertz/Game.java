package au.id.tedp.nertz;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

class Game {
    private HumanPlayer human;
    private ArrayList<AiPlayer> cpus;
    private Lake lake;
    private ScoreKeeper scoreKeeper;
    private GameMove[] pendingAiMoves;
    private AiMoveTask aiMoveTask;
    private Main activity;

    public static final int AI_PLAYERS = 2;

    public Game(Main activity, Bundle saved) throws EmptyPileException {
        this.activity = activity;
        Context ctx = activity;
        android.util.Log.d("Nertz", "Game saved bundle " + (saved == null ? "is null" : "is not null"));
        // Only used on initial deal, not on resume
        ArrayList<Deck> decks = null;

        if (saved == null) {
            decks = new ArrayList<Deck>(AI_PLAYERS + 1);
            for (int i = 0; i < AI_PLAYERS + 1; ++i) {
                Deck d = new Deck(i);
                d.shuffle();
                decks.add(d);
            }
        }

        // Only used on resume, not initial deal
        ArrayList<Bundle> aiBundles = null;

        if (saved != null) {
            lake = saved.getParcelable("Lake");
            human = new HumanPlayer(ctx, this, lake, saved.getBundle("HumanPlayer"));
            aiBundles = saved.getParcelableArrayList("AiPlayers");
        } else {
            lake = new Lake();
            human = new HumanPlayer(ctx, this, lake, decks.get(0));
        }

        cpus = new ArrayList<AiPlayer>(AI_PLAYERS);
        for (int i = 0; i < AI_PLAYERS; ++i) {
            Bundle ai_player_bundle = null;
            if (aiBundles != null) {
                ai_player_bundle = aiBundles.get(i);
                cpus.add(new AiPlayer(this, lake, ai_player_bundle));
            } else {
                cpus.add(new AiPlayer(String.format("CPU %d", i + 1), this, lake, decks.get(i + 1)));
            }
        }

        scoreKeeper = new ScoreKeeper(human, cpus);
        if (saved != null) {
            int humanScore = saved.getInt("PlayerScore");
            ArrayList<Integer> aiScores = saved.getIntegerArrayList("AiScores");
            scoreKeeper.assignScores(humanScore, aiScores);
        }

        if (saved == null) {
            human.start();
            for (AiPlayer cpu: cpus)
                cpu.start();
        }
    }

    public void onResume() {
        aiMoveTask = null;
        findAiMoves();
    }

    public void onPause() {
        if (aiMoveTask != null)
            aiMoveTask.cancel(true);
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

        b.putInt("PlayerScore", scoreKeeper.getHumanScore());
        b.putIntegerArrayList("AiScores", scoreKeeper.getAiScoreList());
    }

    private class AiMoveTask extends AsyncTask<AiPlayer, Void, GameMove[]> {
        private AiPlayer ai;

        @Override
        protected GameMove[] doInBackground(AiPlayer... players) {
            GameMove[] moves = new GameMove[players.length];
            for (int i = 0; i < players.length; ++i) {
                if (isCancelled())
                    return moves;

                AiPlayer player = players[i];
                GameMove move = player.findMove();
                if (move != null) {
                    moves[i] = move;
                    Log.d("Nertz", "Player " + player.toString() + " found move " +
                            move.toString());
                }
            }

            return moves;
        }

        @Override
        protected void onPostExecute(GameMove moves[]) {
            pendingAiMoves = moves;
            aiMoveTask = null;
        }

        @Override
        protected void onCancelled(GameMove[] moves) {
            pendingAiMoves = moves;
            aiMoveTask = null;
        }
    }

    private void findAiMoves() {
        // Only start a new finder task if there isn't already running.
        // Leave the existing one running to give it the best chance of
        // actually finding a move, even if its move is too late to be
        // executed. It's better than thrashing move-finder tasks that
        // never complete.
        if (aiMoveTask == null) {
            aiMoveTask = new AiMoveTask();
            aiMoveTask.execute(cpus.toArray(new AiPlayer[cpus.size()]));
        } else {
            Log.d("Nertz", "AiMoveTask is still running, letting it finish");
        }
    }

    public void declareWinner(Player winner) {
        activity.declareWinner(winner);
    }

    public boolean checkForWinner() {
        if (human.getNertzPile().isEmpty()) {
            declareWinner(human);
            return true;
        }
        for (AiPlayer ai: cpus) {
            if (ai.getNertzPile().isEmpty()) {
                declareWinner(ai);
                return true;
            }
        }
        return false;
    }

    public void onPlayerMove() {
        if (checkForWinner())
            return;

        if (pendingAiMoves != null) {
            for (int i = 0; i < pendingAiMoves.length; ++i) {
                GameMove move = pendingAiMoves[i];
                if (move != null) {
                    try {
                        move.execute();
                        if (checkForWinner()) {
                            human.getGameView().invalidate();
                            return;
                        }
                    }
                    catch (InvalidMoveException e) {
                        // This is a normal condition, when the AI was planning a
                        // move that was blocked by the user.
                        android.util.Log.d("Nertz", "Failed to execute AI move: " + e.getMessage());
                    }
                }
            }
        }
        findAiMoves();
    }

    public void onAiMove() {
        human.onAiMove();
    }

    public HumanPlayer getHumanPlayer() {
        return human;
    }

    public int getPlayerScore() {
        return scoreKeeper.getHumanScore();
    }

    public int[] getAiScores() {
        return scoreKeeper.getAiScores();
    }

    public ScoreKeeper getScoreKeeper() {
        return scoreKeeper;
    }
}
