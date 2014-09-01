package framework;

import java.util.List;

/**
 * Created by Olav on 01.09.2014.
 */
public abstract class AStarStateHandler<T extends AStarState> {

    public abstract int generateHash(T state);

    public abstract double calculateH(T state, T goalState);

    /**
     * This method should generate all valid successor states to the paramater state.
     * The G value should also be set for the successor states.
     * @param state
     * @return
     */
    protected abstract List<T> generateChildren(T state);

    /**
     * This method will call generateChildren(), and generate and set all the hashes for the children using the generateHash() function.
     * Note that the heuristic is not calculated pr. state.
     * @return The list of child states.
     */
    public List<T> getChildren(T state) {
        List<T> res = generateChildren(state);

        for (T child : res){
            child.setHash(generateHash(child));
        }
        return res;
    }
}
