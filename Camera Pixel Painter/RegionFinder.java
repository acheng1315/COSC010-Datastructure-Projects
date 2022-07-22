import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 * Andrew Cheng
 */
public class RegionFinder {
    private static final int maxColorDiff = 20;                // how similar a pixel color must be to the target color, to belong to a region
    private static final int minRegion = 50;                // how many points in a region to be worth considering

    private BufferedImage image;                            // the image in which to find regions
    private BufferedImage recoloredImage;                   // the image with identified regions recolored
    private BufferedImage visited;
    private ArrayList<ArrayList<Point>> regions;            // a region is a list of points
    // so the identified regions are in a list of lists of points

    public RegionFinder() {
        this.image = null;
    }

    public RegionFinder(BufferedImage image) {
        this.image = image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getRecoloredImage() {
        return recoloredImage;
    }

    /**
     * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
     */
    public void findRegions(Color targetColor) {
        // defines visited image and regions, the list of regions.
        visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        regions = new ArrayList<ArrayList<Point>>();
        // loops through all the pixels of the image
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // checks if color matches and whether pixel has been visited
                if (visited.getRGB(x, y) == 0 && colorMatch(targetColor, new Color(image.getRGB(x, y)))) {

                    ArrayList<Point> region = new ArrayList<Point>();
                    ArrayList<Point> toVisit = new ArrayList<Point>();
                    Point pixel = new Point(x, y);
                    // adds pixel to the list of pixels to be visited
                    toVisit.add(pixel);
//                    System.out.println(pixel);
                    // while the list of pixels to visit isnt empty
                    while (!toVisit.isEmpty()) {
                        // pop
                        Point checkPixel = toVisit.remove(toVisit.size() - 1);
                        if (visited.getRGB(checkPixel.x, checkPixel.y) == 0) {
//                            System.out.println(toVisit.get(toVisit.size() - 1));

                            visited.setRGB(checkPixel.x, checkPixel.y, 1);
                            region.add(checkPixel);
                            // checks all the neighboring pixels while maintaining bounds
                            for (int ny = Math.max(0, checkPixel.y - 1);
                                 ny <= Math.min(image.getHeight() - 1, checkPixel.y + 1);
                                 ny++) {
                                for (int nx = Math.max(0, checkPixel.x - 1);
                                     nx <= Math.min(image.getWidth() - 1, checkPixel.x + 1);
                                     nx++) {
                                    // sets new color of neighbor color
                                    Color neighborColor = new Color(image.getRGB(nx, ny));
                                    // checks if color matches and whether pixel has been visited
                                    if (visited.getRGB(nx, ny) == 0 && colorMatch(targetColor, neighborColor)) {
                                        Point newPoint = new Point(nx, ny);
                                        // add the new point of the neighbor toVisit.
                                        toVisit.add(newPoint);
                                    }
                        }

                            }

                        }

                    }
//                    System.out.println(region.size());
                    // checks if the size of the region is large enough to qualify as a region
                    if (region.size() >= minRegion) {
                        regions.add(region);
//                        System.out.println("region added");
                    }
                }

            }

            }
        }


    /**
     * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
     */
    private static boolean colorMatch(Color c1, Color c2) {
        int red = Math.abs((c1.getRed() - c2.getRed()));
        int green = Math.abs(c1.getGreen() - c2.getGreen());
        int blue = Math.abs(c1.getBlue() - c2.getBlue());
        // checks if r g and b are less than or equal to the max color difference
        return red <= maxColorDiff && blue <= maxColorDiff && green <= maxColorDiff;

    }

    /**
     * Returns the largest region detected (if any region has been detected)
     */
    public ArrayList<Point> largestRegion() {
        ArrayList<Point> maxRegion = null;
        // as long as the list of regions isnt empty
        if (!regions.isEmpty()) {
            maxRegion = regions.get(0);

            // loops through each region in regions
            for (ArrayList<Point> region : regions) {
                // checks if region size is greater than max region size
                if (region.size() > maxRegion.size()) {
                    maxRegion = region;
                }
            }
        }
        return maxRegion;


    }


    /**
     * Sets recoloredImage to be a copy of image,
     * but with each region a uniform random color,
     * so we can see where they are
     */
    public void recolorImage() {
        // First copy the original
        if(regions.isEmpty()) {
            System.out.println("is empty");
        }
        recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
        // Now recolor the regions in it
        for (ArrayList<Point> region: regions) {
            int red = (int) (Math.random() * 255);
            int green = (int) (Math.random() * 255);
            int blue = (int) (Math.random() * 255);
            Color randColor = new Color(red, green, blue);
//            System.out.println("test1");
            // loops through each pixel in the region and recolors to a random color.
            for (Point pixel: region) {
//                System.out.println("test");
                recoloredImage.setRGB(pixel.x, pixel.y, randColor.getRGB());

            }

        }
        // for each region in regions
        // loop through each pixel in region
        // for each pixel set random r g b using image.setRGB

    }
}