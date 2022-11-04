package bomberman;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class Controller {

    public Controller() {
    }

    public void handleEvent(Scene scene, GameMap gameMap, long now) {
        scene.setOnKeyPressed(keyEvent -> {
            KeyCode code = keyEvent.getCode();
            gameMap.handleEvent(code);
        });
        scene.setOnKeyReleased(keyEvent -> gameMap.idleBomber());
    }
}
