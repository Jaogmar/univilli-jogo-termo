import dataAccess.FileDataAccess;
import dataAccess.IDataAccess;
import game.Game;
import game.TermoGame;

public class Main {
    public static void main(String[] args) {
        IDataAccess da = new FileDataAccess();
        String wordsFile   = "src/data/br-utf8.txt";
        String rankingFile = "src/data/ranking.txt";
        Game game = new TermoGame(da, wordsFile, rankingFile);
        game.start();
    }
}