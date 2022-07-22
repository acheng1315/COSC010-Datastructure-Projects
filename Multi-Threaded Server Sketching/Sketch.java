/**
 * sketch class that is used as a data type that stores a treemap of the ids of the various shapes on a sketch as well
 * as certain information about the shapes, such as the color or coordinates or etc.
 * @author Andrew Cheng
 */

import java.awt.*;
import java.util.TreeMap;

public class Sketch {
    TreeMap<Integer, Shape> shapes;

    /**
     * constructor for a sketch using a treemap
     */
    public Sketch() {
        shapes = new TreeMap<>();
    }

    /**
     * creates and defines a shape based on the data that is parsed from the line.
     * @param line line of data that is parsed
     * @return a shape
     */
    public static Shape defineShape(String line) {
        String[] command = line.split(" ");

        if (command[0].equals("ellipse")) {
            return new Ellipse(Integer.parseInt(command[1]), Integer.parseInt(command[2]), Integer.parseInt(command[3]), Integer.parseInt(command[4]), new Color(Integer.parseInt(command[5])));
        }

        if (command[0].equals("freehand")) {
            Polyline polyline = new Polyline(new Color(Integer.parseInt(command[command.length-1])));

            for(int i = 2; i < command.length - 1; i += 6) {
                polyline.addSeg(new Segment(Integer.parseInt(command[i]), Integer.parseInt(command[i + 1]), Integer.parseInt(command[i + 2]), Integer.parseInt(command[i + 3]), new Color(Integer.parseInt(command[command.length - 1]))));
            }

            return polyline;
        }

        if (command[0].equals("rectangle")) {
            return new Rectangle(Integer.parseInt(command[1]), Integer.parseInt(command[2]), Integer.parseInt(command[3]), Integer.parseInt(command[4]), new Color(Integer.parseInt(command[5])));
        }

        if (command[0].equals("segment")) {
            return new Segment(Integer.parseInt(command[1]), Integer.parseInt(command[2]), Integer.parseInt(command[3]), Integer.parseInt(command[4]), new Color(Integer.parseInt(command[5])));
        }

        return null;
    }

    /**
     * Adds a shape to the treemap using id
     * @param id id
     * @param line line that is parsed to find data
     */
    public void add(int id, String line) {
        addShape(id, defineShape(line));
    }

    /**
     * Adds a shape to treemap
     * @param id id
     * @param shape shape
     */
    public void addShape(int id, Shape shape) {
        shapes.put(id, shape);
    }
    /**
     * draws shapes on the screen
     * @param g
     */
    public void drawShapes(Graphics g) {
        for(Shape shape : shapes.values())
            shape.draw(g);
    }

    /**
     * combines two sketches
     * @param sketch other sketch
     */
    public void combineSketch(Sketch sketch) {
        for(int key : this.shapes.keySet()) {
            sketch.addShape(key, this.shapes.get(key));
        }
        this.shapes = sketch.shapes;
    }

    /**
     * runs through the key set to show all the shapes
     * @return
     */
    public String toString() {
        String string = "Shapes are:\n";
        System.out.print(shapes.size()+"\t");
        System.out.println(shapes);
        for(int key : shapes.keySet()) {
            Shape shape = shapes.get(key);

            if(shape != null) {
                string += key + "\t" + shape + "\n";
            }
        }
        string += "end";
        return string;
    }

}
