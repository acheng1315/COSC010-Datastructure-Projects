import java.awt.Color;
import java.awt.Graphics;

/**
 * A rectangle-shaped Shape
 * Defined by an upper-left corner (x1,y1) and a lower-right corner (x2,y2)
 * with x1<=x2 and y1<=y2
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, updated Fall 2016
 * @author Andrew Cheng
 */
public class Rectangle implements Shape {
	// TODO: YOUR CODE HERE
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private Color color;

	/**
	 * rectangle constructor with one corner and the color
	 * @param x1 corner x
	 * @param y1 corner y
	 * @param color color parameter
	 */
	public Rectangle(int x1, int y1, Color color) {
		this.x1 = x1;
		this.x2 = x1;
		this.y1 = y1;
		this.y2 = y1;
		this.color = color;
	}

	/**
	 * rectangle constructor with 2 corners and the color parameter
	 * @param x1 corner x1
	 * @param y1 corner y1
	 * @param x2 corner x2
	 * @param y2 corner y2
	 * @param color color parameter
	 */
	public Rectangle(int x1, int y1, int x2, int y2, Color color) {
		setCorners(x1, y1, x2, y2);
		this.color = color;
	}

	/**
	 * sets the corner for the rectangle
	 * @param x1 corner x1
	 * @param y1 corner y1
	 * @param x2 corner x2
	 * @param y2 corner y2
	 */
	public void setCorners(int x1, int y1, int x2, int y2) {
		// sets the corners
		this.x1 = Math.min(x1, x2);
		this.y1 = Math.min(y1, y2);
		this.x2 = Math.max(x1, x2);
		this.y2 = Math.max(y1, y2);
	}

	/**
	 * moves rectangle
	 * @param dx goal x
	 * @param dy goal y
	 */
	@Override
	public void moveBy(int dx, int dy) {
		x1 += dx;
		y1 += dy;
		x2 += dx;
		y2 += dy;
	}

	/**
	 * gets color
	 * @return color
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/**
	 * sets color
	 * @param color The shape's color
	 */
	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * checks whether x and y are in rectangle
	 * @param x x being checked
	 * @param y y being checked
	 * @return boolean
	 */
	@Override
	public boolean contains(int x, int y) {
		return x1 <= x && x <= x2 && y1 <= y && y <= y2;
	}

	/**
	 * draws
	 * @param g graphics
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x1, y1, (x2 - x1), (y2 - y1));
	}

	/**
	 * converts to string
	 * @return string
	 */
	public String toString() {
		String string = "rectangle";
		string = string + " " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + color.getRGB();
		return string;
	}
}
