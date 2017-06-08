import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Driver2 extends JPanel implements KeyListener
{
	private static final long serialVersionUID = 1L;
	private BufferedImage path = null;
	private BufferedImage fruit = null;
	private BufferedImage greenPath = null;
	private BufferedImage yoshi = null;
	private static Point pos = new Point(0, 0);
	private static Point startPos = new Point(0, 0);
	private static Point currentStartPos = new Point(0, 0);
	/**
	 * Stores the initial board for resetting
	 */
	private static int[][] gridInput = new int[32][32];
	/**
	 * User interface
	 */
	private static Driver2 panel;
	private static int speed = 1;
	private static int collectedCount = 0;
	private static int numFruitLeft;
	private static int totalFruit = 0;
	/**
	 * Index of population
	 */
	private static int popNum = 1;
	/**
	 * Current population
	 */
	private static Population pop;
	/**
	 * 0 = path<br>
	 * 1 = fruit<br>
	 * 2 = completed path<br>
	 */
	private static int[][] grid = new int[32][32];
	/**
	 * Keeps track of current max fitness
	 */
	private static double currentMaxFitness = 0;
	/**
	 * Number of times fitness has been repeated
	 */
	private static double fitnessCount = 0;
	private static String curBestPath = "";
	private static final int MAX_GENE_LENGTH = 200;
	private static int applesInFittest = 0;
	
	public static void main(String[] args) throws InterruptedException 
	{
		//Initializes window for drawing to
		JFrame frame = new JFrame ("Santa Fe Ant Problem");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new Driver2();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		panel.readImages();
		
		Individual.setGeneLength(20);
		pop = new Population(100, true);
		double fitness = 0;
		readGrid();
		
		//Main loop 
		while(collectedCount < totalFruit)
		{
			fitness = calcFitness();
			if(fitness > currentMaxFitness)
			{
				currentMaxFitness = fitness;
				fitnessCount = 0;
			} else
			{
				fitnessCount++;
			}
			if(fitnessCount >= 1000 && Individual.geneLength < MAX_GENE_LENGTH)
			{
				collectedCount += applesInFittest;
				curBestPath += pop.getIndividual(pop.getFittest()).getGenes();
				pop = new Population(pop.size(), true);
				updateStartPos();
				fitnessCount = 0;
			}
		}
		System.out.println("Done :)");
	}
	
	public Driver2()
	{
		addKeyListener(this);
		this.setPreferredSize(new Dimension(1014, 1014));
		setFocusable(true);
		requestFocus();
	}
	
	private static double calcFitness()
	{
		//Loops through individuals
		indivLoop:for(int indiv = 0; indiv < pop.size(); indiv++)
		{
			//Resets the grid between individuals
			numFruitLeft = totalFruit;
			for(int x = 0; x < grid.length; x++)
			{
				for(int y = 0; y < grid[x].length; y++)
				{
					grid[x][y] = gridInput[x][y];
				}
			}
			pos.setLocation(startPos.x, startPos.y);
			
			//Checks first space for fruit
			if (grid[pos.x][pos.y] == 1)
			{
				numFruitLeft--;
				pop.getIndividual(indiv).addApple(pos);
			}
			grid[pos.x][pos.y] = 2;
			
			for(int gene = 0; gene < pop.getIndividual(indiv).size(); gene++)
			{
				//Controls movement based on gene
				switch(pop.getIndividual(indiv).getGene(gene))
				{
					case 0:
						pos.setLocation(pos.getX(), pos.getY() - 1);
						break;
					case 1:
						pos.setLocation(pos.getX() - 1, pos.getY());
						break;
					case 2:
						pos.setLocation(pos.getX(), pos.getY() + 1);
						break;
					case 3:
						pos.setLocation(pos.getX() + 1, pos.getY());
						break;
				}
				
				if(bound(pos))
				{
					//Stops current individual
					pop.getIndividual(indiv).spaces = MAX_GENE_LENGTH;
					continue indivLoop;
				} else
				{
					//Collects a fruit if pos is on a fruit
					if (grid[pos.x][pos.y] == 1)
					{
						numFruitLeft--;
						pop.getIndividual(indiv).addApple(pos);
					}
					//Stops if it retraces path
//					if(grid[pos.x][pos.y] == 2)
//					{
//						pop.getIndividual(indiv).spaces = MAX_GENE_LENGTH;
//						continue indivLoop;
//					}
					//Stops if all fruit are collected
					if(numFruitLeft == 0)
					{
						pop.getIndividual(indiv).spaces++;
						continue indivLoop;
					}
					else //Empty space
					{
						grid[pos.x][pos.y] = 2;
						pop.getIndividual(indiv).spaces++;
					}
				}
			}
		}
	
		double fitness = pop.getIndividual(pop.getFittest()).getFitness();
		reset();
		return fitness;
	}
	
	private static void reset()
	{
		showFittest(pop.getIndividual(pop.getFittest()).getGenes());
		applesInFittest = pop.getIndividual(pop.getFittest()).getApples();
		
		//Evolves next population
		Population newPop = Algorithims.evolve(pop);
		pop = newPop;
		popNum++;
	}
	
	private void readImages()
	{
		try
		{
			path = ImageIO.read(new File("assets/Path.png"));
			fruit = ImageIO.read(new File("assets/Fruit.png"));
			greenPath = ImageIO.read(new File("assets/GreenPath.png"));
			yoshi = ImageIO.read(new File("assets/Yoshi.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//Prints grid
		for(int x = 0; x < 32; x++)
		{
			for(int y = 0; y < 32; y++)
			{
				switch(grid[x][y])
				{
					case 0:
						g.drawImage(path, x*32, y*32, 32, 32, null);
						break;
					case 1:
						g.drawImage(fruit, x*32, y*32, 32, 32, null);
						break;
					case 2:
						g.drawImage(greenPath, x*32, y*32, 32, 32, null);
						break;
					default:
						g.drawImage(path, x*32, y*32, 32, 32, null);
				}
			}
		}
		if(pos.x == 0 && pos.y == 0 && curBestPath.length() > 0)
		{
			System.out.println("Position error");
		}
		g.drawImage(yoshi, (int)pos.getX() * 32, (int)pos.getY() * 32, 32, 32, null);
	}
	
	/**
	 * Reads input grid to store fruit locations
	 * (Called once)
	 */
	private static void readGrid()
	{
		try
		{
			Scanner scan = new Scanner(new File("assets\\Grid.txt"));
			String[] input = new String[32];
			for(int x = 0; x < grid.length; x++)
			{
				
				input = scan.nextLine().split(" ");
				for(int y = 0; y < grid[x].length; y++)
				{
					grid[y][x] = Integer.parseInt(input[y]);
					gridInput[y][x] = grid[y][x];
					if(Integer.parseInt(input[y]) == 1)
					{
						totalFruit++;
					}
				}
			}
			numFruitLeft = totalFruit;
			scan.close();
			panel.repaint();
		} catch (FileNotFoundException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Used to control simulation speed
	 */
	public void keyPressed(KeyEvent key)
	{
		if(key.getKeyCode() == KeyEvent.VK_EQUALS && speed < Integer.MAX_VALUE/2)
		{
			speed *= 2;
		}
		if(key.getKeyCode() == KeyEvent.VK_MINUS && speed > 1)
		{
			speed /= 2;
		}
	}

	public void keyReleased(KeyEvent arg0)
	{
		
	}
	
	public void keyTyped(KeyEvent arg0)
	{
		
	}
	
	/**
	 * @return Whether the position is in the grid
	 */
	private static boolean bound(Point p)
	{
		if(p.getX() < 0)
		{
			return true;
		} else if(p.getX() > 31)
		{
			return true;
		}
		if(p.getY() < 0)
		{
			return true;
		} else if(p.getY() > 31)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Changes where the ant starts on the grid
	 */
	private static void updateStartPos()
	{
		startPos.setLocation(0, 0);
		for(int x = 0; x < curBestPath.length(); x++)
		{
			gridInput[startPos.x][startPos.y] = 2;
			switch(Integer.parseInt(curBestPath.charAt(x) + ""))
			{
				case 0:
					startPos.setLocation(startPos.getX(), startPos.getY() - 1);
					break;
				case 1:
					startPos.setLocation(startPos.getX() - 1, startPos.getY());
					break;
				case 2:
					startPos.setLocation(startPos.getX(), startPos.getY() + 1);
					break;
				case 3:
					startPos.setLocation(startPos.getX() + 1, startPos.getY());
					break;
			}
			setToEdge(startPos);
		}
		currentStartPos.setLocation(startPos);
	}
	
	/**
	 * Displays the current best path
	 * @param genes Current best path of instructions
	 */
	private static void showFittest(String genes)
	{
		for(int x = 0; x < grid.length; x++)
		{
			for(int y = 0; y < grid[x].length; y++)
			{
				grid[x][y] = gridInput[x][y];
			}
		}
		pos.setLocation(startPos);
		numFruitLeft = totalFruit;
		grid[pos.x][pos.y] = 2;
		
		for(int gene = 0; gene < genes.length(); gene++)
		{
			panel.repaint();
			try {
				Thread.sleep(1000/speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch(Integer.parseInt(genes.charAt(gene) + ""))
			{
				case 0:
					pos.setLocation(pos.getX(), pos.getY() - 1);
					break;
				case 1:
					pos.setLocation(pos.getX() - 1, pos.getY());
					break;
				case 2:
					pos.setLocation(pos.getX(), pos.getY() + 1);
					break;
				case 3:
					pos.setLocation(pos.getX() + 1, pos.getY());
					break;
			}
			if(bound(pos))
			{
				break;
			}
//			if(grid[pos.x][pos.y] == 2)
//			{
//				break;
//			}
			if (grid[pos.x][pos.y] == 1)
			{
				numFruitLeft--;
			}
			if(numFruitLeft == 0)
			{
				break;
			}
			grid[pos.x][pos.y] = 2;
		}
	}
	
	private static void setToEdge(Point p)
	{
		if(p.getX() < 0)
		{
			p.setLocation(0, p.y);
		} else if(p.getX() > 31)
		{
			p.setLocation(31, p.y);
		}
		if(p.getY() < 0)
		{
			p.setLocation(p.x, 0);
		} else if(p.getY() > 31)
		{
			p.setLocation(p.x, 31);
		}
	}
}