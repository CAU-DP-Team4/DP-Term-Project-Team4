package game.ghostStrategies;

import game.Game;
import game.GameplayPanel;
import game.entities.Position;
import game.entities.ghosts.Ghost;
import game.utils.Utils;

//Stratégie concrète d'Inky (le fantôme bleu)
public class InkyStrategy implements IGhostStrategy{
    private Ghost otherGhost;
    private final Position SCATTER_TARGET = new Position(GameplayPanel.width, GameplayPanel.height);

    public InkyStrategy(Ghost ghost) {
        this.otherGhost = ghost;
    }

    @Override
    public Position getChaseTargetPosition() {
        var pacman = Game.getPacman();
        Position pacmanPos = pacman.getPosition();
        Position blinkyPos = otherGhost.getPosition();
        double pacmanDir = Utils.directionConverter(pacman.getDirection());

        // Pivot 포인트 계산 (팩맨 위치에서 32만큼 앞선 곳)
        Position pivotPoint = pacmanPos.move(32.0, pacmanDir);

        // 최종 타겟 계산 (Blinky 위치를 Pivot 기준으로 반전)
        return blinkyPos.reflectAround(pivotPoint);
    }

    //En pause, Inky cible la case en bas à droite
    @Override
    public Position getScatterTargetPosition() {
        return SCATTER_TARGET;
    }
}
