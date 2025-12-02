package game.ghostStrategies;

import game.Game;
import game.utils.Utils;

//Stratégie concrète de Pinky (le fantôme rose)
public class PinkyStrategy implements IGhostStrategy {
    private final int[] SCATTER_TARGET = new int[]{0, 0};

    //Pinky cible deux cases devant de Pacman
    @Override
    public int[] getChaseTargetPosition() {
        int[] position = new int[2];
        int[] pacmanFacingPosition = Utils.getPointDistanceDirection(Game.getPacman().getxPos(), Game.getPacman().getyPos(), 64, Utils.directionConverter(Game.getPacman().getDirection()));
        position[0] = pacmanFacingPosition[0];
        position[1] = pacmanFacingPosition[1];
        return position;
    }

    //En pause, Pinky cible la case en haut à gauche
    @Override
    public int[] getScatterTargetPosition() {
        return SCATTER_TARGET;
    }
}
