
public class Individual
{
	private static int geneLength = 600;
	/**
	 * 0 = up<br>
	 * 1 = left<br>
	 * 2 = down<br>
	 * 3 = right
	 */
	private int[] genes = new int[geneLength];
	public double fitness = 0;
	public int apples, spaces;
	
	public Individual()
	{
		apples = 0;
		spaces = 1;
	}
	
	public void generate()
	{
		for(int x = 0; x < geneLength; x++)
		{
			genes[x] = (int) (Math.random() * 4);
		}
	}
	
	public double getFitness()
	{
		if(fitness == 0 && spaces != 0)
		{
			fitness = (double)apples / spaces;
		}
		return fitness;
	}
	
	public static void setDefaultGeneLength(int length)
	{
        geneLength = length;
    }
    
    public int getGene(int index)
    {
        return genes[index];
    }
    
    public void setGene(int index, int gene)
    {
    	genes[index] = gene;
    }
    
    public int size()
    {
    	return geneLength;
    }
    
    public String toString()
    {
    	String result = "";
    	for(int x = 0; x < genes.length; x++)
    	{
    		result += genes[x];
    	}
    
    	return result;
    }
}
