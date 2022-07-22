import java.util.ArrayList;
import java.util.List;

/**
 * @author Sangha (Noel) Jang and Andrew Cheng
 * Date: 04/25/22
 * Purpose:
 * A point quadtree: stores an element at a 2D position,
 * with children at the subdivided quadrants
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, explicit rectangle
 * @author CBK, Fall 2016, generic with Point2D interface
 *
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters

	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 * @return null
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 * @return boolean checking whether it has a child
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * Inserts the point into the tree
	 * @param p2 generic point
	 */
	public void insert(E p2) {
		// TODO: YOUR CODE HERE
		// checks quadrant 1
		if (p2.getX() >= point.getX() && p2.getX() <= getX2() && p2.getY() <= point.getY() && p2.getY() >= getY1()) {
			// if it has a child, insert point into child
			if (hasChild(1)) {
				c1.insert(p2);
			// otherwise, if quadrant does not have child, make new PointQuadTree setting c1 at the new quadtree.
			} else if (!hasChild(1)){
				c1 = new PointQuadtree<E>(p2, (int) point.getX(), getY1(), getX2(), (int) point.getY());
			}

		}

		// checks quadrant 2
		else if (p2.getX() >= getX1() && p2.getX() < point.getX() && p2.getY() <= point.getY() && p2.getY() >= getY1()) {
			if (hasChild(2)) {
				c2.insert(p2);
			}
		 	else if (!hasChild(2)) {
				c2 = new PointQuadtree<E>(p2, getX1(), getY1(), (int) point.getX(), (int) point.getY());
			}

		}

		// checks quadrant 3
		else if (p2.getX() >= getX1() && p2.getX() <= point.getX() && p2.getY() > point.getY() && p2.getY() <= getY2()) {
			if (hasChild(3)) {
				c3.insert(p2);
			}
			else if (!hasChild(3)){
				c3 = new PointQuadtree<E>(p2, getX1(), (int) point.getY(), (int) point.getX(), getY2());
			}

		}
		// checks quadrant 4
		else if (p2.getX() > point.getX() && p2.getX() <= getX2() && p2.getY() > point.getY() && p2.getY() <= getY2()) {
			if (hasChild(4)) {
				c4.insert(p2);
			}
			else if (!hasChild(4)){
				c4 = new PointQuadtree<E>(p2, (int)point.getX(),(int)point.getY(), getX2(), getY2());
			}

		}

	}




	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 * @return the size of the tree
	 */
	public int size() {
		// TODO: YOUR CODE HERE

		int num = 1;

		if (hasChild(1)) {
			num += c1.size();
		}
		if (hasChild(2)) {
			num += c2.size();
		}
		if (hasChild(3)) {
			num += c3.size();
		}
		if (hasChild(4)) {
			num += c4.size();
		}
		return num;

	}

	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 * @return list of all points in the tree
	 */
	public List<E> allPoints() {
		// TODO: YOUR CODE HERE
		ArrayList<E> points = new ArrayList<>();
		addPoints(points, point);
		return points;

	}

	/**
	 * helper method for allPoints method
	 * @param points arraylist of points that keeps track of traversals
	 * @param point generic point child
	 */
	public void addPoints(ArrayList<E> points, E point) {
		if (point != null) {
			points.add(point);
			}
		if (hasChild(1)) {
			c1.addPoints(points, c1.point);
		}
		if (hasChild(2)) {
			c2.addPoints(points, c2.point);
		}
		if (hasChild(3)) {
			c3.addPoints(points, c3.point);
		}
		if (hasChild(4)) {
			c4.addPoints(points, c4.point);
		}
	}

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		ArrayList<E> hits = new ArrayList<E>();
		findInCircleHelper(hits, cx, cy, cr);
		// TODO: YOUR CODE HERE
		return hits;
	}

	/**
	 * helper method for findInCircle
	 * @param hit arraylist of points that were considered hit
	 * @param cx circle x
	 * @param cy circle y
	 * @param cr circle radius
	 */
	public void findInCircleHelper(ArrayList<E> hit, double cx, double cy, double cr) {
		if (Geometry.circleIntersectsRectangle(cx, cy, cr, getX1(), getY1(), getX2(), getY2())) {

			if (Geometry.pointInCircle(point.getX(), point.getY(), cx, cy, cr)) {
				hit.add(point);
			}

			if (hasChild(1)) {
				c1.findInCircleHelper(hit, cx, cy, cr);
			}
			if (hasChild(2)) {
				c2.findInCircleHelper(hit, cx, cy, cr);
			}
			if (hasChild(3)) {
				c3.findInCircleHelper(hit, cx, cy, cr);
			}
			if (hasChild(4)) {
				c4.findInCircleHelper(hit, cx, cy, cr);
			}
		}
	}
	// TODO: YOUR CODE HERE for any helper methods
}
