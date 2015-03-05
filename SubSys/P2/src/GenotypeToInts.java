import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olav on 03/03/2015.
 */
public class GenotypeToInts implements DevelopmentalMethod<List<Integer>>{

    @Override
    public List<Integer> getPhenotype(Genotype g) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < g.geneCount * g.geneSize; i++) {
            int val = 0;
            if (g.genotype.get(i)) {
                val = 1;
            }
            list.add(val);
        }
        return list;
    }

    public static void main(String[] args) {
        Genotype g = new Genotype(1,8).randomize();
        DevelopmentalMethod dm = new GenotypeToInts();
        System.out.println(dm.getPhenotype(g));
    }
}


