package au.id.tedp.nertz;

import java.lang.Integer;
import java.util.ArrayList;

class ScoreKeeper {
    private HumanPlayer human;
    private ArrayList<Deck> decks;
    private ArrayList<AiPlayer> cpus;

    private int humanScore;
    private ArrayList<Integer> cpuScores;

    public ScoreKeeper(ArrayList<Deck> decks, HumanPlayer human, ArrayList<AiPlayer> cpus) {
        this.human = human;
        this.decks = decks;
        this.cpus = cpus;
        newGame();
    }

    public static int NERTZ_CARD_POINTS = 2;
    public static int LAKE_CARD_POINTS = 1;

    public void newGame() {
        int initialScore = 0 - NertzPile.INITIAL_DEAL * NERTZ_CARD_POINTS;
        this.humanScore = initialScore;
        this.cpuScores = new ArrayList<Integer>(2);
        this.cpuScores.add(new Integer(initialScore));
        this.cpuScores.add(new Integer(initialScore));
    }

    public void newRound(int nertzcards) {
        int points = nertzcards * NERTZ_CARD_POINTS;
        this.humanScore -= points;
        for (Integer score: cpuScores) {
            score -= points;
        }
    }

    public int getHumanScore() {
        return humanScore;
    }

    public ArrayList<Integer> getAiScoreList() {
        return cpuScores;
    }

    public int[] getAiScores() {
        int scores[] = new int[cpuScores.size()];
        for (int i = 0; i < cpuScores.size(); ++i)
            scores[i] = cpuScores.get(i).intValue();
        return scores;
    }

    protected Player getPlayerForDeck(Deck d) {
        if (d == decks.get(0))
            return human;

        for (int ai = 0; ai < cpus.size(); ++ai) {
            if (d == decks.get(ai + 1))
                return cpus.get(ai);
        }
        // XXX: Throw exception
        return null;
    }

    private void score(Player p, int score) {
        if (p == human)
            humanScore += score;
        else {
            for (int ai = 0; ai < cpus.size(); ++ai) {
                if (cpus.get(ai) == p) {
                    Integer oldScore = cpuScores.get(ai);
                    cpuScores.set(ai, new Integer(oldScore.intValue() + score));
                    break;
                }
            }
        }
    }

    public void cardRemovedFromNertzPile(Card c) {
        Player p = getPlayerForDeck(c.getDeck());
        if (p == null)
            return;

        // Since cards in the nertz pile score negative, this is positive
        score(p, NERTZ_CARD_POINTS);
    }

    public void cardMovedToLake(Card c) {
        Player p = getPlayerForDeck(c.getDeck());
        if (p == null)
            return;

        score(p, LAKE_CARD_POINTS);
    }

    public void assignScores(int humanScore, ArrayList<Integer> aiScores) {
        this.humanScore = humanScore;
        this.cpuScores = aiScores;
    }

    public void registerMove(Pile from, Pile to, Card c) {
        if (from instanceof NertzPile)
            cardRemovedFromNertzPile(c);

        if (to instanceof SequentialSuitPile)
            cardMovedToLake(c);
    }
}
