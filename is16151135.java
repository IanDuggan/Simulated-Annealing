import java.util.*;
import java.io.*;
import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;

public class is16151135
{
	private static int matrix[][];
	private static int vertices;
	private static double temperature, coolingRate, fitness;
	private static final double cool = 8.0;
	
	private static JTextField tField;
	private static JTextField cField;
	private static JButton button;
	
	private static ArrayList <Integer> order;
	private static ArrayList <Double> evo = new ArrayList<Double>();
	private static ArrayList<ArrayList<Integer>> numbers = new ArrayList<ArrayList<Integer>>();
	private static double chunk;
	
	private static JPanel container = new JPanel();
	private static JPanel vPanel = new JPanel();
	
	public static void main(String[] args) throws IOException
	{
		start();
	}
	
	public static void start() throws IOException
	{
		JFrame f = new JFrame();
		f.setTitle("SA");
		
		JPanel tPanel = new JPanel();
		JLabel tLabel = new JLabel("Temperature: ");
		tField = new JTextField("",10);
		tPanel.add(tLabel);
		tPanel.add(tField);
		
		JPanel cPanel = new JPanel();
		JLabel cLabel = new JLabel("Cooling rate: ");
		cField = new JTextField("",10);
		tPanel.add(cLabel);
		tPanel.add(cField);
		
		button = new JButton("Confirm");
		
		button.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					temperature = Double.parseDouble(tField.getText());
					coolingRate = Double.parseDouble(cField.getText());
					
					String filename = "GA2018-19.txt";
					File inputfile = new File(filename);
					
					String fileElements[];
					
					numbers.add(new ArrayList <Integer>());
					numbers.add(new ArrayList <Integer>());
					
					Scanner in;
					in = new Scanner(inputfile);
					int counter = 0;
					int max = 0;
					while (in.hasNext())
					{
						fileElements = in.nextLine().split(" ");
						int num1 = Integer.parseInt(fileElements[0]);
						int num2 = Integer.parseInt(fileElements[1]);
						
						if (num1 > num2)
						{
							if (num1 > max)
							{
								max = num1;
							}
						}
						else
						{
							if (num2 > max)
							{
								max = num2;
							}
						}
						numbers.get(0).add(num1);
						numbers.get(1).add(num2);
						counter++;
					}
					
					matrix = new int[max+1][max+1];
					vertices = max + 1;
					
					
					for (int i = 0; i < numbers.get(0).size(); i++)
					{
						int num1 = numbers.get(0).get(i);
						int num2 = numbers.get(1).get(i);
						
						matrix[num1][num2] = 1;
						matrix[num2][num1] = 1;
					}
					
					order = new ArrayList <Integer>();
					for (int i = 0; i < max + 1; i++)
					{
						int temp = (int) (Math.random() * (max + 1));
						while (order.contains(temp))
						{
							temp = (int) (Math.random() * (max + 1));
						}
						order.add(temp);
					}
					
					double currentFitness;
					currentFitness = fitness(order);
					
					while (temperature > cool)
					{
						ArrayList <Integer> tempOrder = order;
						double tempFitness;
						for (int i = 0; i < temperature; i++)
						{
							tempOrder = mutation(tempOrder);
							tempFitness = fitness(tempOrder);
							if (tempFitness < currentFitness)
							{
								currentFitness = tempFitness;
								order = tempOrder;
							}
						}
					
						temperature -= coolingRate;
						evo.add(currentFitness);
						System.out.println(currentFitness);
					}
					visual(matrix,order,chunk);
				}
				catch(NumberFormatException ex)
				{
					System.err.println("Please only use numbers");
				}
				catch(IOException ex)
				{
					System.err.println("Error IO exception");
				}
			}
		});
		
		container.add(tPanel);
		container.add(cPanel);
		container.add(button);
		f.add(container);
		f.pack();
		f.setVisible(true);
	}
	
	public static void visual(int[][] matrix, ArrayList <Integer> order, double chunk)
	{
		new Visualisation(matrix, order, chunk);
	}
	
	
	
	public static double fitness(ArrayList <Integer> order)
	{
		double nodes[][] = new double[order.size()][2];
		double fitness = 0.0;
		chunk = (2 * Math.PI)/Math.abs(order.size());
		
		for (int i = 0; i < order.size(); i++)
		{
			double x = Math.cos(i * chunk);
			double y = Math.sin(i * chunk);
			nodes[i][0] = x;
			nodes[i][1] = y;
		}
		
		for (int i = 0; i < order.size(); i++)
		{
			for (int j = i + 1; j < order.size(); j++)
			{
				if(matrix[order.get(i)][order.get(j)] == 1)
				{
					double x1 = nodes[i][0];
					double y1 = nodes[i][1];
					double x2 = nodes[j][0];
					double y2 = nodes[j][1];
					
					double edgeLength = Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
					fitness += edgeLength;
				}
			}
		}
		return fitness;
	}
	
	public static ArrayList<Integer> mutation(ArrayList <Integer> temp)
	{
		int firstIndex = (int) (Math.random() * temp.size());
		int secondIndex = (int) (Math.random() * temp.size());
		
		while (firstIndex == secondIndex)
		{
			firstIndex = (int) (Math.random() * temp.size());
			secondIndex = (int) (Math.random() * temp.size());
		}
	
		int temp1 = temp.get(firstIndex);
		int temp2 = temp.get(secondIndex);
		temp.set(firstIndex,temp2);
		temp.set(secondIndex,temp1);
		
		return temp;
	}
}

class Visualisation extends JFrame
{
	private int[][] matrix;
	private ArrayList <Integer> order;
	private int vertices = 0;
	private double chunk;
	
	public Visualisation(int[][] matrix, ArrayList <Integer> order, double chunk)
	{
		this.matrix = matrix;
		this.order = order;
		this.chunk = chunk;
		this.vertices = order.size();
		this.setSize(600,600);
		this.setTitle("Visual");
		setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		int radius = 100;
		int mov = 200;
		
		for (int i = 0; i < vertices; i++)
		{
			for (int j = i + 1; j < vertices; j++)
			{
				if(matrix[order.get(i)][order.get(j)] == 1)
				{
					g.drawLine(
						(int)(((double) Math.cos(i * chunk)) * radius + mov),
						(int)(((double) Math.sin(i * chunk)) * radius + mov),
						(int)(((double) Math.cos(j * chunk)) * radius + mov),
						(int)(((double) Math.sin(j * chunk)) * radius + mov));
				}
			}
		}
	}
}