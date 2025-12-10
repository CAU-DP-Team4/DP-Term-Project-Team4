package game.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager extends UnicastRemoteObject implements ScoreService {
    private List<ScoreEntry> scores;
    private static ScoreManager instance;

    // 점수 저장을 위한 내부 클래스
    private class ScoreEntry implements Comparable<ScoreEntry> {
        String name;
        int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public int compareTo(ScoreEntry o) {
            return o.score - this.score; // 내림차순 정렬
        }
    }

    private ScoreManager() throws RemoteException {
        super();
        scores = new ArrayList<>();
    }

    private static class SingletonHolder {
        private static final ScoreManager INSTANCE;

        static {
            try {
                INSTANCE = new ScoreManager();
            } catch (RemoteException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    public static ScoreManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public synchronized void addScore(String playerName, int score) throws RemoteException {
        scores.add(new ScoreEntry(playerName, score));
        Collections.sort(scores);
        // 상위 10개만 유지
        if (scores.size() > 10) {
            scores.subList(10, scores.size()).clear();
        }
        System.out.println("[Leaderboard] New Score Added: " + playerName + " - " + score);
    }

    @Override
    public synchronized List<String> getTopScores() throws RemoteException {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry entry = scores.get(i);
            result.add((i + 1) + ". " + entry.name + " : " + entry.score);
        }
        return result;
    }
}
