package game.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    @DisplayName("생성자와 Getter가 올바르게 작동해야 한다")
    void testConstructorAndGetters() {
        Position pos = new Position(10, 20);

        assertEquals(10, pos.getX());
        assertEquals(20, pos.getY());
    }

    @Test
    @DisplayName("set 메서드는 객체의 좌표를 직접 변경해야 한다")
    void testSet() {
        Position pos = new Position(0, 0);
        pos.set(50, 100);

        assertEquals(50, pos.getX());
        assertEquals(100, pos.getY());
    }

    @Test
    @DisplayName("translate 메서드는 현재 좌표에서 상대적으로 이동해야 한다")
    void testTranslate() {
        Position pos = new Position(10, 10);

        // x는 5 증가, y는 3 감소
        pos.translate(5, -3);

        assertEquals(15, pos.getX());
        assertEquals(7, pos.getY());
    }

    @Test
    @DisplayName("plus 메서드는 원본을 변경하지 않고 새로운 Position 객체를 반환해야 한다")
    void testPlus() {
        Position original = new Position(10, 10);
        Position newPos = original.plus(5, 5);

        // 1. 값 검증
        assertEquals(15, newPos.getX());
        assertEquals(15, newPos.getY());

        // 2. 원본 객체 불변성 검증 (원본은 그대로여야 함)
        assertEquals(10, original.getX());
        assertEquals(10, original.getY());

        // 3. 객체 참조값 비교 (서로 다른 객체여야 함)
        assertNotSame(original, newPos);
    }

    @Test
    @DisplayName("distanceTo는 오프셋을 포함하여 거리를 정확히 계산해야 한다")
    void testDistanceTo() {
        // 상황: 현재 (0,0), 타겟 (3,4)
        // 오프셋이 (0,0)일 때 -> 거리는 5 (피타고라스 3:4:5)
        Position start = new Position(0, 0);
        Position target = new Position(3, 4);

        double distance = start.distanceTo(target, 0, 0);
        assertEquals(5.0, distance, 0.0001); // 부동소수점 비교

        // 상황: 현재 (0,0)이지만 오프셋으로 (1,1)만큼 이동했다고 가정.
        // 가상 위치 (1,1)에서 타겟 (4,5)까지의 거리 -> x차이 3, y차이 4 -> 거리 5
        Position targetWithOffset = new Position(4, 5);
        double distanceWithOffset = start.distanceTo(targetWithOffset, 1, 1);
        assertEquals(5.0, distanceWithOffset, 0.0001);
    }

    @Test
    @DisplayName("move 메서드는 각도와 거리에 따라 새로운 위치를 반환해야 한다")
    void testMove() {
        Position start = new Position(0, 0);

        // 1. 0도(오른쪽)로 10만큼 이동
        Position movedRight = start.move(10, 0);
        assertEquals(10, movedRight.getX());
        assertEquals(0, movedRight.getY());

        // 2. 90도(PI/2, 아래쪽/위쪽 - 좌표계에 따라 다름)로 10만큼 이동
        // Math.sin(PI/2) = 1, Math.cos(PI/2) ≒ 0
        Position movedVertical = start.move(10, Math.PI / 2);
        assertEquals(0, movedVertical.getX());
        assertEquals(10, movedVertical.getY());

        // 3. 소수점 버림 확인 (int 캐스팅)
        // 45도 방향, 거리 10 -> x, y는 약 7.07 -> int 변환 시 7
        Position movedDiagonal = start.move(10, Math.PI / 4);
        assertEquals(7, movedDiagonal.getX());
        assertEquals(7, movedDiagonal.getY());
    }

    @Test
    @DisplayName("reflectAround는 기준점(pivot)을 중심으로 대칭 이동한 위치를 반환해야 한다 (Inky 전략)")
    void testReflectAround() {
        // 내 위치: (0, 0)
        // 기준점(Pivot): (2, 2)
        // 벡터(Pivot - 나): (2, 2)
        // 결과(Pivot + 벡터): (4, 4)
        Position me = new Position(0, 0);
        Position pivot = new Position(2, 2);

        Position reflected = me.reflectAround(pivot);

        assertEquals(4, reflected.getX());
        assertEquals(4, reflected.getY());

        // 다른 케이스: 수평 대칭
        // 내 위치: (10, 5)
        // 기준점: (10, 10)
        // 벡터: (0, 5)
        // 결과: (10, 15)
        Position me2 = new Position(10, 5);
        Position pivot2 = new Position(10, 10);

        Position reflected2 = me2.reflectAround(pivot2);

        assertEquals(10, reflected2.getX());
        assertEquals(15, reflected2.getY());
    }
}