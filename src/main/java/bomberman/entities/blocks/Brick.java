package bomberman.entities.blocks;

import bomberman.entities.Entity;
import bomberman.graphics.Sprite;
import javafx.scene.image.Image;

public class Brick extends Entity {
    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Brick(int xUnit, int yUnit) {
        super(xUnit, yUnit);
        this.img = Sprite.brick.getFxImage();
    }

    @Override
    public void update() {

    }
}
