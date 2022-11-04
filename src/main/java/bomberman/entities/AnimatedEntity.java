package bomberman.entities;

import javafx.scene.image.Image;

public abstract class AnimatedEntity extends Entity {
    protected int animate = 0;
    protected int timeRemain = 200;
    protected final int Max_animate = 7500;

    public AnimatedEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    protected void animate() {
        if (animate < Max_animate) {
            animate++;
        } else {
            animate = 0;
        }
    }

    @Override
    public void update() {
        animate();
    }

    public int getAnimate() {
        return animate;
    }

    public int getTimeRemain() {
        return timeRemain;
    }

    public int getMax_animate() {
        return Max_animate;
    }
}
