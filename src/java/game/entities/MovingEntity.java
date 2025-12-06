package game.entities;

import game.GameplayPanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

//Classe abtraite pour décrire une entité mouvante
public abstract class MovingEntity extends Entity {
    protected int spd;
    protected int xSpd = 0;
    protected int ySpd = 0;
    protected BufferedImage sprite;
    protected float subimage = 0;
    protected int nbSubimagesPerCycle;
    protected int direction = 0;
    protected float imageSpd = 0.2f;
    protected int startX, startY;
    protected boolean hasStartPosition;

    public MovingEntity(int size, int xPos, int yPos, int spd, String spriteName, int nbSubimagesPerCycle, float imageSpd) {
        super(size, xPos, yPos);
        this.spd = spd;
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResource("img/" + spriteName));
            this.nbSubimagesPerCycle = nbSubimagesPerCycle;
            this.imageSpd = imageSpd;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStop(){
        this.xSpd = 0;
        this.ySpd = 0;
    }

    public void setStartPosition(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.hasStartPosition = true;
    }

    public void resetToStart() {
        if (!hasStartPosition) return;

        this.setPosition(startX, startY);
        this.setStop();
        this.subimage = 0;
        this.direction = 0;
        this.destroyed = false;
    }


    @Override
    public void update() {
        updatePosition();
    }

    public void updatePosition() {
        //Mise à jour de la position de l'entité
        if (!(xSpd == 0 && ySpd == 0)) {
            position.translate(xSpd, ySpd);

            if (xSpd > 0) {
                direction = 0;
            } else if (xSpd < 0) {
                direction = 1;
            } else if (ySpd < 0) {
                direction = 2;
            } else if (ySpd > 0) {
                direction = 3;
            }

            subimage += imageSpd;
            if (subimage >= nbSubimagesPerCycle) {
                subimage = 0;
            }
        }

        if (position.getX() > GameplayPanel.width) {
            position.set(0 - size + spd, position.getY());
        }

        if (position.getX() < 0 - size + spd) {
            position.set(GameplayPanel.width, position.getY());
        }

        if (position.getY() > GameplayPanel.height) {
            position.set(position.getX(), 0 - size + spd);
        }

        if (position.getY() < 0 - size + spd) {
            position.set(position.getX(), GameplayPanel.height);
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(
                sprite.getSubimage((int)subimage * size + direction * size * nbSubimagesPerCycle, 0, size, size),
                position.getX(),
                position.getY(),
                null
        );
    }

    //Méthode pour savoir si l'entité est bien positionnée sur une case de la grille de la zone de jeu ou non
    public boolean onTheGrid() {
        return (position.getX() % 8 == 0 && position.getY() % 8 == 0);
    }

    //Méthode pour savoir si l'entité est dans la zone de jeu ou non
    public boolean onGameplayWindow() {
        int x = position.getX();
        int y = position.getY();
        return !(x <= 0 || x >= GameplayPanel.width || y <= 0 || y >= GameplayPanel.height);
    }

    public Rectangle getHitbox() {
        return new Rectangle(position.getX(), position.getY(), size, size);
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public void setSprite(String spriteName) {
        try {
            this.sprite = ImageIO.read(getClass().getClassLoader().getResource("img/" + spriteName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float getSubimage() { return subimage; }
    public void setSubimage(float subimage) { this.subimage = subimage; }

    public int getNbSubimagesPerCycle() { return nbSubimagesPerCycle; }
    public void setNbSubimagesPerCycle(int nbSubimagesPerCycle) { this.nbSubimagesPerCycle = nbSubimagesPerCycle; }

    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }

    public int getXSpd() { return xSpd; }
    public void setXSpd(int xSpd) { this.xSpd = xSpd; }

    public int getYSpd() { return ySpd; }
    public void setYSpd(int ySpd) { this.ySpd = ySpd; }

    public int getSpd() { return spd; }
}
