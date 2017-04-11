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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
	private static Driver panel;
	private static int speed;
	private static int numFruitLeft; // number of fruit left on the map
	private static Population pop;
	private static int popNum = -1;
	private static int geneNum = 0;
	/**
	 * 0 = path<br>
	 * 1 = fruit<br>
	 * 2 = completed path<br>
s	 */
	private static int[][] grid = new int[30][30];
	
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
		pop = new Population(20, true);
		reset();
		tick();
	}
	
	public Driver()
	{
		addKeyListener(this);
		this.setPreferredSize(new Dimension(960, 960));
		setFocusable(true);
		requestFocus();
		grid[0][0] = 2; // first tile cannot be an apple and is already traversed
	}
	
	private static void tick()
	{
		switch(pop.getIndividual(popNum).getGene(geneNum))
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
		bound();
		geneNum++;
		if(geneNum >= pop.getIndividual(popNum).size())
		{
			System.out.println("Reset");
			reset();
		}
		if (grid[pos.x][pos.y] == 1)
		{
			numFruitLeft--;
			pop.getIndividual(popNum).apples++;
		}
		grid[pos.x][pos.y] = 2;
		pop.getIndividual(popNum).spaces++;
		
		if(numFruitLeft == 0)
		{
			reset();
		}
		startTimer();
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
		
		for(int x = 0; x < 30; x++)
		{
			for(int y = 0; y < 30; y++)
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
			String[] input = new String[30];
			for(int x = 0; x < grid.length; x++)
			{
				
				input = scan.nextLine().split(" ");
				for(int y = 0; y < grid[x].length; y++)
				{
					grid[y][x] = Integer.parseInt(input[y]);
					if(Integer.parseInt(input[y]) == 1)
					{
						numFruitLeft++;
					}
				}
			}
			scan.close();
		} catch (FileNotFoundException e)
		{
			System.out.println(e);
		}
	}
	
	private static void reset()
	{
		numFruitLeft = 0;
		readGrid();
		grid[0][0] = 2;
		pos.setLocation(0, 0);
		geneNum = 0;
		popNum++;
		if(popNum >= pop.size())
		{
			Population newPop = new Population(pop.size(), false);
		}
	}

	public void keyPressed(KeyEvent key)
	{
		if(key.getKeyCode() == KeyEvent.VK_EQUALS)
		{
			speed++;
		}
		if(key.getKeyCode() == KeyEvent.VK_MINUS && speed > 1)
		{
			speed--;
		}
	}

	public void keyReleased(KeyEvent arg0)
	{
		
	}

	public void keyTyped(KeyEvent arg0)
	{
		
	}
	
	private static void startTimer()
	{
		Runnable runnable = new Runnable()
		{
			public void run()
			{
				panel.tick();
				panel.repaint();
			}
		};
		Executors.newSingleThreadScheduledExecutor().schedule(runnable, 1000/speed, TimeUnit.MILLISECONDS);
	}
	
	private static void bound()
	{
		if(pos.getX() < 0)
		{
			pos.setLocation(0, pos.getY());
		}
		if(pos.getX() > 29)
		{
			pos.setLocation(29, pos.getY());
		}
		if(pos.getY() < 0)
		{
			pos.setLocation(pos.getX(), 0);
		}
		if(pos.getY() > 29)
		{
			pos.setLocation(pos.getX(), 29);
		}
	}
	
	
}
