package game.ghostStates;

import game.entities.Position;
import game.entities.ghosts.Ghost;
import game.utils.Utils;

//Classe pour décrire l'état concret d'un fantôme effrayé (après que Pacman ait mangé une SuperPacGum)
public class FrightenedMode extends GhostState {

    public FrightenedMode(Ghost ghost) {
        super(ghost);
    }

    //Transition lorsque le fantôme est mangé
    @Override
    public void eaten() {
        ghost.switchEatenMode();
    }

    //Transition lorsque le timer d'état effrayé est terminé
    @Override
    public void timerFrightenedModeOver() {
        ghost.switchChaseModeOrScatterMode();
    }

    //Dans cet état, la position ciblée est une case aléatoire autour du fantôme
    @Override
    public Position getTargetPosition() {
        // 랜덤 로직을 통해 이동할 오프셋 계산
        Position currentPos = ghost.getPosition();
        boolean isXAxis = Utils.randomBool();

        int randomOffset = Utils.randomInt(-1, 1) * 32;
        int dx = isXAxis ? randomOffset : 0;
        int dy = !isXAxis ? randomOffset : 0;

        return currentPos.plus(dx, dy);
    }
}