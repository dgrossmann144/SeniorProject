import java.util.HashSet;
import java.util.Set;

public class Algorithims
{
	/**
	 * Chance a gene will be mutated
	 */
	public static double mutationRate = .1;
	/**
	 * Number of individuals selected to go through tournament algorithm
	 */
	public static final int TOURNAMENT_SIZE = 5;
	
	/**
	 * @param pop population to evolve
	 * @return New evolved population
	 */
	public static Population evolve(Population pop)
	{
		Population newPop = new Population(pop.size(), false);

		newPop.saveIndividual(0, pop.getIndividual(pop.getFittest()));

		for (int i = 1; i < pop.size(); i++)
		{
			Individual individual1 = tournamentSelection(pop);
			Individual individual2 = tournamentSelection(pop);
			Individual newIndividual = crossover(individual1, individual2);
			newPop.saveIndividual(i, newIndividual);
			mutate(newPop.getIndividual(i));
		}
		
		return newPop;
	}
	
	/**
	 * @param individual1 first individual to crossover
	 * @param individual2 second individual to crossover
	 * @return an individual which is a combination of individual1 and individual2
	 */
	public static Individual crossover(Individual individual1, Individual individual2)
	{
		Individual newIndividual = new Individual(Individual.geneLength);
		
		for(int x = 0; x < individual1.size(); x++)
		{
			if(Math.random() <= .5)
			{
				newIndividual.setGene(x, individual1.getGene(x));
			} else
			{
				newIndividual.setGene(x, individual2.getGene(x));
			}
		}
		
		return newIndividual;
	}
	
	/**
	 * @param individual to be mutated
	 */
	public static void mutate(Individual individual)
	{
		for(int x = 0; x < individual.size(); x++)
		{
			if(Math.random() < mutationRate)
			{
				individual.setGene(x, (int) (Math.random() * 4));
			}
		}
	}
	
	/**
	 * Selects random individuals and returns one with best fitness
	 * Uses set to prevent duplication
	 * @param pop population to select individuals from
	 * @return Most fit individual from a small sample
	 */
	public static Individual tournamentSelection(Population pop)
	{
		Set<Integer> indexes = new HashSet<Integer>();
		Population population = new Population(TOURNAMENT_SIZE, false);
		
		int random;
		while(indexes.size() < TOURNAMENT_SIZE)
		{
			random = (int)(Math.random() * pop.size());
			if(indexes.add(random))
			{
				population.saveIndividual(indexes.size() - 1, pop.getIndividual(random));
			}
		}
		
		return population.getIndividual(population.getFittest());
	}

}