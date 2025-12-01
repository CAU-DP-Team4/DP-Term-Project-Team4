package game.utils;

import game.entities.Entity;

@FunctionalInterface
public interface EntityFactory {
    Entity create(int x, int y);
}
