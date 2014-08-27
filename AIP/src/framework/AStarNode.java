package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Olav on 27/08/2014.
 */
public class AStarNode implements Comparable<AStarNode>{
    private final AStarState state;
    private List<AStarNode> children;
    private AStarNode parent;
    private Map<AStarNode, Double> childCosts;

    public AStarNode(AStarState state){
        this.children  = new ArrayList<AStarNode>();
        this.childCosts = new HashMap<AStarNode, Double>();
        this.state = state;

    }

    public void setParent(AStarNode node) {
        this.parent = node;
    }

    public double getF() {
        return this.state.getF();
    }
    public int getHash() {
        return state.getHash();
    }

    public void addChild(AStarNode n) {
        //TODO: IMPLEMENT
    }

    public List<AStarNode> getChildren() {
        return this.children;
    }

    public boolean equals(AStarNode other) {
        return this.getHash() == other.getHash();
    }

    @Override
    public int compareTo(AStarNode o) {
        if (o.getF() > this.getF()) {
            return -1;
        }
        else if (o.getF() > this.getF()) {
            return 1;
        }
        return 0;
    }
}
