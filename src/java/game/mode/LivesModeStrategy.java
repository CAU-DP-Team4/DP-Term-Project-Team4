package game.mode;

import game.Game;
import game.entities.ghosts.Ghost;
import game.gameStates.GameOverState;
import game.gameStates.PlayState;

public class LivesModeStrategy implements ModeStrategy {
    private int defaultLives;
    private int lives;

    public LivesModeStrategy(int initialLives) {
        this.defaultLives = initialLives;
        this.lives = defaultLives;
    }

    @Override
    public void onCollision(Game game, PlayState playState, Ghost ghost) {
        decreaseLives();
        playState.updateModeUI(p -> p.updateLives(lives));
        if (lives <= 0) {
            game.setState(new GameOverState(game));
        } else {
            // 팩맨과 유령의 위치를 초기화하는 로직 추가
            playState.resetPositions();
        }
        
    }

    @Override
    public void onStart(Game game, PlayState playState) {
        setLives(defaultLives);
        playState.updateModeUI(p -> p.updateLives(lives));
    }

    @Override
    public void onRespawn(Game game, PlayState playState) {
        // nothing to do
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    private void decreaseLives() {
        lives--;
    }
    
}
