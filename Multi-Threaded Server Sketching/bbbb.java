import java.awt.*;
import java.util.ArrayList;

public class bbbb {
    public void test() {
        ArrayList<Shape> shapes = new ArrayList<>();
        shapes.add(new Ellipse(0, 0, new Color((int) (16777216 * Math.random()))));
        shapes.add(new Rectangle(0, 0, new Color((int) (16777216 * Math.random()))));

        ((Ellipse) shapes.get(0)).setCorners(0, 0, 10, 5);

    }
}
