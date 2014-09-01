package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Olav on 27/08/2014.
 */
public class AStar<T extends AStarState> {

    AStarStateHandler handler;
    T initialState;
    T goalState;

    AStarNode<T> initialNode = null;
    AStarNode<T> goalNode = null;

    public AStarNode lastExpanded = null;

    Callback callback;

    Map<Integer, AStarNode<T>> nodes;
    List<AStarNode<T>> open;

    int expandedNodes = 0;

    /**
     * @param stateHandler
     * @param initialState
     * @param goalState
     * @param callback - This callback will be called before every node expansion and on termination.
     */
    public AStar(AStarStateHandler stateHandler, T initialState, T goalState, Callback<AStarNode<T>> callback) {

        this.handler = stateHandler;
        this.callback = callback;


        goalState.setHash(handler.generateHash(goalState));
        this.goalState = goalState;

        initialState.setHash(handler.generateHash(initialState));
        initialState.setH(handler.calculateH(initialState, goalState));
        this.initialState = initialState;

        initialize();
    }

    /**
     * This method initializes all datastructures required to run the algorithm.
     * This method is automagically called from the constructor.
     */
    public void initialize() {
        this.open = new ArrayList<AStarNode<T>>();
        this.nodes = new HashMap<Integer, AStarNode<T>>();
        this.expandedNodes = 0;

        this.initialNode = new AStarNode(initialState);
        this.goalNode = new AStarNode(goalState);

        nodes.put(initialNode.getHash(), initialNode);
        nodes.put(initialNode.getHash(), initialNode);
        open.add(initialNode);
    }

    public boolean run() {
        boolean success = false;

        do {
            this.lastExpanded = open.remove(0);
            if (callback != null) {
                callback.callback(lastExpanded);
            }
            //Check success criteria
            if (lastExpanded.equals(goalNode)){
                goalNode = lastExpanded;
                success = true;
                break;
            }

            //Expand
            List<T> children = handler.getChildren(lastExpanded.state);
            lastExpanded.isExpanded = true;
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
                lastExpanded.addChild(childNode, childNode.state.getG() - lastExpanded.state.getG());

                if(!nodes.containsKey(childNode.getHash())) {
                    attachAndEval(lastExpanded, childNode);
                    nodes.put(childNode.getHash(), childNode);
                    open.add(childNode);
                }
                //Node was already made, check if new path is more optimal than previous path found
                else if (childNode.state.getG() > lastExpanded.state.getG() + lastExpanded.getArcCost(childNode)) {
                    attachAndEval(lastExpanded, childNode);

                    //See if propagation to grandchildren is needed.
                    if(childNode.isExpanded){
                        propagate(childNode);
                    }
                    //If not, we need to put it in the correct place in the open list.
                    else {
                        open.remove(childNode);
                        open.add(binarySearch(open, 0, open.size(), childNode.getF()), childNode);
                    }
                }
            }

        } while(open.size() > 0);
        if (callback != null) {
            callback.callback(lastExpanded);
        }
        return success;
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
                    open.add(binarySearch(open, 0, open.size(), child.getF()), child);
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
