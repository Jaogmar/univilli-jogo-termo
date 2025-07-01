package model;

public class RankingEntry {
    private String playerName;
    private int score;

    public RankingEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
}