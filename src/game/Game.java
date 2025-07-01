package game;

import dataAccess.IDataAccess;
import manager.RankingManager;
import model.Guess;
import model.RankingEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Game implements IPlayable {
    protected String gameName;
    protected List<String> wordList;
    protected List<Guess> guesses;
    protected RankingManager rankingManager;
    protected IDataAccess dataAccess;
    protected Scanner scanner = new Scanner(System.in);

    public Game(String gameName,
                IDataAccess dataAccess,
                String wordsFile,
                String rankingFile) {
        this.gameName = gameName;
        this.dataAccess = dataAccess;
        this.guesses = new ArrayList<>();
        try {
            this.wordList = dataAccess.loadWords(wordsFile);
        } catch (IOException ex) {
            System.err.println("Erro ao carregar palavras: " + ex.getMessage());
            this.wordList = new ArrayList<>();
        }
        this.rankingManager = new RankingManager(dataAccess, rankingFile);
        try {
            rankingManager.loadRanking();
        } catch (IOException ex) {
            System.err.println("Erro ao carregar ranking: " + ex.getMessage());
        }
    }

    @Override
    public void start() {
        System.out.println("=== " + gameName + " ===");
        playTurn();
        end();
    }

    @Override
    public abstract void playTurn();

    @Override
    public void end() {
        System.out.println("\n>> Fim do jogo!");
        int score = calculateScore();
        System.out.println("Sua pontuação: " + score);
        System.out.print("Digite seu nome para o ranking: ");
        String name = scanner.nextLine().trim();
        rankingManager.updateRanking(name, score);
        try {
            rankingManager.saveAll();
        } catch (IOException ex) {
            System.err.println("Erro ao salvar ranking: " + ex.getMessage());
        }
        System.out.println("\n--- Top 10 Ranking ---");
        for (RankingEntry e : rankingManager.getTopRankings(10)) {
            System.out.printf("%s : %d\n", e.getPlayerName(), e.getScore());
        }
    }

    /** Cada jogo define sua lógica de pontuação */
    protected abstract int calculateScore();
}