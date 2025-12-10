package game.rmi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.rmi.RemoteException;
import java.util.List;

public class ScoreManagerTest {

    @Test
    @DisplayName("점수 추가 및 정렬, 상위 10개 유지 로직 테스트")
    void testAddScoreAndRanking() throws RemoteException {
        ScoreManager manager = ScoreManager.getInstance();

        // 다양한 점수 무작위 추가
        manager.addScore("PlayerA", 100);
        manager.addScore("PlayerB", 500);
        manager.addScore("PlayerC", 300);

        // 11개 이상의 데이터를 넣어 상위 10개 유지 기능 테스트
        for (int i = 0; i < 15; i++) {
            manager.addScore("Dummy" + i, 10); // 낮은 점수
        }

        // 검증
        List<String> topScores = manager.getTopScores();

        // 1. 반환된 리스트 크기는 10개여야 함
        Assertions.assertEquals(10, topScores.size(), "리더보드는 상위 10개만 유지해야 합니다.");

        // 2. 1등은 500점(PlayerB)이어야 함
        String firstPlace = topScores.get(0);
        Assertions.assertTrue(firstPlace.contains("PlayerB") && firstPlace.contains("500"),
                "1등은 500점인 PlayerB여야 합니다.");

        // 3. 2등은 300점(PlayerC)이어야 함
        String secondPlace = topScores.get(1);
        Assertions.assertTrue(secondPlace.contains("PlayerC") && secondPlace.contains("300"),
                "2등은 300점인 PlayerC여야 합니다.");
    }
}