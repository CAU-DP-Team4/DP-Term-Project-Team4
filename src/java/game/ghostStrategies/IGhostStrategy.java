package game.ghostStrategies;

import game.entities.Position;

//Interface pour décrire les stratégies des différents fantômes (cette vidéo les explique bien : https://www.youtube.com/watch?v=ataGotQ7ir8)
public interface IGhostStrategy {
    Position getChaseTargetPosition(); //Case ciblée lorsque le fantôme poursuit Pacman
    Position getScatterTargetPosition(); //Case ciblée lorsque le fantôme fait une pause
}
