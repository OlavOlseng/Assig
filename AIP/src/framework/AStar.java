package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Olav on 27/08/2014.
 */
public class AStar<T extends AStarState> implements Runnable {

    public final static int TYPE_BEST_FIRST = 0;
    public final static int TYPE_BREADTH_FIRST = 1;
    public final static int TYPE_DEPTH_FIRST = 2;

    private long stepInterval = 0;
    private long lastStepTime = 0;

    AStarStateHandler handler;
    T initialState;
    T goalState;

    public AStarNode<T> initialNode = null;
    public AStarNode<T> goalNode = null;
    public AStarNode<T> currentNode = null;

    Callback<AStar> callback;

    private int type;


    Map<Integer, AStarNode<T>> nodes;
    public List<AStarNode<T>> open;

    public int expandedNodes = 0;
    public boolean success = false;
    /**
     * @param stateHandler
     * @param initialState
     * @param goalState
     * @param callback - This callback will be called right after each node is popped off open.
     */
    public AStar(AStarStateHandler stateHandler, T initialState, T goalState, Callback<AStar> callback) {

        this.handler = stateHandler;
        this.callback = callback;

        setNewStates(initialState, goalState);

        initialize();
    }

    public AStar(AStarStateHandler stateHandler, T initialState, T goalState) {
        this(stateHandler, initialState, goalState, null);
    }

    public void setNewStates(T initialState, T goalState) {
        initialState.setHash(handler.generateHash(initialState));
        initialState.setH(handler.calculateH(initialState, goalState));
        this.initialState = initialState;

        goalState.setHash(handler.generateHash(goalState));
        this.goalState = goalState;
    }

        /**
         * This method initializes all datastructures required to run the algorithm.
         * This method is automagically called from the constructor.
         */
    public void initialize() {
        this.open = new ArrayList<AStarNode<T>>();
        this.nodes = new HashMap<Integer, AStarNode<T>>();
        currentNode = null;
        this.expandedNodes = 0;

        this.initialNode = new AStarNode(initialState);
        this.goalNode = new AStarNode(goalState);

        nodes.put(initialNode.getHash(), initialNode);
        open.add(initialNode);
    }

    public void setStepInterval(long ms) {
        this.stepInterval = ms;
    }

    public void setCallback(Callback<AStar> callback) {
        this.callback = callback;
    }

    /**
     * Sets the searchtype.
     * @param searchType
     * @return
     */
    public AStar<T> setType(int searchType) {
        this.type = searchType;
        return this;
    }

    @Override
    public void run() {
        success = false;

        do {
            //Check if we can step.
            long now = System.currentTimeMillis();
            if (now - lastStepTime < stepInterval) {
                continue;
            }
            lastStepTime = now;

            this.currentNode = open.remove(0);
            if (callback != null) {
                callback.callback(this);
            }
            //Check success criteria
            if (currentNode.equals(goalNode)){
                goalNode = currentNode;
                success = true;
                break;
            }

            //Expand
            List<T> children = handler.getChildren(currentNode.state);
            currentNode.isExpanded = true;
            expandedNodes++;

            for(T child : children) {
                AStarNode childNode;
                if(nodes.containsKey(child.getHash())) {
                    childNode = nodes.get(child.getHash());
                }
                else {
                    childNode = new AStarNode(child);
                }

                //Add childNodeToParent and set the distance between them.
                currentNode.addChild(childNode, childNode.state.getG() - currentNode.state.getG());

                if(!nodes.containsKey(childNode.getHash())) {
                    attachAndEval(currentNode, childNode);
                    nodes.put(childNode.getHash(), childNode);
                    insert(childNode);
                }
                //Node was already made, check if new path is more optimal than previous path found
                else if (childNode.state.getG() > currentNode.state.getG() + currentNode.getArcCost(childNode)) {
                    attachAndEval(currentNode, childNode);

                    //See if propagation to grandchildren is needed.
                    if(childNode.isExpanded){
                        propagate(childNode);
                    }
                    //If not, we need to put it in the correct place in the open list.
                    else {
                        open.remove(childNode);
                        insert(childNode);
                    }
                }
            }

        } while(open.size() > 0);
        callback.callback(this);
        System.out.println("Nodes expanded: " + expandedNodes);
    }

    public void insert(AStarNode<T> node) {
        switch(type) {
            case TYPE_DEPTH_FIRST:
                open.add(0, node);
                break;
            case TYPE_BREADTH_FIRST:
                open.add(node);
                break;
            case TYPE_BEST_FIRST:
                open.add(binarySearch(open, 0, open.size(), node.getF()), node);
                break;
            default:
                throw new IllegalStateException("Searchtype is invalid");
        }
    }

    private void propagate(AStarNode<T> parentNode) {
        double g = parentNode.state.getG();
        for (AStarNode child : parentNode.getChildren()) {
            double newG = g + parentNode.getArcCost(child);
            if (newG < child.state.getG()) {
                child.setParent(parentNode);
                child.state.setG(g);
                if(!child.isExpanded) {
                    open.remove(child);
                    insert(child);
                }
                propagate(child);
            }
        }
    }

    private void attachAndEval(AStarNode parentNode, AStarNode childNode) {
        childNode.setParent(parentNode);
        childNode.state.setH(handler.calculateH(childNode.state, goalNode.state));
    }

    private int binarySearch(List<AStarNode<T>> a, int start, int end, double val){

        if(start == end){
            return start;
        }
        int middle = (start + end)/2;
        if(val <= a.get(middle).getF()){
            return binarySearch(a, start, middle, val);
        }
        else{
            return binarySearch(a, middle + 1, end, val);
        }
    }
}
