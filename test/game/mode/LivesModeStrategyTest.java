package game.mode;

import game.Game;
import game.gameStates.GameOverState;
import game.gameStates.PlayState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LivesModeStrategyTest {

	@Test
	@DisplayName("onStart는 기본 목숨을 설정함")
	void testOnStartInitializesLivesAndUpdatesUi() {
		Game game = mock(Game.class);
		PlayState playState = mock(PlayState.class);
		LivesModeStrategy strategy = new LivesModeStrategy(5);

		strategy.onStart(game, playState);

		assertEquals(5, strategy.getLives());
        verify(playState).updateModeUI(any());
        verify(playState, never()).resetPositions();
        verify(game, never()).setState(any());
	}

	@Test
	@DisplayName("충돌 시 목숨이 남아 있으면 감소 후 위치를 리셋한다")
	void testOnCollisionWithRemainingLives() {
		Game game = mock(Game.class);
		PlayState playState = mock(PlayState.class);
		LivesModeStrategy strategy = new LivesModeStrategy(3);
		strategy.setLives(2);

		strategy.onCollision(game, playState, null);

		assertEquals(1, strategy.getLives());
		verify(playState).updateModeUI(any());
		verify(playState).resetPositions();
		verify(game, never()).setState(any());
	}

	@Test
	@DisplayName("마지막 목숨에서 충돌하면 GameOverState로 전환한다")
	void testOnCollisionTriggersGameOverWhenNoLivesLeft() {
		Game game = mock(Game.class);
		PlayState playState = mock(PlayState.class);
		LivesModeStrategy strategy = new LivesModeStrategy(1);
		strategy.setLives(1);

		strategy.onCollision(game, playState, null);

		assertEquals(0, strategy.getLives());
		verify(playState).updateModeUI(any());
		verify(playState, never()).resetPositions();
		
        // 메서드에 전달된 인자 캡처해서 검증
		ArgumentCaptor<GameOverState> captor = ArgumentCaptor.forClass(GameOverState.class);
		verify(game).setState(captor.capture());
		assertNotNull(captor.getValue());
		assertTrue(captor.getValue() instanceof GameOverState);
	}
}
