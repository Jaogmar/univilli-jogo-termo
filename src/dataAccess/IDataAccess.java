package dataAccess;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IDataAccess {
    List<String> loadWords(String filePath) throws IOException;
    Map<String, Integer> loadRanking(String filePath) throws IOException;
    void saveRanking(Map<String, Integer> ranking, String filePath) throws IOException;
}