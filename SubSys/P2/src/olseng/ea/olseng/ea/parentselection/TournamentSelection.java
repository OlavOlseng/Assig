package olseng.ea.olseng.ea.parentselection;

import olseng.ea.Genotype;
import olseng.ea.Phenotype;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olav on 08.03.2015.
 */
public class TournamentSelection implements ParentSelector {

    /**
     * Participants pr tournament.
     */
    public int k;
    /**
     * The chance a random phenotype wins the tournament
     */
    public double randomRate;
    private List<Phenotype> pool;
    double avgUtil = 0;

    public TournamentSelection(int K, double randomRate) {
        this.k = K;
        this.randomRate = randomRate;
    }

    @Override
    public List<Genotype> getParents(boolean sexual) {
        if (this.pool == null) {
            throw new InvalidStateException("ParentSelector not properly initialized");
        }
        List<Genotype> genotypes = new ArrayList<Genotype>();


        Phenotype best;
        List<Phenotype> tournament = getTournament(null);

        if(Math.random() < randomRate) {
            int index = (int)(Math.random() * tournament.size());
            best = tournament.get(index);
        }
        else {
            best = getBest(tournament);
        }
        genotypes.add(best.genotype);

        if(sexual) {
            tournament = getTournament(best);
            if(Math.random() < randomRate) {
                int index = (int)(Math.random() * tournament.size());
                best = tournament.get(index);
            }
            else {
                best = getBest(tournament);
            }
            genotypes.add(best.genotype);
        }
        return genotypes;
    }

    @Override
    public void initialize(List<Phenotype> adultPool) {
        this.pool = adultPool;
        this.k = Math.min(k, pool.size());
        for (Phenotype p : pool) {
            avgUtil += p.getUtilty();
        }
        avgUtil /= pool.size();
    }

    @Override
    public double getAvgUtility() {
        return avgUtil;
    }

    private List<Phenotype> getTournament(Phenotype drop) {
        ArrayList<Phenotype> tournament = new ArrayList<Phenotype>(pool.size());
        tournament.addAll(pool);
        tournament.remove(drop);

        //OMGOMGOMGOMG
        while(tournament.size() > k) {
            int removeIndex = (int)(Math.random() * tournament.size());
            tournament.remove(removeIndex);
        }
        return tournament;
    }

    private Phenotype getBest(List<Phenotype> tournament) {
        Phenotype best = null;
        double bestUtil = -1000000000;
        for (Phenotype p : tournament) {
            if (p.getUtilty() < bestUtil) {
                continue;
            }
            else if(p.getUtilty() == bestUtil) {
                if(Math.random() < 0.5) {
                    continue;
                }
            }
            best = p;
            bestUtil = p.getUtilty();
        }
        return best;
    }
}
