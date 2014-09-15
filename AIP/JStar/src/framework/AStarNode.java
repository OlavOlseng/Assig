package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Olav on 27/08/2014.
 */
public class AStarNode<T extends AStarState> implements Comparable<AStarNode>{
    public T state;
    private List<AStarNode<T>> children;
    private AStarNode parent;
    private Map<Integer, Double> childCosts;
    public boolean isExpanded = false;

    public AStarNode(T state){
        this.children  = new ArrayList<AStarNode<T>>();
        this.childCosts = new HashMap<Integer, Double>();
        this.state = state;

    }

    public void setParent(AStarNode node) {
        this.parent = node;
    }

    public AStarNode getParent() {
        return this.parent;
    }

    public double getF() {
        return this.state.getF();
    }

    public int getHash() {
        return state.getHash();
    }

    public void addChild(AStarNode<T> n, double cost) {
        if (!children.contains(n)) {
            children.add(n);
            childCosts.put(n.getHash(), cost);
        }
        else {
            throw new RuntimeException("Child already set, this shouldn't happen");
        }
    }

    public List<AStarNode<T>> getChildren() {
        return this.children;
    }

    /**
     * This method checks if the hashes are equal.
     * @param other
     * @return true if hashes are equal, false elsewise.
     */
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

    public double getArcCost(AStarNode childNode) {
        return childCosts.get(childNode.getHash());
    }
}