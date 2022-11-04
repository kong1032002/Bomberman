package bomberman.entities.mobs;

import bomberman.entities.*;
import bomberman.graphics.Sprite;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Stack;

public class Balloom extends Mob {
    public Balloom(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Balloom(int x, int y) {
        super(x, y, Sprite.balloom_left1.getFxImage());
        curDir = RIGHT;
    }

    @Override
    public void move(List<List<Stack<Entity>>> map) {
        if (status != DEATH) {
            if (curDir == LEFT) {
                if (x % 32 != 0 || y % 32 != 0) {
                    moveLeft();
                } else {
                    if (canMove(map)) {
                        moveLeft();
                    } else {
                        curDir = RIGHT;
                    }
                }
            }
            if (curDir == RIGHT) {
                if (x % 32 != 0 || y % 32 != 0) {
                    moveRight();
                } else {
                    if (canMove(map)) {
                        moveRight();
                    } else {
                        curDir = LEFT;
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
                case LEFT -> this.img = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2, Sprite.balloom_left3, animate, 30).getFxImage();
                case RIGHT -> this.img = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2, Sprite.balloom_right3, animate, 30).getFxImage();
            }
        } else {
            this.img = Sprite.movingSprite(Sprite.balloom_dead, Sprite.mob_dead1, Sprite.mob_dead2, animate, 30).getFxImage();
            timeRemain--;
        }
    }
}
