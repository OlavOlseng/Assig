package olseng.ea;

/**
 * Should convert from G to P
 * Created by Olav on 03/03/2015.
 */
public interface DevelopmentalMethod<G extends Genotype, P extends Phenotype<G>> {
    public P getPhenotype(G g);
}
