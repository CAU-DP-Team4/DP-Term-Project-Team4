package game.mode;

import game.Game;
import game.gameStates.PlayState;
import game.entities.ghosts.Ghost;

public interface ModeStrategy {

    

    // 게임 시작/초기화
    void onStart(Game game, PlayState playState);
    // 팩맨-유령 충돌 처리
    void onCollision(Game game, PlayState playState, Ghost g);
    // 리스폰 시 추가 동작
    void onRespawn(Game game, PlayState playState);
}