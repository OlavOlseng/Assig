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

    public static final int DIR_FORWARD = 0;
    public static final int DIR_LEFT = 1;
    public static final int DIR_RIGHT = 2;

    public int consumedFood = 0;
    public int consumedPoison = 0;

    public int foodCount = 0;
    public int poisonCount = 0;

    public int[][] map;
    public int[][] shadow;

    private int playerX = -1;
    private int playerY = -1;

    public int orientation = 0;

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
        orientation = (int)(Math.random() * 4) * 90;
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

    public double[] getSensorData() {
        double[] readings = new double[6];
        int fx, fy, lx, ly, rx, ry;

        //forward
        if (orientation == 0) {
            fx = playerX + 1;
            fy = playerY;

            lx = playerX;
            ly = playerY - 1;

            rx = playerX;
            ry = playerY + 1;
        }
        else if (orientation == 90) {
            fx = playerX;
            fy = playerY - 1;

            lx = playerX - 1;
            ly = playerY;

            rx = playerX + 1;
            ry = playerY;
        }
        else if (orientation == 180) {
            fx = playerX - 1;
            fy = playerY;

            lx = playerX;
            ly = playerY - 1;

            rx = playerX;
            ry = playerY + 1;
        }
        else if (orientation == 270) {
            fx = playerX;
            fy = playerY + 1;

            lx = playerX + 1;
            ly = playerY;

            rx = playerX - 1;
            ry = playerY;
        }
        else {
            throw new InvalidStateException("Agent disoriented");
        }

        fx = (fx + map[0].length) % map[0].length;
        fy = (fy + map.length) % map.length;
        lx = (lx + map[0].length) % map[0].length;
        ly = (ly + map.length) % map.length;
        rx = (rx + map[0].length) % map[0].length;
        ry = (ry + map.length) % map.length;

        readings[0] = map[fy][fx] == TILE_FOOD ? 1 : 0;
        readings[1] = map[ly][lx] == TILE_FOOD ? 1 : 0;
        readings[2] = map[ry][rx] == TILE_FOOD ? 1 : 0;
        readings[3] = map[fy][fx] == TILE_POISON ? 1 : 0;
        readings[4] = map[ly][lx] == TILE_POISON ? 1 : 0;
        readings[5] = map[ry][rx] == TILE_POISON ? 1 : 0;

        return readings;
    }

    public Level copy() {
        Level copy = new Level(map.length, map[0].length);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x  < map[0].length; x++) {
                copy.map[y][x] = this.map[y][x];
            }
        }
        copy.playerX = this.playerX;
        copy.playerY = this.playerY;
        copy.orientation = this.orientation;
        copy.foodCount = this.foodCount;
        copy.poisonCount = this.poisonCount;
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

        l = new Level(3,3);
        l.initialize(0.3, 0.3);
        l.setPlayer(1,1);
        l.print();
        System.out.println(Arrays.toString(l.getSensorData()));
    }
}
