
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
	
	public Individual getFittest()
	{
		System.out.println("Start get fittest");
		int index = 0;
		System.out.println("This.size() " + this.size());
		System.out.println(getIndividual(1));
		for(int x = 1; x < this.size(); x++)
		{
			System.out.println("In for loop" + x);
			if(getIndividual(x).getFitness() > getIndividual(index).getFitness())
			{
				index = x;
			}
		}
		return getIndividual(index);
	}
}
