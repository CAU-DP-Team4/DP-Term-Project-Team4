package game.entities.ghosts;

import game.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GhostEmpowermentTest {

    @Test
    @DisplayName("유령이 10초(600프레임) 후 강화 상태로 변해야 한다")
    void testGhostEmpowerment() {
        // 1. 테스트용 유령 생성 (Blinky 사용)
        Blinky blinky = new Blinky(0, 0);

        // 유령은 처음에 HouseMode이므로 추격을 위해 ChaseMode로 전환해야 함
        blinky.switchChaseMode();

        // 2. 초기 상태 확인 (기본 속도 배율은 1이어야 함)
        Assertions.assertEquals(1, blinky.getSpeedMultiplier(), "초기 속도 배율은 1이어야 합니다.");

        // 3. 강제로 게임 입력을 활성화 (Ghost.update() 내부 조건 통과용)
        Game.setFirstInput(true);

        // 4. 10초 직전까지 (599프레임) 시뮬레이션
        for (int i = 0; i < 600; i++) {
            blinky.update();
        }

        // 5. 강화 상태 확인 (속도 배율이 1로 유지되어야 함)
        Assertions.assertEquals(1, blinky.getSpeedMultiplier(),
                "10초 경과 전 속도 배율이 1로 유지되어야 합니다.");
        Assertions.assertEquals(1, blinky.getScoreMultiplier(),
                "10초 경과 전 점수 배율이 1로 유지되어야 합니다.");

        // 6. 10초 도달 (600프레임)
        blinky.update();

        // 7. 강화 상태 확인 (속도 배율이 2로 증가해야 함)
        Assertions.assertEquals(2, blinky.getSpeedMultiplier(),
                "10초 경과 후 속도 배율이 2(강화 상태)가 되어야 합니다.");
        Assertions.assertEquals(2, blinky.getScoreMultiplier(),
                "10초 경과 후 점수 배율이 2(강화 상태)가 되어야 합니다.");
    }
}
