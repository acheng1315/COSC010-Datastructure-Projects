import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, updated Fall 2016
 * @author Andrew Cheng
 */
public class Polyline implements Shape {
	// TODO: YOUR CODE HERE
	private Segment curr;
	private Color color;
	private ArrayList<Segment> segments = new ArrayList<>();


	/**
	 * Polyline constructor with just the color parameter
	 * @param color color param
	 */
	public Polyline(Color color) {
		this.color = color;
	}
	/**
	 * Polyline constructor with x and y coords and the color parameters.
	 * @param color color param
	 * @param x cursorX
	 * @param y cursorY
	 */
	public Polyline(int x, int y, Color color) {
		curr = new Segment(x, y, color);
		this.color = color;
	}

	public void update(int x, int y) {
		curr.setEnd(x,y);
		addSeg(curr);
		curr = new Segment(x, y, color);
	}

	/**
	 * Adds segments to the arraylist of segments to form the line.
	 */
	public void addSeg(Segment s) {
		segments.add(0, s);
	}

	/**
	 * moves segments
	 * @param dx x coord to move to
	 * @param dy y coord to move to
	 */
	@Override
	public void moveBy(int dx, int dy) {
		for(Segment s : segments) {
			s.moveBy(dx, dy);
		}
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
	 * checks if contains x or y coord for any segments
	 * @param x x coord
	 * @param y y coord
	 * @return return true or false
	 */
	@Override
	public boolean contains(int x, int y) {
		for(Segment s : segments) {

			if (s.contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * draws
	 * @param g graphics
	 */
	@Override
	public void draw(Graphics g) {
		for(Segment s : segments) {
			s.setColor(color);
			s.draw(g);
		}
	}

	/**
	 * Formatted as Name x1 y1 x2 y2 x1 y1 x2 y2 ... x1 y1 x2 y2
	 */
	@Override
	public String toString() {
		String string = "freehand";

		for(Segment s : segments) {
			string += " " + s;
		}

		return string;
	}
}
