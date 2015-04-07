package olseng.ea.flatland;

import sun.plugin.dom.exception.InvalidStateException;

import java.util.Arrays;

/**
 * Created by Olav on 07.04.2015.
 */
public class Level {

    public static final int TILE_EMPTY = 0;
    public static final int TILE_PLAYER = 1;
    public static final int TILE_FOOD = 2;
    public static final int TILE_POISON = 3;

    public static final int DIR_FORWARD = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;

    public int consumedFood = 0;
    public int consumedPoison = 0;

    private int foodCount = 0;
    private int poisonCount = 0;

    public int[][] map;
    public int[][] shadow;

    private int playerX = -1;
    private int playerY = -1;

    private int orientation = 0;

    private boolean shadowing = false;
    private boolean initialized = false;

    public Level(int width, int height) {
        this.map = new int[height][width];
        this.shadow = new int[height][width];
    }

    public void initialize(double pFood, double pPoison) {
        if (initialized) {
            return;
        }
        initialized = true;
        int px = (int)(Math.random() * map[0].length);
        int py = (int)(Math.random() * map.length);

        setPlayer(px, py);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == TILE_PLAYER) {
                    continue;
                }
                double val = Math.random();

                if (val < pFood) {
                    map[y][x] = TILE_FOOD;
                    foodCount += 1;
                }
                else if (val < pFood + pPoison) {
                    map[y][x] = TILE_POISON;
                    poisonCount += 1;
                }
            }
        }
    }

    public void movePlayer(int dir) {
        if (dir == DIR_LEFT) {
            orientation = (orientation + 90) % 360;
            dir = DIR_FORWARD;
        }
        else if (dir == DIR_RIGHT) {
            orientation = (orientation + 270) % 360;
            dir = DIR_FORWARD;

        }
        int x = playerX;
        int y = playerY;
        if (dir == DIR_FORWARD) {

            if (orientation == 0) {
                x += 1;
            }
            else if (orientation == 90) {
                y -= 1;
            }
            else if (orientation == 180) {
                x -= 1;

            }
            else if (orientation == 270) {
                y += 1;
            }
            else {
                throw new InvalidStateException("Orientation was bad");
            }

            x = (x + map[0].length) % map[0].length;
            y = (y + map.length) % map.length;
        }
        setPlayer(x, y);
    }

    private void setPlayer(int x, int y) {
        if(playerX >= 0 && playerY >= 0) {
            if(shadowing) {
               shadow[playerY][playerX] += 1;
            }
            map[playerY][playerX] = TILE_EMPTY;
        }
        int consumed = map[y][x];
        if(consumed == TILE_FOOD) {
            consumedFood++;
        }
        else if (consumed == TILE_POISON) {
            consumedPoison++;
        }

        map[y][x] = TILE_PLAYER;
        this.playerX = x;
        this.playerY = y;
    }

    public void setShadowing(boolean enabled) {
        this.shadowing = enabled;
    }

    public void print() {
        System.out.println();
        System.out.println("Orientation: " + orientation);
        for (int[] row : map) {
            System.out.println(Arrays.toString(row));
        }
    }

    /**
     * @return A new level object with a copied map object, and player location and orientation. Nothing else is copied.
     */
    public Level copy() {
        Level copy = new Level(map.length, map[0].length);
        copy.map = this.map.clone();
        copy.playerX = this.playerX;
        copy.playerY = this.playerY;
        copy.orientation = this.orientation;
        return copy;
    }

    public static void main(String[] args) {
        Level l = new Level(5,5);
        l.initialize(0.3, 0.3);
        l.print();
        l.movePlayer(DIR_FORWARD);
        l.print();
        l.movePlayer(DIR_LEFT);
        l.print();
        l.movePlayer(DIR_FORWARD);
        l.print();
        l.movePlayer(DIR_FORWARD);
        l.print();
        l.movePlayer(DIR_FORWARD);
        l.print();
        l.movePlayer(DIR_LEFT);
        l.print();
        l.movePlayer(DIR_RIGHT);
        l.print();
        System.out.println("\nFE: " + l.consumedFood + " PE: " + l.consumedPoison);
    }
}
