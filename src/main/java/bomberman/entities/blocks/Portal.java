package bomberman.entities.blocks;

import bomberman.entities.Entity;
import bomberman.graphics.Sprite;
import javafx.scene.image.Image;

public class Portal extends Entity {

    public Portal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Portal(int x, int y) {
        super(x, y);
        this.img = Sprite.portal.getFxImage();
    }

    @Override
    public void update() {

    }
}
