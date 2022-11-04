package bomberman.entities.blocks;

import bomberman.entities.Entity;
import bomberman.graphics.Sprite;
import javafx.scene.image.Image;

public class Grass extends Entity {

    public Grass(int x, int y, Image img) {
        super(x, y, img);
    }

    public Grass(int x, int y) {
        super(x, y);
        this.img = Sprite.grass.getFxImage();
    }

    @Override
    public void update() {

    }
}
