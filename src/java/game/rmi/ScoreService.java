package game.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ScoreService extends Remote {
    // 점수 추가
    void addScore(String playerName, int score) throws RemoteException;

    // 상위 랭킹 조회 (문자열 리스트 형태: "이름 : 점수")
    List<String> getTopScores() throws RemoteException;
}
