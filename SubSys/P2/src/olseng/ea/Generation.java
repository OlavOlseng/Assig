package olseng.ea;

import olseng.ea.olseng.ea.bitvec.BinaryGenome;
import olseng.ea.olseng.ea.bitvec.BitToIntVec;
import olseng.ea.olseng.ea.bitvec.MaxOneEvaluator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by Olav on 03.03.2015.
 */
public class Generation<G extends Genotype> {

    //Problem specific classes
    List<G> genotypes;
    List<Phenotype<G>> phenotypes;
    DevelopmentalMethod<G, Phenotype<G>> dm;
    FitnessEvaluator<Phenotype<G>> fe;
    GeneticOperators<G> go;

    public Generation(DevelopmentalMethod dm, FitnessEvaluator fe, GeneticOperators go){
        this.dm = dm;
        this.fe = fe;
        this.go = go;
    }

    public Generation(DevelopmentalMethod dm, FitnessEvaluator fe, GeneticOperators<G> go, List<G> genotypes) {
        this(dm, fe, go);
        this.genotypes = genotypes;
    }

    /**
     * Develops all genotypes in the population to phenotypes.
     */
    public void developGenotypes() {
        this.phenotypes = new ArrayList<Phenotype<G>>();
        for (G g : genotypes) {
            phenotypes.add(dm.getPhenotype(g));
        }
    }

    /**
     * Evaluates all phenotypes developed from the population.
     * Must have called developGenotypes() before calling this function, else
     * @throws java.lang.NullPointerException
     */
    public void evaluatePhenotypes(List<Phenotype<G>> phenotypes) {
        for (Phenotype p : phenotypes) {
            //if (p.getUtilty() == 0.0) {
                p.setUtility(fe.evaluate(p));
            //}
        }
    }

    public GeneticOperators<G> getGeneticOperators() {
        return this.go;
    }

    //=================================TEST===============================
    public static void main(String[] args) {
        List<BinaryGenome> gs = new ArrayList<BinaryGenome>();
        for (int i = 0; i < 2; i++) {
            BinaryGenome g = new BinaryGenome(20,1);
            g.randomize();
            gs.add(g);
        }
        BitSet bs = new BitSet(4);
        bs.set(0,20);
        gs.add(new BinaryGenome(20,1, bs));

        Generation<BinaryGenome> p = new Generation<BinaryGenome>(new BitToIntVec(), new MaxOneEvaluator(), null, gs);
        p.developGenotypes();
        p.evaluatePhenotypes(p.phenotypes);
        for(Phenotype ph : p.phenotypes) {
            System.out.println(ph);
        }
    }
}
