package game.ghostStrategies;

import game.Game;
import game.GameplayPanel;
import game.entities.Position;
import game.entities.ghosts.Ghost;

//Stratégie concrète de Clyde (le fantôme jaune)
public class ClydeStrategy implements IGhostStrategy{
    private Ghost ghost;
    private final Position SCATTER_TARGET = new Position(0, GameplayPanel.height);

    public ClydeStrategy(Ghost ghost) {
        this.ghost = ghost;
    }

    //Clyde cible directement Pacman s'il est au dela d'un rayon de 8 cases, et sinon il cible sa position de pause
    @Override
    public Position getChaseTargetPosition() {
        var pacman = Game.getPacman();

        if (ghost.distanceTo(pacman) >= 256) {
            return pacman.getPosition();
        }else{
            return getScatterTargetPosition();
        }
    }

    //En pause, Clyde cible la case en bas à gauche
    @Override
    public Position getScatterTargetPosition() {
        return SCATTER_TARGET;
    }
}
