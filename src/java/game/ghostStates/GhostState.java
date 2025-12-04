package game.ghostStates;

import game.entities.Position;
import game.entities.ghosts.Ghost;
import game.utils.WallCollisionDetector;

//Classe abstraite pour décrire les différents états de fantômes
public abstract class GhostState {
    protected Ghost ghost;

    public GhostState(Ghost ghost) {
        this.ghost = ghost;
    }

    //Différentes transitions possibles d'un état vers un autre
    public void superPacGumEaten() {}
    public void timerModeOver() {}
    public void timerFrightenedModeOver() {}
    public void eaten() {}
    public void outsideHouse() {}
    public void insideHouse() {}

    public Position getTargetPosition() {
        return new Position(0, 0);
    }

    //Méthode pour calculer la prochaine direction que le fantôme va prendre
    public void computeNextDir() {
        int newXSpd = 0;
        int newYSpd = 0;

        if (!ghost.onTheGrid()) return; //Le fantôme doit être sur une "case" de la zone de jeu
        if (!ghost.onGameplayWindow()) return;  //Le fantôme doit être dans la zone de jeu

        double minDist = Double.MAX_VALUE; //distance minimale courante

        // 계산에 필요한 변수 미리 추출
        Position currentPos = ghost.getPosition();
        Position targetPos = getTargetPosition();
        int speed = ghost.getSpd();

        // LEFT
        if (ghost.getXSpd() <= 0 && !WallCollisionDetector.checkWallCollision(ghost, -speed, 0)) {
            double distance = currentPos.distanceTo(targetPos, -speed, 0);

            if (distance < minDist) {
                newXSpd = -speed;
                newYSpd = 0;
                minDist = distance;
            }
        }

        // RIGHT
        if (ghost.getXSpd() >= 0 && !WallCollisionDetector.checkWallCollision(ghost, speed, 0)) {
            double distance = currentPos.distanceTo(targetPos, speed, 0);

            if (distance < minDist) {
                newXSpd = speed;
                newYSpd = 0;
                minDist = distance;
            }
        }

        // UP
        if (ghost.getYSpd() <= 0 && !WallCollisionDetector.checkWallCollision(ghost, 0, -speed)) {
            double distance = currentPos.distanceTo(targetPos, 0, -speed);

            if (distance < minDist) {
                newXSpd = 0;
                newYSpd = -speed;
                minDist = distance;
            }
        }

        // DOWN
        if (ghost.getYSpd() >= 0 && !WallCollisionDetector.checkWallCollision(ghost, 0, speed)) {
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
