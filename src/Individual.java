
public class Individual
{
	private static int geneLength = 10;
	/**
	 * 0 = up<br>
	 * 1 = left<br>
	 * 2 = down<br>
	 * 3 = right
	 */
	private int[] genes = new int[geneLength];
	private double fitness = 0;
	private int apples, spaces;
	
	public Individual()
	{
		apples = 0;
		spaces = 0;
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
		if(fitness == 0)
		{
			fitness = apples / spaces;
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
    
    public void setApples(int apples)
    {
    	this.apples = apples;
    }
    
    public int getApples()
    {
    	return this.apples;
    }
    
    public int getSpaces()
    {
    	return this.spaces;
    }
    
    public void setSpaces(int spaces)
    {
    	this.spaces = spaces;
    }
    
}
