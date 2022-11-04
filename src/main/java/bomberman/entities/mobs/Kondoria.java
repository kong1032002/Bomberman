package bomberman.entities.mobs;

import bomberman.entities.*;
import bomberman.entities.blocks.Bomb;
import bomberman.entities.blocks.Brick;
import bomberman.entities.blocks.Wall;
import bomberman.graphics.Sprite;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.util.*;

public class Kondoria extends Mob {

    protected Bomber target;
    private int[][] trace = new int[1000][1000];

    public Kondoria(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Kondoria(int x, int y) {
        super(x, y, Sprite.oneal_left1.getFxImage());
        curDir = LEFT;
    }

    @Override
    public void move(List<List<Stack<Entity>>> map) {
        if (status != DEATH) {
            BFS(target.getX() / 32, target.getY() / 32, map);
            if (trace[getTileX()][getTileY()] != 0 && x % 32 == 0 && y % 32 == 0) {
                curDir = trace[getTileX()][getTileY()];
            }
            switch (curDir) {
                case LEFT -> {
                    if (x % 32 != 0 || y % 32 != 0) {
                        moveLeft();
                    } else {
                        if (canMove(map)) {
                            moveLeft();
                        } else {
                            Random random = new Random();
                            curDir = random.nextInt(4) + 1;
                        }
                    }
                }
                case RIGHT -> {
                    if (x % 32 != 0 || y % 32 != 0) {
                        moveRight();
                    } else {
                        if (canMove(map)) {
                            moveRight();
                        } else {
                            Random random = new Random();
                            curDir = random.nextInt(4) + 1;
                        }
                    }
                }
                case UP -> {
                    if (x % 32 != 0 || y % 32 != 0) {
                        moveUp();
                    } else {
                        if (canMove(map)) {
                            moveUp();
                        } else {
                            Random random = new Random();
                            curDir = random.nextInt(4) + 1;
                        }
                    }
                }
                case DOWN -> {
                    if (x % 32 != 0 || y % 32 != 0) {
                        moveDown();
                    } else {
                        if (canMove(map)) {
                            moveDown();
                        } else {
                            Random random = new Random();
                            curDir = random.nextInt(4) + 1;
                        }
                    }
                }

            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (status != DEATH) {
            switch (curDir) {
                case LEFT, DOWN -> this.img = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, animate, 30).getFxImage();
                case RIGHT, UP -> this.img = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 30).getFxImage();
            }
        } else {
            this.img = Sprite.movingSprite(Sprite.kondoria_dead, Sprite.mob_dead1, Sprite.mob_dead2, animate, 30).getFxImage();
            timeRemain--;
        }
    }


    public void BFS(int x, int y, List<List<Stack<Entity>>> map) {
        Queue<Pair<Integer, Integer>> queue = new ArrayDeque<>();
        int[][] dis = new int[map.get(0).size()][map.size()];
        trace = new int[map.get(0).size()][map.size()];
        for (int i = 0; i < map.get(0).size(); i++) {
            for (int j = 0; j < map.size(); j++) {
                dis[i][j] = 10000;
            }
        }
        int u = x;
        int v = y;
        dis[u][v] = 0;
        trace[u][v] = 0;
        queue.add(new Pair<>(u, v));
        while (!queue.isEmpty()) {
            u = queue.peek().getKey();
            v = Objects.requireNonNull(queue.poll()).getValue();
            if (u <= 0 || v <= 0 || u >= map.get(0).size() || v >= map.size()) {
                continue;
            }
            if (!(map.get(v).get(u + 1).peek() instanceof Wall)
                    && !(map.get(v).get(u + 1).peek() instanceof Brick)
                    && !(map.get(v).get(u + 1).peek() instanceof Bomb)
                    && dis[u + 1][v] > dis[u][v] + 1) {
                dis[u + 1][v] = dis[u][v] + 1;
                trace[u + 1][v] = LEFT;
                queue.add(new Pair<>(u + 1, v));
            }
            if (!(map.get(v).get(u - 1).peek() instanceof Wall)
                    && !(map.get(v).get(u - 1).peek() instanceof Brick)
                    && !(map.get(v).get(u - 1).peek() instanceof Bomb)
                    && dis[u - 1][v] > dis[u][v] + 1) {
                dis[u - 1][v] = dis[u][v] + 1;
                trace[u - 1][v] = RIGHT;
                queue.add(new Pair<>(u - 1, v));
            }
            if (!(map.get(v - 1).get(u).peek() instanceof Wall)
                    && !(map.get(v - 1).get(u).peek() instanceof Brick)
                    && !(map.get(v - 1).get(u).peek() instanceof Bomb)
                    && dis[u][v - 1] > dis[u][v] + 1) {
                dis[u][v - 1] = dis[u][v] + 1;
                trace[u][v - 1] = DOWN;
                queue.add(new Pair<>(u, v - 1));
            }
            if (!(map.get(v + 1).get(u).peek() instanceof Wall)
                    && !(map.get(v + 1).get(u).peek() instanceof Brick)
                    && !(map.get(v + 1).get(u).peek() instanceof Bomb)
                    && dis[u][v + 1] > dis[u][v] + 1) {
                dis[u][v + 1] = dis[u][v] + 1;
                trace[u][v + 1] = UP;
                queue.add(new Pair<>(u, v - 1));
            }
            if (getTileX() == u && getTileY() == v) {
                break;
            }
        }
    }

    public void setTarget(Bomber target) {
        this.target = target;
    }
}
