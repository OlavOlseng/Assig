import sun.plugin.dom.exception.InvalidStateException;

import java.io.*;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Olav on 07.04.2015.
 */
public class Level {

    public static final int TILE_FINISHED = -3;
    public static final int TILE_PLAYER = -2;
    public static final int TILE_POISON = -1;
    public static final int TILE_EMPTY = 0;
    public static final int TILE_FOOD = 1;

    public static final int DIR_UP = 0;
    public static final int DIR_LEFT = 1;
    public static final int DIR_DOWN = 2;
    public static final int DIR_RIGHT = 3;

    public int consumedFood = 0;
    public int consumedPoison = 0;

    public int foodCount = 0;
    public int poisonCount = 0;

    public int[][] map;
    public int[][] shadow;

    int orientation = 0;

    public final int width;
    public final int height;

    private int playerX = -1;
    private int playerY = -1;
    public int startX, startY;

    private boolean shadowing = false;
    private boolean initialized = false;

    long foodEaten = 0;

    public Level(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new int[height][width];
        this.shadow = new int[height][width];
    }

    public int movePlayer(int dir) {
        int x = playerX;
        int y = playerY;

        switch (dir) {
            case DIR_UP:
                y -= 1;
                break;
            case DIR_LEFT:
                x -= 1;
                break;
            case DIR_DOWN:
                y += 1;
                break;
            case DIR_RIGHT:
                x += 1;
                break;
            default:
                System.out.println("Invalid move.");
        }

        x = (x + map[0].length) % map[0].length;
        y = (y + map.length) % map.length;
        return setPlayer(x, y);
    }

    private int setPlayer(int x, int y) {
        if(playerX >= 0 && playerY >= 0) {
            if(shadowing) {
               shadow[playerY][playerX] += 1;
            }
            map[playerY][playerX] = TILE_EMPTY;
        }
        else {
            startX = x;
            startY = y;
        }
        int consumed = map[y][x];
        if(consumed > 0) {
            consumedFood++;
            foodEaten |= 1L << (consumed - 1);
        }
        else if (consumed == TILE_POISON) {
            consumedPoison++;
        }

        map[y][x] = TILE_PLAYER;
        this.playerX = x;
        this.playerY = y;
        return consumed;
    }

    public int getPlayerX() {
        return this.playerX;
    }

    public int getPlayerY() {
        return this.playerY;
    }

    public void setShadowing(boolean enabled) {
        this.shadowing = enabled;
    }

    public void print() {
        System.out.println();
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

    public static Level fromFile(String path) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(Level.class.getResourceAsStream("Levels/" + path)));

        String[] line  = rd.readLine().split(" ");
        Level level = new Level(Integer.parseInt(line[0]), Integer.parseInt(line[1]));

        level.foodCount = Integer.parseInt(line[4]);

        for (int y = 0; y < level.height; y++) {
            line  = rd.readLine().split(" ");

            for (int x = 0; x < line.length; x++) {
                switch (Integer.parseInt(line[x])) {
                    case -2:
                        level.map[y][x] = TILE_PLAYER;
                        level.setPlayer(x, y);
                    break;
                    case 0:
                    break;
                    case -1:
                        level.map[y][x] = TILE_POISON;
                        level.poisonCount++;
                    break;
                    default:
                        level.map[y][x] = Integer.parseInt(line[x]);
                }
            }
        }
        level.initialized = true;
        return level;
    }

    public boolean gameOver() {
        return foodCount == consumedFood && playerX == startX && playerY == startY;
    }

    public Level copy() {
        Level copy = new Level(map[0].length, map.length);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x  < map[0].length; x++) {
                copy.map[y][x] = this.map[y][x];
            }
        }
        copy.playerX = this.playerX;
        copy.playerY = this.playerY;
        copy.startX = this.startX;
        copy.startY = this.startY;
        copy.orientation = this.orientation;
        copy.foodCount = this.foodCount;
        copy.poisonCount = this.poisonCount;
        copy.foodEaten = this.foodEaten;
        return copy;
    }

    public static void main(String[] args) {
        Level l = null;

        try {
            l = Level.fromFile("1-simple.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        l.print();
        l.movePlayer(DIR_UP);
        l.print();
        l.movePlayer(DIR_LEFT);
        l.print();
        l.movePlayer(DIR_UP);
        l.print();
        l.movePlayer(DIR_DOWN);
        l.print();
        l.movePlayer(DIR_UP);
        l.print();
        l.movePlayer(DIR_UP);
        l.print();
        l.movePlayer(DIR_UP);
        l.print();
        l.movePlayer(DIR_LEFT);
        l.print();
        l.movePlayer(DIR_RIGHT);
        l.print();
        System.out.println("\nFE: " + l.consumedFood + " PE: " + l.consumedPoison);
    }
}
