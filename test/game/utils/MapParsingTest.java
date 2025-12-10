package game.utils;

import game.entities.Entity;
import game.entities.Wall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

public class MapParsingTest {

    @Test
    @DisplayName("CsvReader가 레벨 파일을 정상적으로 파싱해야 한다")
    void testCsvReader() {
        CsvReader reader = new CsvReader();
        try {
            // 실제 리소스 파일 경로 가져오기
            URI uri = getClass().getClassLoader().getResource("level/level.csv").toURI();
            List<List<String>> data = reader.parseCsv(uri);

            // level.csv의 구조 검증
            Assertions.assertNotNull(data, "파싱된 데이터는 null이 아니어야 합니다.");
            Assertions.assertFalse(data.isEmpty(), "데이터가 비어있지 않아야 합니다.");

            // 첫 줄의 첫 글자가 'x' (벽) 인지 확인 (level.csv 내용 기반)
            String firstCell = data.get(0).get(0);
            Assertions.assertEquals("x", firstCell, "맵의 (0,0)은 벽('x')이어야 합니다.");

        } catch (Exception e) {
            Assertions.fail("CSV 파싱 중 오류 발생: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("EntityFactory 람다식이 정상적으로 객체를 생성해야 한다")
    void testEntityFactoryLambda() {
        // PlayState 내부의 팩토리 로직을 시뮬레이션
        EntityFactory wallFactory = (x, y) -> new Wall(x, y);

        Entity wall = wallFactory.create(10, 20);

        Assertions.assertTrue(wall instanceof Wall, "생성된 객체는 Wall 타입이어야 합니다.");
        Assertions.assertEquals(10, wall.getXPos());
        Assertions.assertEquals(20, wall.getYPos());
    }
}