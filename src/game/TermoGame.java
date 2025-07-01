package game;

import dataAccess.IDataAccess;
import enums.FeedbackTypeEnum;
import model.Feedback;
import model.Guess;

import java.util.*;
import java.text.Normalizer;

public class TermoGame extends Game {
    private static final String ANSI_RESET   = "\u001B[0m";
    private static final String ANSI_GREEN   = "\u001B[32m";  // Cor Correta na posição
    private static final String ANSI_YELLOW  = "\u001B[33m";  // Cor Existe na palavra, posição errada
    private static final String ANSI_GRAY    = "\u001B[90m";  // Cor Não existe na palavra

    private String secretWord;
    private final int maxAttempts = 6;

    public TermoGame(IDataAccess da, String wordsFile, String rankingFile) {
        super("Jogo do Termo", da, wordsFile, rankingFile);
    }

    @Override
    public void playTurn() {
        secretWord = selectRandomWord().toUpperCase();
        System.out.println("Palavra de " + secretWord.length() + " letras carregada. Você tem " + maxAttempts + " tentativas.");

        int attempts = 0;
        while (attempts < maxAttempts) {
            try {

                System.out.print("Tentativa " + (attempts + 1) + ": ");
                String guess = scanner.nextLine().trim().toUpperCase();
                if (guess.length() != secretWordLength()) {
                    System.out.println("A palavra deve ter " + secretWordLength() + " letras. Tente novamente.");
                    continue;
                }
                List<Feedback> fb = evaluateGuess(guess);
                guesses.add(new Guess(guess, fb));

                attempts++;
                if (isCorrect(fb)) {
                    System.out.println("🎉 " + ANSI_GREEN + "Acertou!" + ANSI_RESET);
                    break;
                }
            } finally {
                for (Guess g : guesses) {
                    printFeedback(g.getFeedbackList());
                }
            }
        }

        if (!isCorrect(guesses.get(guesses.size() - 1).getFeedbackList())) {
            System.out.println("Suas tentativas acabaram. Palavra: " + secretWord);
        }
    }

    @Override
    protected int calculateScore() {
        return Math.max(0, (maxAttempts - guesses.size()) * 10);
    }

    private String selectRandomWord() {
        Random rnd = new Random();
        String word;
        do {
            word = wordList.get(rnd.nextInt(wordList.size()));
        } while (word.length() != secretWordLength());
        return word;
    }

    private int secretWordLength() {
        return secretWord != null ? secretWord.length() : 5;
    }

    private List<Feedback> evaluateGuess(String guess) {
        List<Feedback> out = new ArrayList<>();
        String normalizedSecret = Normalizer.normalize(secretWord, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String normalizedGuess = Normalizer.normalize(guess, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        for (int i = 0; i < normalizedSecret.length(); i++) {
            char c = (i < normalizedGuess.length() ? normalizedGuess.charAt(i) : ' ');
            if (c == normalizedSecret.charAt(i)) {
                out.add(new Feedback(c, FeedbackTypeEnum.CORRECT_POSITION));
            } else if (normalizedSecret.indexOf(c) >= 0) {
                out.add(new Feedback(c, FeedbackTypeEnum.WRONG_POSITION));
            } else {
                out.add(new Feedback(c, FeedbackTypeEnum.NOT_IN_WORD));
            }
        }
        return out;
    }

    private boolean isCorrect(List<Feedback> fb) {
        return fb.stream().allMatch(f -> f.getType() == FeedbackTypeEnum.CORRECT_POSITION);
    }

    private void printFeedback(List<Feedback> fb) {
        for (Feedback f : fb) {
            String symbol = " " + f.getLetter() + " ";
            String color;
            switch (f.getType()) {
                case CORRECT_POSITION:
                    color = ANSI_GREEN;
                    break;
                case WRONG_POSITION:
                    color = ANSI_YELLOW;
                    break;
                default:
                    color = ANSI_GRAY;
                    break;
            }
            System.out.print(color + symbol + ANSI_RESET);
        }
        System.out.println();
    }
}
