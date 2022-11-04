package bomberman.entities.blocks;

import bomberman.entities.AnimatedEntity;
import bomberman.graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Stack;

public class Flame extends AnimatedEntity {
    private String dir = "";

    public Flame(int xUnit, int yUnit) {
        super(xUnit, yUnit, Sprite.bomb_exploded.getFxImage());
        timeRemain = 24;
    }

    public Flame(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        timeRemain = 24;
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }

    public void changeDir(String dir) {
        this.dir = dir;
    }

    @Override
    public void update() {
        animate();
        timeRemain--;
        switch (dir) {
            case "LEFT" -> img = Sprite.movingSprite(Sprite.explosion_horizontal_left_last,
                    Sprite.explosion_horizontal_left_last1, Sprite.explosion_horizontal_left_last2, animate, 30).getFxImage();
            case  "RIGHT" -> img = Sprite.movingSprite(Sprite.explosion_horizontal_right_last,
                    Sprite.explosion_horizontal_right_last1, Sprite.explosion_horizontal_right_last2, animate, 30).getFxImage();
            case "UP" -> img = Sprite.movingSprite(Sprite.explosion_vertical_top_last,
                    Sprite.explosion_vertical_top_last1, Sprite.explosion_vertical_top_last2, animate, 30).getFxImage();
            case "DOWN" -> img = Sprite.movingSprite(Sprite.explosion_vertical_down_last,
                    Sprite.explosion_vertical_down_last1, Sprite.explosion_vertical_down_last2, animate, 30).getFxImage();
            case "VERTICAL" -> img = Sprite.movingSprite(Sprite.explosion_vertical,
                    Sprite.explosion_vertical1, Sprite.explosion_vertical2, animate, 30).getFxImage();
            case "HORIZONTAL" -> img = Sprite.movingSprite(Sprite.explosion_horizontal,
                    Sprite.explosion_horizontal1, Sprite.explosion_horizontal2, animate, 30).getFxImage();
            default -> img = Sprite.movingSprite(Sprite.bomb_exploded,
                    Sprite.bomb_exploded1, Sprite.bomb_exploded2, animate, 30).getFxImage();
        }
    }

}
