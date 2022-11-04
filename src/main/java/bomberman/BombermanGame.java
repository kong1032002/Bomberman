package bomberman;
import bomberman.entities.Item;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import bomberman.graphics.Sprite;

public class BombermanGame extends Application {
    
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;
    private GraphicsContext gc;
    private Canvas canvas;
    private final GameMap map = new GameMap();
    private final Controller controller = new Controller();

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root);
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        stage.setWidth(Sprite.SCALED_SIZE * WIDTH );
        stage.setHeight(Sprite.SCALED_SIZE * HEIGHT + 100);

        Label label = new Label();
        label.setLayoutY(Sprite.SCALED_SIZE * HEIGHT);
        //label.setLayoutX(0);
        root.getChildren().add(label);

        Sound.play("soundtrack");
        stage.setScene(scene);
        stage.show();
        map.loadMap(1);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                stage.setTitle("Bomberman_game");
                label.setText( "Score: " + map.getScore() + "\n"
                                + "Lives: " + map.getLives() + "\n"
                                + "Bombs: " + map.getBombs());
                render();
                update();
                controller.handleEvent(scene, map, l);
                map.bombExplosion();
            }
        };
        timer.start();
    }

    public void update() {
        map.updateGameMap();
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        map.renderGameMap(gc);
    }
}
