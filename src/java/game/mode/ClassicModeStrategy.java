package game.mode;

import game.Game;
import game.entities.ghosts.Ghost;
import game.gameStates.GameOverState;
import game.gameStates.PlayState;

public class ClassicModeStrategy implements ModeStrategy {

    @Override
    public void onStart(Game game, PlayState playState) {
        // nothing to do
    }

    @Override
    public void onCollision(Game game, PlayState playState, Ghost g) {
        game.setState(new GameOverState(game));
    }

    @Override
    public void onRespawn(Game game, PlayState playState) {
        // nothing to do
    }
    


}
