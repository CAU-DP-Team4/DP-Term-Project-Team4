package game.ghostStrategies;

import game.Game;
import game.entities.Position;
import game.utils.Utils;

//Stratégie concrète de Pinky (le fantôme rose)
public class PinkyStrategy implements IGhostStrategy {
    private final Position SCATTER_TARGET = new Position(0, 0);

    //Pinky cible deux cases devant de Pacman
    @Override
    public Position getChaseTargetPosition() {
        int[] pacmanFacingPosition = Utils.getPointDistanceDirection(Game.getPacman().getXPos(), Game.getPacman().getYPos(), 64, Utils.directionConverter(Game.getPacman().getDirection()));

        return new Position(pacmanFacingPosition[0], pacmanFacingPosition[1]);
    }

    //En pause, Pinky cible la case en haut à gauche
    @Override
    public Position getScatterTargetPosition() {
        return SCATTER_TARGET;
    }
}
