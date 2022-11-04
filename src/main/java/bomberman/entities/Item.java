package bomberman.entities;

import bomberman.entities.Entity;
import javafx.scene.image.Image;

public abstract class Item extends Entity {
    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Item(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {

    }
}
