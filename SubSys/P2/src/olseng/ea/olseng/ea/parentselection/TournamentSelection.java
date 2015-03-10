package olseng.ea.olseng.ea.parentselection;

import olseng.ea.Genotype;
import olseng.ea.Phenotype;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.ArrayList;
import java.util.Collections;
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
            best = tournament.get(0);
        }
        genotypes.add(best.genotype.dupe());

        if(sexual) {
            tournament = getTournament(best);
            if(Math.random() < randomRate) {
                int index = (int)(Math.random() * tournament.size());
                best = tournament.get(index);
            }
            else {
                best = tournament.get(0);

            }
            genotypes.add(best.genotype.dupe());
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

        //OMGOMGOMGOMG
        if (k > pool.size() / 2) {
            tournament.addAll(pool);
            tournament.remove(drop);
            while(tournament.size() > k) {
                int removeIndex = (int)(Math.random() * tournament.size());
                tournament.remove(removeIndex);
            }
        }
        else {
            while (tournament.size() < k) {
                Phenotype p = pool.get((int)(Math.random() * pool.size()));
                if (tournament.contains(p) || p == drop) {
                    continue;
                }
                tournament.add(p);
            }
        }
        Collections.sort(tournament);
        return tournament;
    }
}
