package bomberman.entities;

import bomberman.entities.*;
import bomberman.entities.blocks.Bomb;
import bomberman.entities.blocks.Brick;
import bomberman.entities.blocks.Wall;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Stack;

abstract public class Mob extends AnimatedEntity {
    public static final int IDLE = 853;
    public static final int MOVING = 850;
    public static final int DEATH = 41;
    public static final int IMMUNE = 861;

    protected static final int LEFT = 4;
    protected static final int RIGHT = 1;
    protected static final int UP = 2;
    protected static final int DOWN = 3;

    protected int curDir;
    protected int status = IDLE;
    protected int speed = 1;

    public Mob(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        curDir = LEFT;
        timeRemain = 10;
    }

    public int getStatus() {
        return status;
    }

    public void move(List<List<Stack<Entity>>> map) {
    }

    public void moveUp() {
        this.y -= speed;
        curDir = UP;
    }

    public void moveDown() {
        this.y += speed;
        curDir = DOWN;
    }

    public void moveLeft() {
        this.x -= speed;
        curDir = LEFT;
    }

    public void moveRight() {
        this.x += speed;
        curDir = RIGHT;
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }

    public boolean canMove(List<List<Stack<Entity>>> map) {
        switch (curDir) {
            case UP:
                if (!(map.get(getTileY() - 1).get(getTileX()).peek() instanceof Brick)
                        && !(map.get(getTileY() - 1).get(getTileX()).peek() instanceof Bomb)
                        && !(map.get(getTileY() - 1).get(getTileX()).peek() instanceof Wall)) {
                    return true;
                }
                break;
            case DOWN:
                if (!(map.get(getTileY() + 1).get(getTileX()).peek() instanceof Brick)
                        && !(map.get(getTileY() + 1).get(getTileX()).peek() instanceof Bomb)
                        && !(map.get(getTileY() + 1).get(getTileX()).peek() instanceof Wall)) {
                    return true;
                }
                break;
            case LEFT:
                if (!(map.get(getTileY()).get(getTileX() - 1).peek() instanceof Brick)
                        && !(map.get(getTileY()).get(getTileX() - 1).peek() instanceof Bomb)
                        && !(map.get(getTileY()).get(getTileX() - 1).peek() instanceof Wall)) {
                    return true;
                }
                break;
            case RIGHT:
                if (!(map.get(getTileY()).get(getTileX() + 1).peek() instanceof Brick)
                        && !(map.get(getTileY()).get(getTileX() + 1).peek() instanceof Bomb)
                        && !(map.get(getTileY()).get(getTileX() + 1).peek() instanceof Wall)) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void moving() {
        status = MOVING;
    }

    public void idle() {
        status = IDLE;
    }

    public void die() {
        status = DEATH;
        timeRemain = 32;
    }

    public void revive() {
        x = 32;
        y = 32;
        idle();
    }
}

