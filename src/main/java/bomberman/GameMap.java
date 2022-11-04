package bomberman;

import bomberman.entities.*;
import bomberman.entities.blocks.*;
import bomberman.entities.mobs.*;
import bomberman.entities.items.BombItem;
import bomberman.entities.items.FlameItem;
import bomberman.entities.items.SpeedItem;
import bomberman.graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class GameMap {
    private List<List<Stack<Entity>>> map = new ArrayList<>();
    private List<String> titleMap = new ArrayList<>();
    private Queue<Bomb> bombs = new ArrayDeque<>();
    private Queue<Flame> flames = new ArrayDeque<>();
    private List<Mob> enemies = new ArrayList<>();
    private Bomber bomber = new Bomber(1, 1);
    private List<Entity> immortalObjects = new ArrayList<>();
    private int score = 0;
    private int level;

    public int getScore() {
        return score;
    }

    public void addScore() {
        score += 100;
    }

    public int getLives() {
        return bomber.getLives();
    }

    public int getBombs() {
        return bomber.getNumberOfBombs();
    }

    public void readTitleMapFromFile(int level) {
        this.level = level;
        FileInputStream fileInputStream;
        String url = String.format("src\\main\\resources\\levels\\Level%s.txt", level);
        try {
            fileInputStream = new FileInputStream(url);
            Scanner scanner = new Scanner(fileInputStream);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.equals("\n") || line.equals("")) {
                    break;
                }
                titleMap.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Winner");
        } catch (Exception e) {
            System.out.println("Error");
        }
        for (String s : titleMap) {
            System.out.println(s);
        }
    }

    public void loadMap(int level) {
        map = new ArrayList<>();
        titleMap = new ArrayList<>();
        bombs = new ArrayDeque<>();
        flames = new ArrayDeque<>();
        enemies = new ArrayList<>();
        bomber = new Bomber(1, 1);
        immortalObjects = new ArrayList<>();
        readTitleMapFromFile(level);
        for (int i = 0; i < titleMap.size(); i++) {
            map.add(new ArrayList<>());
            for (int j = 0; j < titleMap.get(i).length(); j++) {
                map.get(i).add(new Stack<>());
                map.get(i).get(j).add(new Grass(j, i, Sprite.grass.getFxImage()));
                immortalObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                switch (titleMap.get(i).charAt(j)) {
                    case '#' -> {
                        map.get(i).get(j).add(new Wall(j, i, Sprite.wall.getFxImage()));
                    }
                    case 'x' -> {
                        map.get(i).get(j).add(new Portal(j, i, Sprite.portal.getFxImage()));
                        map.get(i).get(j).add(new Brick(j, i, Sprite.brick.getFxImage()));
                    }
                    case 'b' -> {
                        map.get(i).get(j).add(new BombItem(j, i, Sprite.powerup_bombs.getFxImage()));
                        map.get(i).get(j).add(new Brick(j, i, Sprite.brick.getFxImage()));
                    }
                    case 'f' -> {
                        map.get(i).get(j).add(new FlameItem(j, i, Sprite.powerup_flames.getFxImage()));
                        map.get(i).get(j).add(new Brick(j, i, Sprite.brick.getFxImage()));
                    }
                    case 's' -> {
                        map.get(i).get(j).add(new SpeedItem(j, i, Sprite.powerup_speed.getFxImage()));
                        map.get(i).get(j).add(new Brick(j, i, Sprite.brick.getFxImage()));
                    }
                    case '*' -> {
                        map.get(i).get(j).add(new Brick(j, i, Sprite.brick.getFxImage()));
                    }
                    case '1' -> enemies.add(new Balloom(j, i, Sprite.balloom_left1.getFxImage()));
                    case '2' -> enemies.add(new Oneal(j, i, Sprite.oneal_left1.getFxImage()));
                    case '3' -> enemies.add(new Doll(j, i, Sprite.doll_left1.getFxImage()));
                    case '4' -> {
                        Kondoria kondoria = new Kondoria(j, i, Sprite.kondoria_left1.getFxImage());
                        kondoria.setTarget(bomber);
                        enemies.add(kondoria);
                    }
                }
            }
        }
    }

    public void renderGameMap(GraphicsContext graphicsContext) {
        immortalObjects.forEach(g -> g.render(graphicsContext));
        for (List<Stack<Entity>> stacks : map) {
            for (Stack<Entity> stack : stacks) {
                if (stack.peek() != null && !(stack.peek() instanceof Bomb)) {
                    stack.peek().render(graphicsContext);
                }
            }
        }

        bomber.render(graphicsContext);
        enemies.forEach(g -> g.render(graphicsContext));
        bombs.forEach(g -> g.render(graphicsContext));
        if (bomber.getLives() <= 0) {
            graphicsContext.setFill(Color.GRAY);
            graphicsContext.setFont(new Font("", 50));
            graphicsContext.fillRect(20, 100, 942, 216);
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillText("YOU LOSE", 370, 230);
        }
    }

    public void updateGameMap() {
        if (bomber.getLives() <= 0) {
            return;
        }
        for (int i = 0; i < enemies.size(); i++) {
            if (bomber.getTileX() == enemies.get(i).getTileX() && enemies.get(i).getTileY() == bomber.getTileY()
                    || map.get(bomber.getTileY()).get(bomber.getTileX()).peek() instanceof Flame) {
                if (bomber.isAlive() && enemies.get(i).getStatus() != Mob.DEATH) {
                    bomber.die();
                    Sound.play("DEATH");
                    if (enemies.get(i) instanceof Kondoria) {
                        enemies.get(i).die();
                    }
                }
            }
            if (map.get(enemies.get(i).getTileY()).get(enemies.get(i).getTileX()).peek() instanceof Flame) {
                if (enemies.get(i).getStatus() != Mob.DEATH) {
                    addScore();
                    enemies.get(i).die();
                }
            }
            if (enemies.get(i).getTimeRemain() <= 0) {
                enemies.remove(i);
                i--;
            }
        }
        enemies.forEach(AnimatedEntity::update);
        enemies.forEach(g -> g.move(map));
        flames.forEach(Flame::update);
        bomber.update();
        bombs.forEach(Bomb::update);
    }

    public void idleBomber() {
        bomber.idle();
    }

    public void handleEvent(KeyCode code) {
        if (bomber.getLives() <= 0) {
            if (code == KeyCode.R) {
                loadMap(1);
            }
        }
        if (bomber.getStatus() == Bomber.DEATH) {
                return;
        }
        Entity entity;
        bomber.moving();
        Sound.play("step");
        switch (code) {
            case LEFT, A -> {
                entity = map.get(bomber.getTileY()).get(bomber.getTileX() - 1).peek();
                if (bomber.getY() % 32 == 0 && bomber.getX() % 32 == 0) {
                    if (!(entity instanceof Wall)
                            && !(entity instanceof Bomb)
                            && !(entity instanceof Brick)) {
                        bomber.moveLeft();
                    }
                } else {
                    if (bomber.getY() % 32 != 0 && bomber.getX() % 32 == 0) {
                        if (!(entity instanceof Wall)
                                && !(entity instanceof Brick)
                                && !(map.get((bomber.getTileY() + 1)).get(bomber.getTileX() - 1).peek() instanceof Wall)
                                && !(map.get((bomber.getTileY() + 1)).get(bomber.getTileX() - 1).peek() instanceof Bomb)
                                && !(map.get((bomber.getTileY() + 1)).get(bomber.getTileX() - 1).peek() instanceof Brick)) {
                            bomber.moveLeft();
                        }
                        if (entity instanceof Wall
                                || entity instanceof Brick) {
                            bomber.moveDown();
                        } else {
                            if (map.get((bomber.getTileY() + 1)).get(bomber.getTileX() - 1).peek() instanceof Wall
                                    || map.get((bomber.getTileY() + 1)).get(bomber.getTileX() - 1).peek() instanceof Bomb
                                    || map.get((bomber.getTileY() + 1)).get(bomber.getTileX() - 1).peek() instanceof Brick) {
                                bomber.moveUp();
                            }
                        }
                    } else {
                        bomber.moveLeft();
                    }
                }
            }
            case RIGHT, D -> {
                entity = map.get((bomber.getTileY())).get(bomber.getTileX() + 1).peek();
                if (bomber.getY() % 32 == 0 && bomber.getX() % 32 == 0) {
                    if (!(entity instanceof Wall)
                            && !(entity instanceof Bomb)
                            && !(entity instanceof Brick)) {
                        bomber.moveRight();
                    }
                } else {
                    if (bomber.getY() % 32 != 0 && bomber.getX() % 32 == 0) {
                        if (!(entity instanceof Wall)
                                && !(entity instanceof Bomb)
                                && !(entity instanceof Brick)
                                && !(map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Wall)
                                && !(map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Bomb)
                                && !(map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Brick)) {
                            bomber.moveRight();
                        }
                        if (entity instanceof Wall
                                || entity instanceof Bomb
                                || entity instanceof Brick) {
                            bomber.moveDown();
                        } else {
                            if (map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Wall
                                    || map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Bomb
                                    || map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Brick) {
                                bomber.moveUp();
                            }
                        }
                    } else {
                        bomber.moveRight();
                    }
                }
            }
            case UP, W -> {
                entity = map.get((bomber.getTileY() - 1)).get(bomber.getTileX()).peek();
                if (bomber.getX() % 32 == 0 && bomber.getY() % 32 == 0) {
                    if (!(entity instanceof Wall)
                            && !(entity instanceof Bomb)
                            && !(entity instanceof Brick)) {
                        bomber.moveUp();
                    }
                } else {
                    if (bomber.getX() % 32 != 0 && bomber.getY() % 32 == 0) {
                        if (!(entity instanceof Wall)
                                && !(entity instanceof Bomb)
                                && !(entity instanceof Brick)
                                && !(map.get((bomber.getTileY() - 1)).get(bomber.getTileX() + 1).peek() instanceof Wall)
                                && !(map.get((bomber.getTileY() - 1)).get(bomber.getTileX() + 1).peek() instanceof Bomb)
                                && !(map.get((bomber.getTileY() - 1)).get(bomber.getTileX() + 1).peek() instanceof Brick)) {
                            bomber.moveUp();
                        }
                        if (entity instanceof Wall
                                || entity instanceof Bomb
                                || entity instanceof Brick) {
                            bomber.moveRight();
                        } else {
                            if (map.get((bomber.getTileY() - 1)).get(bomber.getTileX() + 1).peek() instanceof Wall
                                    || map.get((bomber.getTileY() - 1)).get(bomber.getTileX() + 1).peek() instanceof Brick
                                    || map.get((bomber.getTileY() - 1)).get(bomber.getTileX() + 1).peek() instanceof Bomb) {
                                bomber.moveLeft();
                            }
                        }
                    } else {
                        bomber.moveUp();
                    }
                }
            }
            case DOWN, S -> {
                entity = map.get((bomber.getTileY() + 1)).get(bomber.getTileX()).peek();
                if (bomber.getX() % 32 == 0 && bomber.getY() % 32 == 0) {
                    if (!(entity instanceof Wall)
                            && !(entity instanceof Bomb)
                            && !(entity instanceof Brick)) {
                        bomber.moveDown();
                    }
                } else {
                    if (bomber.getX() % 32 != 0 && bomber.getY() % 32 == 0) {
                        if (!(entity instanceof Wall)
                                && !(entity instanceof Bomb)
                                && !(entity instanceof Brick)
                                && !(map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Wall)
                                && !(map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Bomb)
                                && !(map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Brick)) {
                            bomber.moveDown();
                        }
                        if (entity instanceof Wall
                                || entity instanceof Bomb
                                || entity instanceof Brick) {
                            bomber.moveRight();
                        } else {
                            if (map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Wall
                                    || map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Bomb
                                    || map.get((bomber.getTileY() + 1)).get(bomber.getTileX() + 1).peek() instanceof Brick) {
                                bomber.moveLeft();
                            }
                        }
                    } else {
                        bomber.moveDown();
                    }
                }
            }
            case SPACE -> {
                if (bombs.size() < bomber.getNumberOfBombs() && !(map.get((bomber.getTileY())).get(bomber.getTileX()).peek() instanceof Bomb)) {
                    Sound.play("BOM_SET");
                    Bomb bomb = new Bomb(bomber.getTileX(), bomber.getTileY());
                    bombs.add(new Bomb(bomber.getTileX(), bomber.getTileY()));
                    map.get(bomb.getTileY()).get(bomb.getTileX()).add(bomb);
                }
                return;
            }
        }
        if (bomber.checkBombItem(map)) {
            Sound.play("Item");
            bomber.addBomb();
            map.get(bomber.getTileY()).get(bomber.getTileX()).pop();
            map.get(bomber.getTileY()).get(bomber.getTileX()).add(new Grass(bomber.getTileX(), bomber.getTileY(), Sprite.grass.getFxImage()));
        }
        if (bomber.checkSpeedItem(map)) {
            Sound.play("Item");
            bomber.speedUp();
            map.get(bomber.getTileY()).get(bomber.getTileX()).pop();
            map.get(bomber.getTileY()).get(bomber.getTileX()).add(new Grass(bomber.getTileX(), bomber.getTileY(), Sprite.grass.getFxImage()));
        }
        if (bomber.checkFlameItem(map)) {
            Sound.play("Item");
            bomber.increaseFlameSize();
            map.get(bomber.getTileY()).get(bomber.getTileX()).pop();
            map.get(bomber.getTileY()).get(bomber.getTileX()).add(new Grass(bomber.getTileX(), bomber.getTileY(), Sprite.grass.getFxImage()));
        }
        if (bomber.checkPortal(map)) {
            Sound.play("nextLevel");
            if (level <= 3) {
                nextLevel();
            }
        }

    }

    public void nextLevel() {
        loadMap(++level);
    }

    public void bombExplosion() {
        if (!bombs.isEmpty() && bombs.peek().getTimeRemain() < 0) {
            Bomb bomb = bombs.poll();
            Sound.play("Explosion");
            map.get(bomb.getTileY()).get(bomb.getTileX()).pop();
            dfs(bomb.getTileX(), bomb.getTileY(), "CENTER", bomber.getFlameSize());
        }
        while (!flames.isEmpty() && flames.peek().getTimeRemain() < 0) {
            Flame flame = flames.poll();
            map.get(flame.getTileY()).get(flame.getTileX()).pop();
            if (!(map.get(flame.getTileY()).get(flame.getTileX()).peek() instanceof Grass)
                    && !(map.get(flame.getTileY()).get(flame.getTileX()).peek() instanceof Wall)
                    && !(map.get(flame.getTileY()).get(flame.getTileX()).peek() instanceof Bomb)
                    && !(map.get(flame.getTileY()).get(flame.getTileX()).peek() instanceof Portal)) {
                map.get(flame.getTileY()).get(flame.getTileX()).pop();
            }
        }
    }

    public void dfs(int x, int y, String dir, int length) {
        if (map.get(y).get(x).peek() instanceof Wall || length <= 0) {
            return;
        }
        if (map.get(y).get(x).peek() instanceof Brick) {
            length = 1;
        }
        Flame flame;
        switch (dir) {
            case "LEFT", "RIGHT" -> {
                if (length == 1) {
                    if (dir.equals("LEFT")) {
                        flame = new Flame(x, y, Sprite.explosion_horizontal_left_last.getFxImage());
                        flame.changeDir("LEFT");
                    } else {
                        flame = new Flame(x, y, Sprite.explosion_horizontal_right_last.getFxImage());
                        flame.changeDir("RIGHT");
                    }
                } else {
                    flame = new Flame(x, y, Sprite.explosion_horizontal.getFxImage());
                    flame.changeDir("HORIZONTAL");
                }
            }
            case "UP", "DOWN" -> {
                if (length == 1) {
                    if (dir.equals("UP")) {
                        flame = new Flame(x, y, Sprite.explosion_vertical_top_last.getFxImage());
                        flame.changeDir("UP");
                    } else {
                        flame = new Flame(x, y, Sprite.explosion_vertical_down_last.getFxImage());
                        flame.changeDir("DOWN");
                    }
                } else {
                    flame = new Flame(x, y, Sprite.explosion_vertical.getFxImage());
                    flame.changeDir("VERTICAL");
                }
            }
            default -> {
                flame = new Flame(x, y, Sprite.bomb_exploded.getFxImage());
                flame.changeDir("CENTER");
            }
        }
        map.get(y).get(x).add(flame);
        flames.add(flame);
        if (dir.equals("CENTER") || dir.equals("LEFT")) {
            dfs(x - 1, y, "LEFT", length - 1);
        }
        if (dir.equals("CENTER") || dir.equals("RIGHT")) {
            dfs(x + 1, y, "RIGHT", length - 1);
        }
        if (dir.equals("CENTER") || dir.equals("UP")) {
            dfs(x, y - 1, "UP", length - 1);
        }
        if (dir.equals("CENTER") || dir.equals("DOWN")) {
            dfs(x, y + 1, "DOWN", length - 1);
        }
    }
}
