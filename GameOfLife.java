import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;

/*
 * This program is an implementation of
 * Conway's Game of Life Simulation
 * Author: Andy Fleischer
 * Date: 9/24/2019
 */

public class GameOfLife implements MouseListener, ActionListener, Runnable {
	
	int cellRows = 10;
	int cellColumns = 10;
	boolean[][] cells;
	GameOfLifePanel panel;
	JFrame frame = new JFrame("Life simulation");
	Container south = new Container();
	JButton step = new JButton("Step");
	JButton start = new JButton("Start");
	JButton stop = new JButton("Stop");
	boolean running = false;
	Scanner scanner = new Scanner(System.in);
	
	//Constructor function
	public GameOfLife() {
		System.out.println("How many rows and columns would you like?");
		System.out.println("Rows: 2-50   Columns: 2-100");
		System.out.println("Separate two numbers by a comma and space, like so: R,C");
		//Gets user input of how many rows and columns they would like
		boolean findingRows = true;
		while (findingRows) {
			String input = scanner.nextLine();
			String[] stringValues = input.split(",");
			int[] numValues = new int[stringValues.length];
			for (int i = 0; i < stringValues.length; i++) {
				stringValues[i] = stringValues[i].trim();
				numValues[i] = Integer.parseInt(stringValues[i]);
			}
			//Makes sure the values are in the right range
			if (numValues[0] < 2 || numValues[0] > 50 || numValues[1] < 2 || numValues[1] > 100) {
				System.out.println("Numbers not in range! Enter another set of numbers");
			}
			else {//Numbers are good, create the cells array and panel
				cellRows = numValues[0];
				cellColumns = numValues[1];
				cells = new boolean[cellRows][cellColumns];
				panel = new GameOfLifePanel(cells);
				findingRows = false;
			}
		}
		
		/*
		 * Rows:
		2-5: 500p
		6-10: 600p
		11+: 900p
		*/
		
		//Make the rows and columns all even squares
		if (cellRows <= 5) {
			int x = (500 / cellRows) * cellColumns;
			frame.setSize(x, 540);
		}
		else if (cellRows <= 10) {
			int x = (600 / cellRows) * cellColumns;
			frame.setSize(x, 640);
		}
		else {
			int x = (900 / cellRows) * cellColumns;
			frame.setSize(x, 940);
		}
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		panel.addMouseListener(this);
		//South container
		south.setLayout(new GridLayout(1, 3));
		south.add(step);
		step.addActionListener(this);
		south.add(start);
		start.addActionListener(this);
		south.add(stop);
		stop.addActionListener(this);
		frame.add(south, BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	//Main method
	public static void main(String[] args) {
		new GameOfLife();
	}

	//On mouse released, get the cell of where you clicked and flip its value and color
	@Override
	public void mouseReleased(MouseEvent event) {
		double width = (double)panel.getWidth() / cells[0].length;
		double height = (double)panel.getHeight() / cells.length;
		int column = Math.min(cells[0].length - 1, (int)(event.getX() / width));
		int row = Math.min(cells.length - 1, (int)(event.getY() / height));
		
		cells[row][column] = !cells[row][column];
		frame.repaint();
	}
	
	//Action performed method (checks for step, start, and stop functions)
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(step)) {
			step();
		}
		if (event.getSource().equals(start)) {
			if (!running) {
				running = true;
				Thread t = new Thread(this);
				t.start();
			}
		}
		if (event.getSource().equals(stop)) {
			running = false;
		}
	}
	
	//While the user has not pressed stop, keep stepping 10 times per minute
	@Override
	public void run() {
		while (running) {
			step();
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	//Moves one step forward in the program
	public void step() {
		//Creates array for the next set of cells
		boolean[][] nextCells = new boolean[cells.length][cells[0].length];
		for (int row = 0; row < nextCells.length; row++) {
			for (int col = 0; col < nextCells[0].length; col++) {
				//Check how many neighbors are alive
				int aliveNeighbors = 0;
				if (row > 0 && col > 0 && cells[row-1][col-1]) {//up left
					aliveNeighbors++;
				}
				if (row > 0 && cells[row-1][col]) {//up
					aliveNeighbors++;
				}
				if (row > 0 && col < cells[0].length - 1 && cells[row-1][col+1]) {//up right
					aliveNeighbors++;
				}
				if (col > 0 && cells[row][col-1]) {//left
					aliveNeighbors++;
				}
				if (col < cells[0].length - 1 && cells[row][col+1]) {//right
					aliveNeighbors++;
				}
				if (row < cells.length - 1 && col > 0 && cells[row+1][col-1]) {//down left
					aliveNeighbors++;
				}
				if (row < cells.length - 1 && cells[row+1][col]) {//down
					aliveNeighbors++;
				}
				if (row < cells.length - 1 && col < cells[0].length - 1 && cells[row+1][col+1]) {//down right
					aliveNeighbors++;
				}
				
				if (cells[row][col]) {//Alive cell
					if (aliveNeighbors == 2 || aliveNeighbors == 3) {//2 or 3 neighbors, stays alive
						nextCells[row][col] = true;
					}
					else {//Not enough/Too many neighbors, dies
						nextCells[row][col] = false;
					}
				}
				else {//Dead cell
					if (aliveNeighbors == 3) {//3 Neighbors, brought back to life!
						nextCells[row][col] = true;
					}
					else {//Still dead
						nextCells[row][col] = false;
					}
				}
				
			}
		}
		//Set up the new cell layout
		cells = nextCells;
		panel.setCells(cells);
		frame.repaint();
	}
	
	//Extra mouse events (not used)
	@Override
	public void mouseClicked(MouseEvent event) {
	}
	@Override
	public void mousePressed(MouseEvent event) {
	}
	@Override
	public void mouseEntered(MouseEvent event) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
}
