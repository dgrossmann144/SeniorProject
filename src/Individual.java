public class Individual
{
	public static int geneLength;
	/**
	 * 0 = up<br>
	 * 1 = left<br>
	 * 2 = down<br>
	 * 3 = right
	 */
	public int[] genes = new int[geneLength];
	public double fitness = 0;
	public int apples, spaces;
	
	public Individual(int geneLength)
	{
		setGeneLength(geneLength);
		apples = 0;
		spaces = 1;
	}
	
	/**
	 * Creates random genes
	 */
	public void generate()
	{
		for(int x = geneLength - 10; x < geneLength; x++)
		{
			genes[x] = (int) (Math.random() * 4);
		}
	}
	
	/**
	 * @return The fitness of the individual
	 */
	public double getFitness()
	{
		if(fitness == 0 && spaces != 0)
		{
			fitness = Math.pow(apples, 2) / Math.pow(spaces, 2);
		}
		return fitness;
	}
	
	/**
	 * @param index of gene
	 * @return gene at index
	 */
    public int getGene(int index)
    {
        return genes[index];
    }
    
    /**
     * @param index of replaced gene
     * @param newGene new gene value
     */
    public void setGene(int index, int newGene)
    {
    	genes[index] = newGene;
    }
    
    public static void setGeneLength(int newLength)
    {
    	geneLength = newLength;
    }
    
    /**
     * @return Number of genes
     */
    public int size()
    {
    	return geneLength;
    }
    
    /**
     * @return String of genes
     */
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