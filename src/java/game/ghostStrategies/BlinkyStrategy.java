package game.ghostStrategies;

import game.Game;
import game.GameplayPanel;
import game.entities.Position;

//Stratégie concrète de Blinky (le fantôme rouge)
public class BlinkyStrategy implements IGhostStrategy{
    private final Position SCATTER_TARGET = new Position(GameplayPanel.width, 0);

    //Blinky cible directement la position de Pacman
    @Override
    public Position getChaseTargetPosition() {
        var pacman = Game.getPacman();
        return pacman.getPosition();
    }

    //En pause, Blinky cible la case en haut à droite
    @Override
    public Position getScatterTargetPosition() {
        return SCATTER_TARGET;
    }
}
