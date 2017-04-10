
public class Population
{
	private Individual individuals[];
	
	public Population(int size, boolean initialize)
	{
		individuals = new Individual[size];
		if(initialize)
		{
			for(int x = 0; x < size; x++)
			{
				Individual newIndividual = new Individual();
				newIndividual.generate();
				saveIndividual(x, newIndividual);
			}
		}
	}
	
	public Individual getIndividual(int x)
	{
		return individuals[x];
	}
	
	public int size()
	{
		return individuals.length;
	}
	
	public void saveIndividual(int x, Individual individual)
	{
		individuals[x] = individual;
	}
}
