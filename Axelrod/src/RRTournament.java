import java.util.List;

/**
 * Created by Olav on 19/01/2015.
 */
public class RRTournament {

    public static void play(int rounds, List<Agent> agents) {

        int[][] scores = new int[agents.size()][agents.size()];

        for (int i = 0; i < agents.size()-1; i++) {
            for (int j = i+1; j < agents.size(); j++) {

                Game g = new Game(agents.get(i), agents.get(j));

                for (int n = 0; n < rounds; n++) {
                    g.play();
                }

                scores[i][j] = g.getAgent1MScore();
                scores[j][i] = g.getAgent2MScore();
            }
        }
        System.out.println("Agent scores: ");
        for (int i = 0; i < agents.size(); i++) {
            System.out.println(String.format("Performance of %s: ", agents.get(i).getName()));
            System.out.println(scores[i]);
            int fScore = 0;
            for (int j = 0; j < agents.size(); j++) {
                fScore += scores[i][j];
            }
            System.out.println(String.format("Final score: %d", fScore/(agents.size()-1)));
            System.out.println();
        }
    }
}
