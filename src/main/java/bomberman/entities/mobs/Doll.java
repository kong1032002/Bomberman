package bomberman.entities.mobs;

import bomberman.entities.*;
import bomberman.entities.blocks.Bomb;
import bomberman.entities.blocks.Wall;
import bomberman.graphics.Sprite;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Stack;

public class Doll extends Mob {
    public Doll(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Doll(int x, int y) {
        super(x, y, Sprite.doll_left1.getFxImage());
        curDir = RIGHT;
    }

    @Override
    public void move(List<List<Stack<Entity>>> map) {
        if (status != DEATH) {
            if (curDir == LEFT) {
                if (x % 32 != 0 || y % 32 != 0) {
                    moveLeft();
                } else {
                    if (!(map.get(y / 32).get(x / 32 - 1).peek() instanceof Bomb)
                            && !(map.get(y / 32).get(x / 32 - 1).peek() instanceof Wall)) {
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
                    if (!(map.get(y / 32).get(x / 32 + 1).peek() instanceof Bomb)
                            && !(map.get(y / 32).get(x / 32 + 1).peek() instanceof Wall)) {
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
                case LEFT -> this.img = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2, Sprite.doll_left3, animate, 30).getFxImage();
                case RIGHT -> this.img = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2, Sprite.doll_right3, animate, 30).getFxImage();
            }
        } else{
            this.img = Sprite.movingSprite(Sprite.doll_dead, Sprite.mob_dead1, Sprite.mob_dead2, animate, 30).getFxImage();
            timeRemain--;
        }
    }


}
