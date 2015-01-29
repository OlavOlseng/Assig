import java.util.ArrayList;
import java.util.List;
import Agent.Action;

/**
 * Created by Olav on 19/01/2015.
 */
public class Game {
    //DEFECT, COOPERATE
    public static final int[][] SCORE_MATRIX = {
            {2,5},
            {0,3},
    };

    Agent agent1;
    Agent agent2;
    List<Action> agent1Actions;
    List<Action> agent2Actions;
    List<Integer> agent1Scores;
    List<Integer> agent2Scores;

    public Game(Agent agent1, Agent agent2) {
        this.agent1 = agent1;
        this.agent2 = agent2;
        this.agent1Actions = new ArrayList<Action>();
        this.agent2Actions = new ArrayList<Action>();
        this.agent1Scores = new ArrayList<Integer>();
        this.agent2Scores = new ArrayList<Integer>();
    }

    public void play() {
        Action agent1Act = agent1.dilemma(agent2Actions);
        Action agent2Act = agent2.dilemma(agent1Actions);
        agent1Scores.add(getScore(agent1Act, agent2Act));
        agent2Scores.add(getScore(agent2Act, agent1Act));
        agent1Actions.add(0, agent1Act);
        agent2Actions.add(0, agent2Act);
    }

    public int getAgent1MScore() {
        int score = 0;
        for (int i : agent1Scores) {
            score += i;
        }
        return score/agent1Scores.size();
    }

    public int getAgent2MScore() {
        int score = 0;
        for (int i : agent2Scores) {
            score += i;
        }
        return score/agent2Scores.size();
    }

    public static int getScore(Agent.Action myAction, Agent.Action otherAction) {
        return SCORE_MATRIX[myAction.val][otherAction.val];
    }

    public static void main(String[] args) {
        System.out.println(getScore(Agent.Action.DEFECT, Agent.Action.DEFECT));
        System.out.println(getScore(Agent.Action.DEFECT, Agent.Action.COOPERATE));
        System.out.println(getScore(Agent.Action.COOPERATE, Agent.Action.DEFECT));
        System.out.println(getScore(Agent.Action.COOPERATE, Agent.Action.COOPERATE));
    }
}
