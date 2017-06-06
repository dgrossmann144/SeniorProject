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

public class Driver extends JPanel implements KeyListener
{
	private static final long serialVersionUID = 1L;
	private BufferedImage path = null;
	private BufferedImage fruit = null;
	private BufferedImage greenPath = null;
	private BufferedImage yoshi = null;
	private static Point pos = new Point(0, 0);
	private static int[][] gridInput = new int[32][32];
	private static Driver panel;
	private static int speed;
	private static int numFruitLeft; // number of fruit left on the map
	private static int totalFruit;
	private static int popNum = 1;
	private static Population pop;
	private static boolean readGrid = false;
	/**
	 * 0 = path<br>
	 * 1 = fruit<br>
	 * 2 = completed path<br>
s	 */
	private static int[][] grid = new int[32][32];
	
	public static void main(String[] args) 
	{
		speed = 1;
		JFrame frame = new JFrame ("Santa Fe Ant Problem");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new Driver();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		pop = new Population(100, true);
		
		double fitness = tick();
		while(numFruitLeft != 0 && fitness < .5)
		{
			fitness = tick();
			System.out.println("Achieved a fitness of " + fitness);
		}
	}
	
	public Driver()
	{
		addKeyListener(this);
		this.setPreferredSize(new Dimension(1008, 1008));
		setFocusable(true);
		requestFocus();
	}
	
	private static double tick()
	{
		indivLoop:for(int indiv = 0; indiv < pop.size(); indiv++)
		{
			//Reset grid between individuals
			numFruitLeft = totalFruit;
			if(!readGrid)
			{
				readGrid();
				readGrid = true;
			} else
			{
				for(int x = 0; x < grid.length; x++)
				{
					for(int y = 0; y < grid[x].length; y++)
					{
						grid[x][y] = gridInput[x][y];
					}
				}
			}
			pos.setLocation(0, 0);
			
			if (grid[pos.x][pos.y] == 1)
			{
				numFruitLeft--;
				pop.getIndividual(indiv).apples++;
			}
			grid[pos.x][pos.y] = 2;
			
			for(int gene = 0; gene < pop.getIndividual(indiv).size(); gene++)
			{
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
				
				if(bound())
				{
					pop.getIndividual(indiv).spaces = pop.getIndividual(indiv).size();
					continue indivLoop;
				} else
				{
					if (grid[pos.x][pos.y] == 1)
					{
						numFruitLeft--;
						pop.getIndividual(indiv).apples++;
					}
					if(grid[pos.x][pos.y] == 2)
					{
						pop.getIndividual(indiv).spaces = pop.getIndividual(indiv).size();
						continue indivLoop;
					}
					if(numFruitLeft == 0)
					{
						pop.getIndividual(indiv).spaces++;
						continue indivLoop;
					} else
					{
						grid[pos.x][pos.y] = 2;
						pop.getIndividual(indiv).spaces++;
					}
				}
			}
		}
		return reset();
	}
	
	private static double reset()
	{
		System.out.println("Population " + popNum + " complete, max fitness: " + pop.getIndividual(pop.getFittest()).getFitness());
		System.out.println(pop.getIndividual(pop.getFittest()));
		System.out.println();
		popNum++;
		pos.setLocation(0, 0);
		for(int x = 0; x < grid.length; x++)
		{
			for(int y = 0; y < grid[x].length; y++)
			{
				grid[x][y] = gridInput[x][y];
			}
		}
		showFittest(pop.getIndividual(pop.getFittest()).genes);
		Population newPop = Algorithims.evolve(pop);
		pop = newPop;
		return pop.getIndividual(pop.getFittest()).getFitness();
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		try
		{
			path = ImageIO.read(new File("assets/Path.png"));
			fruit = ImageIO.read(new File("assets/Fruit.png"));
			greenPath = ImageIO.read(new File("assets/GreenPath.png"));
			yoshi = ImageIO.read(new File("assets/Yoshi.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		g.drawImage(yoshi, (int)pos.getX() * 32, (int)pos.getY() * 32, 32, 32, null);
	}
	
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
						numFruitLeft++;
					}
				}
			}
			totalFruit = numFruitLeft;
			scan.close();
		} catch (FileNotFoundException e)
		{
			System.out.println(e);
		}
	}

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
	
	private static boolean bound()
	{
		if(pos.getX() < 0)
		{
			return true;
		} else if(pos.getX() > 31)
		{
			return true;
		}
		if(pos.getY() < 0)
		{
			return true;
		} else if(pos.getY() > 31)
		{
			return true;
		}
		return false;
	}
	
	private static void showFittest(int[] genes)
	{
		numFruitLeft = totalFruit;
		grid[0][0] = 2;
		for(int gene = 0; gene < genes.length; gene++)
		{
			panel.repaint();
			try {
				Thread.sleep(1000/speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch(genes[gene])
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
			if(bound())
			{
				break;
			}
			if(grid[pos.x][pos.y] == 2)
			{
				break;
			}
			if (grid[pos.x][pos.y] == 1)
			{
				numFruitLeft--;
			}
//			System.out.println("Fruitleft: " + numFruitLeft);
			if(numFruitLeft == 0)
			{
				break;
			}
			grid[pos.x][pos.y] = 2;
		}
	}
}
//0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 1 0 0 0 0
//0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 1 0 0
//0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 1 0 0
//0 0 0 1 1 1 1 0 1 1 1 1 1 0 0 0 0 0 0 0 0 1 1 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 1 1 1 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
//0 0 0 1 1 0 0 1 1 1 1 1 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 1 0 0 0 0 0 0 1 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
//0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0