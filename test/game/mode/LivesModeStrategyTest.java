package game.mode;

import game.Game;
import game.gameStates.GameOverState;
import game.gameStates.GameState;
import game.gameStates.PlayState;
import game.UIPanel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class LivesModeStrategyTest {

	@Test
	@DisplayName("onStart는 기본 목숨을 설정하고 UI를 갱신한다")
	void testOnStartInitializesLivesAndUpdatesUi() {
		StubGame game = new StubGame();
		StubPlayState playState = new StubPlayState(game);
		LivesModeStrategy strategy = new LivesModeStrategy(5);

		strategy.onStart(game, playState);

		assertEquals(5, strategy.getLives());
		assertEquals(5, playState.getUi().getLastLives());
		assertFalse(playState.wasResetCalled());
		assertFalse(game.isSetStateCalled());
	}

	@Test
	@DisplayName("충돌 시 목숨이 남아 있으면 감소 후 위치를 리셋한다")
	void testOnCollisionWithRemainingLives() {
		StubGame game = new StubGame();
		StubPlayState playState = new StubPlayState(game);
		LivesModeStrategy strategy = new LivesModeStrategy(3);
		strategy.setLives(2);

		strategy.onCollision(game, playState, null);

		assertEquals(1, strategy.getLives());
		assertEquals(1, playState.getUi().getLastLives());
		assertTrue(playState.wasResetCalled());
		assertFalse(game.isSetStateCalled());
	}

	@Test
	@DisplayName("마지막 목숨에서 충돌하면 GameOverState로 전환한다")
	void testOnCollisionTriggersGameOverWhenNoLivesLeft() {
		StubGame game = new StubGame();
		StubPlayState playState = new StubPlayState(game);
		LivesModeStrategy strategy = new LivesModeStrategy(1);
		strategy.setLives(1);

		strategy.onCollision(game, playState, null);

		assertEquals(0, strategy.getLives());
		assertEquals(0, playState.getUi().getLastLives());
		assertFalse(playState.wasResetCalled());
		assertTrue(game.isSetStateCalled());
		assertTrue(game.getLastState() instanceof GameOverState);
	}

	// --- Test Doubles ---
	private static class StubGame extends Game {
		private GameState lastState;
		private boolean setStateCalled;

		@Override
		public void setState(GameState state) {
			this.lastState = state;
			this.setStateCalled = true;
		}

		public GameState getLastState() {
			return lastState;
		}

		public boolean isSetStateCalled() {
			return setStateCalled;
		}
	}

	private static class StubPlayState extends PlayState {
		private final TestUIPanel ui = new TestUIPanel();
		private boolean resetCalled;

		StubPlayState(Game game) {
			super(game);
		}

		@Override
		public void init() {
			// override to skip heavy initialization
		}

		@Override
		public void resetPositions() {
			this.resetCalled = true;
		}

		@Override
		public void updateModeUI(Consumer<UIPanel> action) {
			if (action != null) {
				action.accept(ui);
			}
		}

		public boolean wasResetCalled() {
			return resetCalled;
		}

		public TestUIPanel getUi() {
			return ui;
		}
	}

	private static class TestUIPanel extends UIPanel {
		private int lastLives = -1;

		TestUIPanel() {
			super(0, 0);
		}

		@Override
		public void updateLives(int lives) {
			this.lastLives = lives;
		}

		public int getLastLives() {
			return lastLives;
		}
	}
}
