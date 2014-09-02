package GridPathfinder;

import GridPathfinder.gui.AStarCanvas;
import GridPathfinder.gui.AStarMenuPanel;
import framework.AStar;
import framework.AStarNode;
import framework.Callback;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Olav on 01.09.2014.
 */
public class EuclidTest {
    public static void main(String args[]) {

        final JTextField tf_statusField = new JTextField("Status Field");
        tf_statusField.setEditable(false);

        int[][] board = new int[100][100];
        GridState start = new GridState(board, board[0].length, board.length, 0, 0);
        GridState end = new GridState(board, board[0].length, board.length, 80, 75);

        final AStar aStar = new AStar(new EuclidianGridHandler(), start, end);
        aStar.initialize();
        JFrame frame = new JFrame("A* 420");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1280, 720);
        final AStarMenuPanel menu = new AStarMenuPanel();

        final AStarCanvas canvas = new AStarCanvas(aStar);
        aStar.setStepInterval(5);
        canvas.setClearingMode(true);
        aStar.setCallback(new Callback<AStar>() {
            @Override
            public void callback(AStar data) {
                canvas.render();
                tf_statusField.setText("Expanded nodes: " + data.expandedNodes + "\tLast expanded: " + data.currentNode.state + "\tSuccess: " + aStar.success);
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
                long startTime = System.currentTimeMillis();
                aStar.initialize();
                new Thread(aStar).start();
                System.out.println("Runtime: " + (System.currentTimeMillis() - startTime) + " ms");
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
    }

    public static void readTextArea(JTextArea area, AStar astar) {
        int count = area.getLineCount();
        if (count < 2) {
            return;
        }
        String[] lines = area.getText().split("\n");
        int height = 0, width = 0;
        GridState start = null;
        GridState finish = null;
        int[][] board = null;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (i == 0) {
                line = line.substring(1, line.length() - 1);
                String[] temp = line.split(",");
                width = Integer.parseInt(temp[0]);
                height = Integer.parseInt(temp[1]);
                board = new int[height][width];
            } else if (i == 1) {
                String[] temp = lines[i].split(" ");

                line = temp[0].substring(1, temp[0].length() - 1);
                String[] temptemp = line.split(",");
                start = new GridState(board, width, height, Integer.parseInt(temptemp[0]), Integer.parseInt(temptemp[1]));

                line = temp[1].substring(1, temp[1].length() - 1);
                temptemp = line.split(",");
                finish = new GridState(board, width, height, Integer.parseInt(temptemp[0]), Integer.parseInt(temptemp[1]));
            } else {
                line = line.substring(1, line.length() - 1);
                String[] temp = line.split(",");
                int ix = Integer.parseInt(temp[0]);
                int iy = Integer.parseInt(temp[1]);
                int iw = Integer.parseInt(temp[2]);
                int ih = Integer.parseInt(temp[3]);

                for (int x = ix; x < ix + iw; x++) {
                    for (int y = iy; y < iy + ih; y++) {
                        board[y][x] = 1;
                    }
                }
            }
        }
        astar.setNewStates(start, finish);
    }
}

