import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam-based drawing 
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Spring 2015 (based on a different webcam app from previous terms)
 * Andrew Cheng
 */
public class CamPaint extends Webcam {
    private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
    private RegionFinder finder;			// handles the finding
    private Color targetColor;          	// color of regions of interest (set by mouse press)
    private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
    private BufferedImage painting;			// the resulting masterpiece

    /**
     * Initializes the region finder and the drawing
     */
    public CamPaint() {
        finder = new RegionFinder();
        clearPainting();
    }

    /**
     * Resets the painting to a blank image
     */
    protected void clearPainting() {
        painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * DrawingGUI method, here drawing one of live webcam, recolored image, or painting,
     * depending on display variable ('w', 'r', or 'p')
     */
    @Override
    public void draw(Graphics g) {
        // webcam mode that allows you to select target color regions.
        if (displayMode == 'w') {
            g.drawImage(image, 0, 0, null);
        // shows the recolored version
        } else if (displayMode == 'r') {
            System.out.println("r pressed");
            g.drawImage(finder.getRecoloredImage(), 0, 0, null);
        // takes the largest region and uses it as a paintbrush
        } else if (displayMode == 'p') {
            System.out.println("p pressed");
            g.drawImage(painting, 0, 0, null);
        }

    }

    /**
     * Webcam method, here finding regions and updating the painting.
     */
    @Override
    public void processImage() {
        // TODO: YOUR CODE HERE
//        System.out.print("process image called");
        if (targetColor !=null) {
            finder.setImage(image);
            finder.findRegions(targetColor);
            finder.recolorImage();
//            System.out.print("recolored image");
            // creates largest region
            ArrayList<Point> largestRegion = finder.largestRegion();
            if (largestRegion != null) {
                // loops through points in largest region to set new color
                for(Point pixel: largestRegion) {
                    painting.setRGB(pixel.x, pixel.y, paintColor.getRGB());
                }
            }

        }

    }

    /**
     * Overrides the DrawingGUI method to set the track color.
     */
    @Override
    public void handleMousePress(int x, int y) {
        if (image != null) {
            // sets new target color to the current mouse click location.
            targetColor = new Color(image.getRGB(x, y));

//            System.out.println("target color:" + targetColor);
        }

    }

    /**
     * DrawingGUI method, here doing various drawing commands
     */
    @Override
    public void handleKeyPress(char k) {
        if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
            displayMode = k;
        }
        else if (k == 'c') { // clear
            clearPainting();
        }
        else if (k == 'o') { // save the recolored image
            saveImage(finder.getRecoloredImage(), "Pset1/recolored.png", "png");
        }
        else if (k == 's') { // save the painting
            saveImage(painting, "Pset1/painting.png", "png");
        }
        else {
            System.out.println("unexpected key "+k);
        }
    }
    // main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CamPaint();
            }
        });
    }
}