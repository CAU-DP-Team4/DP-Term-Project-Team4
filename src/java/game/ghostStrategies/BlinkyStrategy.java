package game.ghostStrategies;

import game.Game;
import game.GameplayPanel;
import game.entities.Entity;

//Stratégie concrète de Blinky (le fantôme rouge)
public class BlinkyStrategy implements IGhostStrategy{
    private final int[] SCATTER_TARGET = new int[]{GameplayPanel.width, 0};

    //Blinky cible directement la position de Pacman
    @Override
    public int[] getChaseTargetPosition() {
        Entity pacman = Game.getPacman();
        return pacman.getPosition();
    }

    //En pause, Blinky cible la case en haut à droite
    @Override
    public int[] getScatterTargetPosition() {
        return SCATTER_TARGET;
    }
}
