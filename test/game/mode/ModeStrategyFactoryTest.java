package game.mode;

import game.GameMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModeStrategyFactoryTest {

	@Test
	@DisplayName("CLASSIC 모드는 ClassicModeStrategy를 반환한다")
	void testReturnsClassicModeStrategy() {
		ModeStrategy strategy = ModeStrategyFactory.getStrategy(GameMode.CLASSIC);

		assertNotNull(strategy);
		assertTrue(strategy instanceof ClassicModeStrategy);
	}

	@Test
	@DisplayName("LIVES 모드는 기본 목숨 3의 LivesModeStrategy를 반환한다")
	void testReturnsLivesModeStrategyWithDefaultLives() {
		ModeStrategy strategy = ModeStrategyFactory.getStrategy(GameMode.LIVES);

		assertNotNull(strategy);
		assertTrue(strategy instanceof LivesModeStrategy);
		assertEquals(3, ((LivesModeStrategy) strategy).getLives());
	}
}
