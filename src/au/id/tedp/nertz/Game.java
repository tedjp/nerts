package au.id.tedp.nertz;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import java.util.ArrayList;
import java.util.List;

class Game {
    private HumanPlayer human;
    private ArrayList<AiPlayer> cpus;
    private Lake lake;
    private ScoreKeeper scoreKeeper;
    private AiMoveTask aiMoveTask;
    private Main activity;
    // Whether cards may be played
    private boolean isInProgress = false;

    public static final int AI_PLAYERS = 2;

    public Game(Main activity, Bundle saved, int savedPlayerScore, Pair<String,String> opponentNames) throws EmptyPileException {
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

        if (opponentNames == null)
            opponentNames = OpponentNames.getPair();

        if (saved != null) {
            String nameArray[] = saved.getStringArray("AiNames");
            opponentNames = new Pair<String,String>(nameArray[0], nameArray[1]);
        }

        cpus = new ArrayList<AiPlayer>(AI_PLAYERS);
        for (int i = 0; i < AI_PLAYERS; ++i) {
            Bundle ai_player_bundle = null;
            if (aiBundles != null) {
                ai_player_bundle = aiBundles.get(i);
                cpus.add(new AiPlayer(this, lake, ai_player_bundle));
            } else {
                cpus.add(new AiPlayer(i == 0 ? opponentNames.first : opponentNames.second,
                            this, lake, decks.get(i + 1)));
            }
        }

        scoreKeeper = new ScoreKeeper(human, cpus);
        if (saved != null) {
            int humanScore = saved.getInt("PlayerScore");
            ArrayList<Integer> aiScores = saved.getIntegerArrayList("AiScores");
            scoreKeeper.assignScores(humanScore, aiScores);
        } else {
            scoreKeeper.assignHumanScore(savedPlayerScore);
            scoreKeeper.newRound(NertzPile.INITIAL_DEAL);
        }

        if (saved == null) {
            human.start();
            for (AiPlayer cpu: cpus)
                cpu.start();
        }

        isInProgress = true;

        if (saved != null)
            checkForWinner();
    }

    public void onResume() {
        aiMoveTask = null;
        //findAiMoves();
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
        String aiNameArray[] = new String[cpus.size()];
        for (int i = 0; i < cpus.size(); ++i)
            aiNameArray[i] = cpus.get(i).getName();
        b.putStringArray("AiNames", aiNameArray);
    }

    private class AiMoveTask extends AsyncTask<Integer, Void, GameMove> {
        private AiPlayer aiPlayer;

        @Override
        protected GameMove doInBackground(Integer... playernums) {
            if (playernums.length == 0)
                return null;

            aiPlayer = Game.this.cpus.get(playernums[0].intValue());
            return aiPlayer.findMove();
        }

        @Override
        protected void onPostExecute(GameMove move) {
            try {
                playMove(aiPlayer, move);
                if (checkIfPlayerWon(aiPlayer)) {
                    aiMoveTask = null;
                    return;
                }
            }
            catch (InvalidMoveException e) {
                Log.e("Nertz", aiPlayer.getName() + " failed to play " + move.toString());
            }

            if (aiPlayer == cpus.get(cpus.size() - 1))
                aiMoveTask = null;
            else {
                aiMoveTask = new AiMoveTask();
                aiMoveTask.execute(cpus.indexOf(aiPlayer) + 1);
            }
        }

        @Override
        protected void onCancelled(GameMove move) {
            // XXX: save calculated move
            aiMoveTask = null;
        }
    }

    private void checkForWinner() {
        if (checkIfPlayerWon(human))
            return;

        for (AiPlayer cpu: cpus) {
            if (checkIfPlayerWon(cpu))
                return;
        }
    }

    public void playMove(Player player, GameMove move) throws InvalidMoveException {
        if (!isInProgress)
            return;
        move.execute();
        scoreKeeper.registerMove(player, move);
        // Only redraw if the player was human or, if the player was AI,
        // the Lake changed.
        if (player == human || (move instanceof LakeNewPileMove) ||
                (move instanceof CardMove &&
                 ((CardMove)move).dest instanceof SequentialSuitPile)) {
            Game.this.human.getGameView().fullInvalidate();
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
            aiMoveTask.execute(new Integer(0));
        } else {
            Log.d("Nertz", "AiMoveTask is still running, letting it finish");
        }
    }

    public void declareWinner(Player winner) {
        activity.declareWinner(winner);
    }

    public boolean checkIfPlayerWon(Player player) {
        if (player.getNertzPile().isEmpty()) {
            isInProgress = false;
            declareWinner(player);
            return true;
        }
        return false;
    }

    public void onPlayerMove() {
        if (checkIfPlayerWon(human))
            return;
        findAiMoves();
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

    public boolean isGameInProgress() {
        return isInProgress;
    }

    public Pair<String,String> getOpponentNames() {
        return new Pair<String,String>(cpus.get(0).getName(), cpus.get(1).getName());
    }
}
