package bomberman.entities.mobs;

import bomberman.entities.Entity;
import bomberman.entities.Mob;
import bomberman.entities.blocks.Portal;
import bomberman.entities.items.BombItem;
import bomberman.entities.items.FlameItem;
import bomberman.entities.items.SpeedItem;
import bomberman.graphics.Sprite;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Stack;

public class Bomber extends Mob {
    protected int lives = 3;
    protected int flameSize = 2;
    protected int numberOfBombs = 2;

    public void speedUp() {
        speed += speed;
    }

    public Bomber(int x, int y, Image img) {
        super(x, y, img);
        speed = 4;
        status = IDLE;
    }

    public Bomber(int x, int y) {
        super(x, y, Sprite.player_right.getFxImage());
        speed = 4;
        status = IDLE;
    }

    public int getStatus() {
        return status;
    }

    public void idle() {
        status = IDLE;
    }

    public  void moving() {
        status = MOVING;
    }

    public void addBomb() {
        numberOfBombs++;
    }

    public int getLives() {
        return lives;
    }

    public int getFlameSize() {
        return flameSize;
    }

    public int getNumberOfBombs() {
        return numberOfBombs;
    }

    @Override
    public void update() {
        animate();
        if (status == DEATH) {
            this.img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2,
                    Sprite.player_dead3, animate, 60).getFxImage();
            timeRemain--;
            if (timeRemain <= 0) {
                revive();
            }
        } else {
            if (status == IMMUNE) {
                if (timeRemain < 0) {
                    status = IDLE;
                } else{
                    timeRemain--;
                }

            }
            switch (curDir) {
                case LEFT -> {
                    this.img = Sprite.player_left.getFxImage();
                    if (status == MOVING) {
                        this.img = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, animate, 10).getFxImage();
                    }
                }
                case RIGHT -> {
                    this.img = Sprite.player_right.getFxImage();
                    if (status == MOVING) {
                        this.img = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 10).getFxImage();
                    }
                }
                case UP -> {
                    this.img = Sprite.player_up.getFxImage();
                    if (status == MOVING) {
                        this.img = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, animate, 10).getFxImage();
                    }
                }
                case DOWN -> {
                    this.img = Sprite.player_down.getFxImage();
                    if (status == MOVING) {
                        this.img = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, animate, 10).getFxImage();
                    }
                }
            }
        }
    }

    @Override
    public void revive() {
        super.revive();
        lives--;
    }

    public boolean checkSpeedItem(List<List<Stack<Entity>>> map) {

        return (this.x % 32 == 0 && this.y % 32 == 0)
                && map.get(y / 32).get(x / 32).peek() instanceof SpeedItem;
    }

    public boolean checkBombItem(List<List<Stack<Entity>>> map) {
        return (this.x % 32 == 0 && this.y % 32 == 0)
                && map.get(y / 32).get(x / 32).peek() instanceof BombItem;
    }

    public boolean checkFlameItem(List<List<Stack<Entity>>> map) {
        return (this.x % 32 == 0 && this.y % 32 == 0)
                && map.get(y / 32).get(x / 32).peek() instanceof FlameItem;
    }

    public boolean checkPortal(List<List<Stack<Entity>>> map) {
        return (this.x % 32 == 0 && this.y % 32 == 0)
                && map.get(y / 32).get(x / 32).peek() instanceof Portal;
    }

    public void increaseFlameSize() {
        flameSize++;
    }

    public boolean isAlive() {
        return (status == IDLE || status == MOVING);
    }
}