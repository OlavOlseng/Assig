package GridPathfinder;

import GridPathfinder.gui.AStarCanvas;
import GridPathfinder.gui.AStarMenuPanel;
import framework.AStar;
import framework.AStarNode;
import framework.Callback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Olav on 01.09.2014.
 */
public class EuclidTest {
    public static long startTime = 0;
    public static AStar<GridState> aStar;
    public static AStarMenuPanel menu;
    public static AStarCanvas canvas;
    public static JTextField tf_statusField = new JTextField("Status Field");
    public static void main(String args[]) {

        tf_statusField.setEditable(false);

        int[][] board = new int[100][100];
        final GridState start = new GridState(board, board[0].length, board.length, 0, 0);
        GridState end = new GridState(board, board[0].length, board.length, 80, 75);

        aStar = new AStar(new EuclidianGridHandler(), start, end, null);
        aStar.initialize();
        JFrame frame = new JFrame("A* 420");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1280, 720);
        menu = new AStarMenuPanel();

        canvas = new AStarCanvas(aStar);
        aStar.setStepInterval(5);
        canvas.setClearingMode(true);
        aStar.setCallback(new Callback<AStar>() {
            @Override
            public void callback(AStar data) {
                canvas.render();
                tf_statusField.setText(
                        "Expanded nodes: " + data.expandedNodes
                                + "\tLast expanded: " + data.currentNode.state
                                + "\tCurrent Length: " + data.currentNode.state.getG()
                                + "\tCurrent runtime:" + (System.currentTimeMillis() - EuclidTest.startTime) + " ms"
                                + "\tSuccess: " + aStar.success);
            }
        });
        canvas.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                canvas.resize();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        frame.add(menu, BorderLayout.EAST);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(tf_statusField, BorderLayout.SOUTH);
        menu.setVisible(true);
        frame.setVisible(true);
        menu.b_run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTime = System.currentTimeMillis();
                aStar.initialize();
                new Thread(aStar).start();
            }
        });

        menu.cb_typeSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int clicked = menu.cb_typeSelector.getSelectedIndex();
                switch (clicked) {
                    case (1):
                        aStar.setType(AStar.TYPE_BREADTH_FIRST);
                        break;
                    case (2):
                        aStar.setType(AStar.TYPE_DEPTH_FIRST);
                        break;
                    default:
                        aStar.setType(AStar.TYPE_BEST_FIRST);
                }

            }
        });

        menu.b_reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readTextArea(menu.ta_mapdata, aStar);
                aStar.setStepInterval(Long.parseLong(menu.tf_delay.getText()));
                aStar.initialize();
                canvas.resize();
            }
        });
        menu.b_runtest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runNavigationTest();
            }
        });
    }

    public static void runNavigationTest() {
        startTime = System.currentTimeMillis();

        aStar.setType(AStar.TYPE_BEST_FIRST);
        aStar.initialize();
        aStar.run();
        System.out.println();
        System.out.println("Best-First: ");
        System.out.println(aStar);

        aStar.setType(AStar.TYPE_DEPTH_FIRST);
        aStar.initialize();
        aStar.run();
        System.out.println();
        System.out.println("Depth-First: ");
        System.out.println(aStar);

        aStar.setType(AStar.TYPE_BREADTH_FIRST);
        aStar.initialize();
        aStar.run();
        System.out.println();
        System.out.println("Breadth-First: ");
        System.out.println(aStar);
    }

    public static void readTextArea(JTextArea area, AStar astar) {
        String text = area.getText();
        text = text.replace("\n", "");
        text = text.replace(")", "\n");
        String[] lines = text.split("\n");
        int height = 0, width = 0;
        GridState start = null;
        GridState finish = null;
        int[][] board = null;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (i == 0) {
                String[] temp = line.split(",");
                width = Integer.parseInt(stripString(temp[0]));
                height = Integer.parseInt(stripString(temp[1]));
                board = new int[height][width];
            } else if (i == 1) {
                String[] temp = line.split(",");
                start = new GridState(board, width, height, Integer.parseInt(stripString(temp[0])), Integer.parseInt(stripString(temp[1])));

            } else if (i == 2) {
                String[] temp = line.split(",");
                finish = new GridState(board, width, height, Integer.parseInt(stripString(temp[0])), Integer.parseInt(stripString(temp[1])));

            } else {
                stripString(line);
                String[] temp = line.split(",");
                int ix = Integer.parseInt(stripString(temp[0]));
                int iy = Integer.parseInt(stripString(temp[1]));
                int iw = Integer.parseInt(stripString(temp[2]));
                int ih = Integer.parseInt(stripString(temp[3]));

                for (int x = ix; x < ix + iw; x++) {
                    for (int y = iy; y < iy + ih; y++) {
                        board[y][x] = 1;
                    }
                }
            }
        }
        astar.setNewStates(start, finish);
    }

    public static String stripString(String s) {
        s = s.replace("(", "");
        s = s.replace(")", "");
        s = s.replace(" ", "");
        return s;
    }
}

