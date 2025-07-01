package dataAccess;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class FileDataAccess implements IDataAccess {

    @Override
    public List<String> loadWords(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
    }

    @Override
    public Map<String, Integer> loadRanking(String filePath) throws IOException {
        Map<String, Integer> ranking = new LinkedHashMap<>();
        Path p = Paths.get(filePath);
        if (!Files.exists(p)) return ranking;

        for (String line : Files.readAllLines(p, StandardCharsets.UTF_8)) {
            String[] parts = line.split(",");
            if (parts.length == 2)
                ranking.put(parts[0], Integer.parseInt(parts[1]));
        }
        return ranking;
    }

    @Override
    public void saveRanking(Map<String, Integer> ranking, String filePath) throws IOException {
        Path p = Paths.get(filePath);
        try (BufferedWriter w = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, Integer> e : ranking.entrySet()) {
                w.write(e.getKey() + "," + e.getValue());
                w.newLine();
            }
        }
    }
}