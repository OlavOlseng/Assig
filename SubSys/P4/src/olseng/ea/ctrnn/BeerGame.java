package olseng.ea.ctrnn;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Olav on 13.04.2015.
 */
public class BeerGame {

    public final int width;
    public final int height;

    public final int playerSize = 5;

    public int playerPosition;
    public int objectSize;
    public int objectPositionX;
    public int objectPositionY;

    public boolean done = false;

    public boolean wrapping = true;
    public boolean pulled = false;

    private int[] result = null;

    Random rand = new Random(System.currentTimeMillis());

    public BeerGame(int width, int height) {
        this.width = width;
        this.height = height;
        this.playerPosition = rand.nextInt(width - playerSize);
        newDrop();
    }

    public void newDrop() {
        this.done = false;
        this.pulled = false;
        this.result = null;

        this.objectSize = rand.nextInt(6) + 1;

        int objectPlace = width;
        if(!wrapping) {
            objectPlace = width - objectSize;
        }

        this.objectPositionX = rand.nextInt(objectPlace);
        this.objectPositionY = height;
    }

    public void step(int move) {
        if(done) {
            return;
        }
        if(wrapping) {
            playerPosition += move + width;
            playerPosition %= width;
        }
        else {
            if(playerPosition + move < 0) {
                playerPosition = 0;
            }
            else if(playerPosition + move + playerSize > width) {
                playerPosition = 1 + width - playerSize;
            }
            else {
                playerPosition += move;
            }
        }
        objectPositionY -= 1;
        if (objectPositionY <= 0) {
            calculateResult();
            return;
        }
    }

    public void pull() {
        pulled = true;
        objectPositionY = 1;
        calculateResult();
    }

    public double[] getSensorReadings() {
        int size = wrapping ? playerSize : playerSize + 2;
        double[] sensors = new double[size];
        int tempPlayer = playerPosition + width;
        int tempObject = objectPositionX + width;
        for (int i = 0; i < playerSize; i++) {
            if (tempPlayer + i >= tempObject && tempPlayer + i < tempObject + objectSize) {
                sensors[i] = 1;
            }
        }
        if(!wrapping) {
            if (playerPosition <= 0) {
                sensors[sensors.length - 2] = 1;
            }
            if (playerPosition + playerSize > width ){
                sensors[sensors.length - 1] = 1;
            }
        }
        return sensors;
    }

    private void calculateResult() {
        done = true;
        result = new int[2];
        playerPosition += width;
        objectPositionX += width;

        //Avoided
        if (playerPosition > objectPositionX + objectSize || playerPosition + playerSize < objectPositionX) {
            result[0] = 0;
        }
        //Catch
        else if (playerPosition <= objectPositionX && playerPosition + playerSize >= objectPositionX + objectSize) {
            result[0] = 1;
        }
        //Collision
        else {
            result[0] = -1;
        }
        result[1] = objectSize < 5 ? 1 : 0;
        playerPosition -= width;
        objectPositionX -= width;
    }

    /**
     *
     * @return
     */
    public int[] getResult() {
            return result;
    }

    public void print() {
        for (int y = height - 1; y >= 0; y--) {
            String s = "";
            if (objectPositionY == y) {
                for (int x = 0; x < width; x++) {
                    if (x >= objectPositionX && x < objectPositionX + objectSize) {
                        s += "X";
                    }
                    else {
                        s +=  " ";
                    }
                }
            }
            else if (y == 0) {
                for (int x = 0; x < width; x++) {
                    if (x >= playerPosition && x < playerPosition + playerSize) {
                        s += "O";
                    }
                    else {
                        s +=  " ";
                    }
                }
            }
            System.out.println(s);
        }
    }

    //#################TESTPLAY#####################
    public static void main(String[] args) {
        BeerGame bg = new BeerGame(30, 15);
        Scanner input = new Scanner(System.in);
        System.out.println();
        String s = "";
        bg.newDrop();
        while(!bg.done) {
            System.out.println();
            bg.print();
            System.out.println(Arrays.toString(bg.getSensorReadings()));
            s = input.nextLine();
            if (s.equals("a")) {
                bg.step(-1);
            }
            else if (s.equals("d")) {
                bg.step(1);
            }
            else if (s.equals("s")) {
                bg.pull();
            }
            else if (s.equals("w")) {
                bg.step(0);
            }
        }
        bg.print();
        System.out.println(Arrays.toString(bg.getResult()));
    }
}
