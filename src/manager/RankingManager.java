package manager;

import dataAccess.IDataAccess;
import model.RankingEntry;

import java.io.IOException;
import java.util.*;
import java.util.stream.*;

public class RankingManager {
    private Map<String, Integer> rankings = new LinkedHashMap<>();
    private IDataAccess da;
    private String filePath;

    public RankingManager(IDataAccess da, String filePath) {
        this.da = da;
        this.filePath = filePath;
    }

    public void loadRanking() throws IOException {
        rankings = new LinkedHashMap<>(da.loadRanking(filePath));
    }

    public void updateRanking(String name, int score) {
        rankings.put(name, Math.max(rankings.getOrDefault(name, 0), score));
    }

    public List<RankingEntry> getTopRankings(int n) {
        return rankings.entrySet()
                       .stream()
                       .sorted(Map.Entry.<String,Integer>comparingByValue(Comparator.reverseOrder()))
                       .limit(n)
                       .map(e -> new RankingEntry(e.getKey(), e.getValue()))
                       .collect(Collectors.toList());
    }

    public void saveAll() throws IOException {
        da.saveRanking(rankings, filePath);
    }
}