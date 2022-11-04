package bomberman.entities.blocks;

import bomberman.entities.Entity;
import bomberman.graphics.Sprite;
import javafx.scene.image.Image;

public class Wall extends Entity {

    public Wall(int x, int y, Image img) {
        super(x, y, img);
    }

    public Wall(int x, int y) {
        super(x, y);
        this.img = Sprite.wall.getFxImage();
    }

    @Override
    public void update() {

    }
}
