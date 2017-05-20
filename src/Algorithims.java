public class Algorithims
{
	public static final double MUTATION_RATE = .015;
	public static final int TOURNAMENT_SIZE = 5;
	
	public static Population evolve(Population pop)
	{
		Population newPop = new Population(pop.size(), false);

		newPop.saveIndividual(0, pop.getFittest());

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
	
	public static Individual crossover(Individual individual1, Individual individual2)
	{
		Individual newIndividual = new Individual();
		
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
	
	public static void mutate(Individual individual)
	{
		for(int x = 0; x < individual.size(); x++)
		{
			if(Math.random() < MUTATION_RATE)
			{
				individual.setGene(x, (int) (Math.random() * 4));
			}
		}
	}
	
	public static Individual tournamentSelection(Population pop)
	{
		Population tournament = new Population(TOURNAMENT_SIZE, false);
		
		for(int x = 0; x < tournament.size(); x++)
		{
			int id = (int) (Math.random() * pop.size());
			tournament.saveIndividual(x, pop.getIndividual(id));
		}
		
		return tournament.getFittest();
	}

}