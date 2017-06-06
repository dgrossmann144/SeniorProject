public class Population
{
	private Individual individuals[];
	
	/**
	 * @param size number of individuals in the population
	 * @param initialize whether individuals should be randomly given genes
	 */
	public Population(int size, boolean initialize)
	{
		individuals = new Individual[size];
		if(initialize)
		{
			for(int x = 0; x < size; x++)
			{
				Individual newIndividual = new Individual(Individual.geneLength);
				newIndividual.generate();
				saveIndividual(x, newIndividual);
			}
		}
	}
	
	/**
	 * @param x index of individual
	 * @return Individual at index x
	 */
	public Individual getIndividual(int x)
	{
		return individuals[x];
	}
	
	/**
	 * @return Number of individuals in population
	 */
	public int size()
	{
		return individuals.length;
	}
	
	/**
	 * @param x index of individual to be overwritten
	 * @param individual new individual to replace index
	 */
	public void saveIndividual(int x, Individual individual)
	{
		individuals[x] = individual;
	}
	
	/**
	 * @return the fittest individual in the population
	 */
	public int getFittest()
	{
		int index = 0;
		for(int x = 1; x < this.size(); x++)
		{
			if(getIndividual(x).getFitness() > getIndividual(index).getFitness())
			{
				index = x;
			}
		}
		return index;
	}
}