import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Driver extends JPanel
{
	
	private BufferedImage path = null;
	private BufferedImage fruit = null;
	private BufferedImage greenPath = null;
	private BufferedImage yoshi = null;
	private static Point pos = new Point(0, 0);
	private static int numFruitLeft; // number of fruit left on the map
	/**
	 * 0 = path<br>
	 * 1 = fruit<br>
	 * 2 = completed path<br>
s	 */
	private static int[][] grid = new int[30][30];
	
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame ("Santa Fe Ant Problem");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Driver panel = new Driver();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		reset();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){public void run(){panel.tick();panel.repaint();
		}}, 0, 1000/2);//handles tick and repainting the jframe
	}
	
	public Driver()
	{
		this.setPreferredSize(new Dimension(960, 960));
		setFocusable(true);
		requestFocus();
	}
	
	private void tick()
	{
		pos.setLocation(pos.getX() + 1, pos.getY() + 1);
		updateGrid();
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
	/**
	 * Updates the grid to reflect the movement of the character.
	 */
	private void updateGrid()
	{
		if (grid[pos.x][pos.y] == 1)
			numFruitLeft--;
		grid[pos.x][pos.y] = 2;
	}
	
	private static void readGrid()
	{
		try
		{
			Scanner scan = new Scanner(new File("C:\\Users\\Danny G\\workspace\\SeniorProject\\assets\\Grid.txt"));
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
		pos.setLocation(0, 0);
	}
}
