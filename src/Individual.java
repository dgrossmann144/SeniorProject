public class Individual
{
	public static final int geneLength = 200;
	/**
	 * 0 = up<br>
	 * 1 = left<br>
	 * 2 = down<br>
	 * 3 = right
	 */
	public int[] genes = new int[geneLength];
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
			fitness = Math.pow(apples, 2) / Math.pow(spaces, 2);
		}
		return fitness;
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