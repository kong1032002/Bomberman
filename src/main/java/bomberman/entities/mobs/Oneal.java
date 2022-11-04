package bomberman.entities.mobs;

import bomberman.entities.*;
import bomberman.graphics.Sprite;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Oneal extends Mob {
    public Oneal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Oneal(int x, int y) {
        super(x, y, Sprite.oneal_left1.getFxImage());
        curDir = LEFT;
    }

    @Override
    public void move(List<List<Stack<Entity>>> map) {
        if (status != DEATH) {
            if (x % 32 != 0 || y % 32 != 0) {
                switch (curDir) {
                    case UP -> moveUp();
                    case DOWN -> moveDown();
                    case LEFT -> moveLeft();
                    case RIGHT -> moveRight();
                }
            } else {
                if (speed == 1) {
                    speed = 2;
                } else {
                    speed = 1;
                }
                Random r = new Random();
                int d = r.nextInt(4) + 1;
                switch (d) {
                    case 0:
                        curDir = LEFT;
                        if (canMove(map)) {
                            moveLeft();
                            break;
                        }
                    case 1:
                        curDir = RIGHT;
                        if (canMove(map)) {
                            moveRight();
                            break;
                        }
                    case 2:
                        curDir = UP;
                        if (canMove(map)) {
                            moveUp();
                            break;
                        }
                    case 3:
                        curDir = DOWN;
                        if (canMove(map)) {
                            moveDown();
                            break;
                        }
                }
            }
        }
    }
    
    @Override
    public void update() {
        super.update();
        if (status != DEATH) {
            switch (curDir) {
                case LEFT, DOWN -> this.img = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, animate, 30).getFxImage();
                case RIGHT, UP -> this.img = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, animate, 30).getFxImage();
            }
        } else {
                this.img = Sprite.movingSprite(Sprite.oneal_dead, Sprite.mob_dead1, Sprite.mob_dead2, animate, 30).getFxImage();
                timeRemain--;
        }
    }
}
