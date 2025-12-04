package game.entities;

import java.awt.*;

public abstract class Entity {
    protected int size;
    protected Position position;

    protected boolean destroyed = false;

    public Entity(int size, int xPos, int yPos) {
        this.size = size;
        this.position = new Position(xPos, yPos);
    }

    public void update() {}

    public void render(Graphics2D g) {}

    public void destroy() {
        this.position.set(-32, -32);
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public int getSize() {
        return size;
    }

    public int getXPos() {
        return position.getX();
    }

    public int getYPos() {
        return position.getY();
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(int x, int y) {
        this.position = new Position(x, y);
    }

    public abstract Rectangle getHitbox();
}