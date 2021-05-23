import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/*
 * Panel of Game of Life simulation.
 * This class draws the grid and cells for the panel
 */

public class GameOfLifePanel extends JPanel {

	boolean[][] cells;
	double width;
	double height;
	
	public GameOfLifePanel(boolean[][] in) {
		cells = in;
	}
	
	public void setCells(boolean[][] in) {
		cells = in;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		width = (double)this.getWidth() / cells[0].length;
		height = (double)this.getHeight() / cells.length;
		
		//Draw the cells
		g.setColor(Color.BLUE);
		for (int row = 0; row < cells.length; row++) {
			for (int column = 0; column < cells[0].length; column++) {
				if (cells[row][column]) {
					g.fillRect((int)Math.round(column * width), (int)Math.round(row * height), (int)width+1, (int)height+1);
				}
			}
		}
		
		//Draw the grid
		g.setColor(Color.BLACK);
		for (int x = 0; x < cells[0].length + 1; x++) {
			g.drawLine((int)Math.round(x * width), 0, (int)Math.round(x * width), this.getHeight());
		}
		for (int y = 0; y < cells.length + 1; y++) {
			g.drawLine(0, (int)Math.round(y * height), this.getWidth(), (int)Math.round(y * height));
		}
	}
}
