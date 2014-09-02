package GridPathfinder.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Olav on 02.09.2014.
 */
public class AStarMenuPanel extends JPanel {

    public JTextArea ta_mapdata;
    public JComboBox<String> cb_typeSelector;
    public JButton b_reload;
    public JButton b_run;
    public JTextField tf_status;
    public JTextField tf_delay;

    public AStarMenuPanel() {

        this.setLayout(new GridBagLayout());
        this.setBackground(Color.DARK_GRAY);

        ta_mapdata = new JTextArea("(100,100)\n(0,0) (99,99)");
        cb_typeSelector = new JComboBox<String>(new String[]{"Best-first", "Breadth-first", "Depth-first"});
        b_reload = new JButton("Reload");
        b_run = new JButton("GO!");
        tf_status = new JTextField();
        tf_delay = new JTextField("0");

        GridBagConstraints gBC = new GridBagConstraints();

        ta_mapdata.setRows(15);
        ta_mapdata.setColumns(20);
        ta_mapdata.setBackground(Color.LIGHT_GRAY);
        ta_mapdata.setEditable(true);


        gBC.weightx = 1.0;
        gBC.gridwidth = 2;
        gBC.gridx = 0;
        gBC.gridy = 0;
        gBC.fill = 1;
        this.add(ta_mapdata, gBC);


        gBC.weightx = 1.0;
        gBC.gridwidth = 2;
        gBC.gridx = 0;
        gBC.gridy = 1;
        gBC.ipadx = 5;

        this.add(cb_typeSelector, gBC);

        gBC.weightx = 0.5;
        gBC.gridwidth = 1;
        gBC.gridx = 0;
        gBC.gridy = 2;
        this.add(b_reload, gBC);

        gBC.weightx = 0.5;
        gBC.gridwidth = 1;
        gBC.gridx = 1;
        gBC.gridy = 2;
        gBC.fill = 1;
        this.add(b_run, gBC);

        gBC.weightx = 1.0;
        gBC.gridwidth = 2;
        gBC.gridx = 0;
        gBC.gridy = 3;
        gBC.fill = 1;
        this.add(tf_delay, gBC);
    }
}
