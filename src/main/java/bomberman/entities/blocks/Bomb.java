package bomberman.entities.blocks;

import bomberman.entities.AnimatedEntity;
import bomberman.graphics.Sprite;
import javafx.scene.image.Image;

public class Bomb extends AnimatedEntity {

    public Bomb(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        timeRemain = 140;
    }

    public Bomb(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.bomb.getFxImage());
        timeRemain = 140;
    }

    @Override
    public void update() {
        animate();
        timeRemain--;
        this.img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 60).getFxImage();
    }
}
