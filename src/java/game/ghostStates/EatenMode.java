package game.ghostStates;

import game.entities.Position;
import game.entities.ghosts.Ghost;
import game.utils.WallCollisionDetector;

//Classe pour décrire l'état concret d'un fantôme mangé par Pacman
public class EatenMode extends GhostState {
    private final Position HOUSE_CENTER_TARGET = new Position(208, 200);

    public EatenMode(Ghost ghost) {
        super(ghost);
    }

    //Transition lorsque le fantôme retourne dans sa maison
    @Override
    public void insideHouse() {
        ghost.switchHouseMode();
    }

    //Dans cet état, la position ciblée est une case au milieu de la maison des fantômes
    @Override
    public Position getTargetPosition() {
        return HOUSE_CENTER_TARGET;
    }

    //Même chose que la méthode de la classe abstraite, mais on ignore ici les collisions avec les murs de la maison des fantômes
    @Override
    public void computeNextDir() {
        int newXSpd = 0;
        int newYSpd = 0;

        if (!ghost.onTheGrid() || !ghost.onGameplayWindow()) return;

        double minDist = Double.MAX_VALUE;

        Position currentPos = ghost.getPosition();
        Position targetPos = getTargetPosition();
        int speed = ghost.getSpd();

        // LEFT
        if (ghost.getXSpd() <= 0 && !WallCollisionDetector.checkWallCollision(ghost, -speed, 0, true)) {
            double distance = currentPos.distanceTo(targetPos, -speed, 0);

            if (distance < minDist) {
                newXSpd = -speed;
                newYSpd = 0;
                minDist = distance;
            }
        }

        // RIGHT
        if (ghost.getXSpd() >= 0 && !WallCollisionDetector.checkWallCollision(ghost, speed, 0, true)) {
            double distance = currentPos.distanceTo(targetPos, speed, 0);

            if (distance < minDist) {
                newXSpd = speed;
                newYSpd = 0;
                minDist = distance;
            }
        }

        // UP
        if (ghost.getYSpd() <= 0 && !WallCollisionDetector.checkWallCollision(ghost, 0, -speed, true)) {
            double distance = currentPos.distanceTo(targetPos, 0, -speed);

            if (distance < minDist) {
                newXSpd = 0;
                newYSpd = -speed;
                minDist = distance;
            }
        }

        // DOWN
        if (ghost.getYSpd() >= 0 && !WallCollisionDetector.checkWallCollision(ghost, 0, speed, true)) {
            double distance = currentPos.distanceTo(targetPos, 0, speed);

            if (distance < minDist) {
                newXSpd = 0;
                newYSpd = speed;
                minDist = distance;
            }
        }

        if (newXSpd == 0 && newYSpd == 0) return;

        ghost.setXSpd(newXSpd);
        ghost.setYSpd(newYSpd);
    }
}
