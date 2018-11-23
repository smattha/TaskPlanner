package lms.robopartner.mfg_planning_tool;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JFrame;

public class SolutionPainter extends Component implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = -4040407711760257149L;
    int width, height;
    private Vector<Rectangle> taskRectangles;
    private Vector<Rectangle> reachabilityRectangles;

    public SolutionPainter(Dimension aDimension, Vector<Rectangle> taskRectangles,Vector<Rectangle> reachabilityRectangles) {
	this.width = aDimension.width;
	this.height = aDimension.height;
	this.taskRectangles = taskRectangles;
	this.reachabilityRectangles = reachabilityRectangles;

    }

    public Dimension getPreferredSize() {
	return new Dimension(width, height);
    }

    public void paint(Graphics graphics) {
	graphics.setColor(Color.WHITE);
	graphics.fillRect(0, 0, this.width, this.height);

	
//	//PAINT SHAPES
//	for (Rectangle aRectangle : this.reachabilityRectangles) {
//
//	    graphics.setColor(Color.GRAY);
//	    graphics.fillRect(aRectangle.x, aRectangle.y, aRectangle.width,
//		    aRectangle.height);
//	}
	

	
	for (Rectangle aRectangle : this.taskRectangles) {

	    graphics.setColor(Color.GREEN);
	    graphics.fillRect(aRectangle.x, aRectangle.y, aRectangle.width,
		    aRectangle.height);
	}
	
	//DRAW LINES
	for (Rectangle aRectangle : this.reachabilityRectangles) {

	    graphics.setColor(Color.BLUE);
	    graphics.drawRect(aRectangle.x, aRectangle.y, aRectangle.width,
		    aRectangle.height);

	}
	
	for (Rectangle aRectangle : this.taskRectangles) {

	    graphics.setColor(Color.BLACK);
	    graphics.drawRect(aRectangle.x, aRectangle.y, aRectangle.width,
		    aRectangle.height);

	}
	

    }

    public void actionPerformed(ActionEvent e) {

	repaint();

    };

    /**
     * @param s
     * @author Spyros Koukas
     */
    public static void main(String stringInput[]) {
	JFrame frame = new JFrame("Solution");
	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});

	Dimension mapDimension = new Dimension(50, 50);
	Vector<Rectangle> theRectangles = new Vector<Rectangle>();
	theRectangles.add(new Rectangle(1, 2, 3, 4));
	Vector<Rectangle> theReachabilityRectangles = new Vector<Rectangle>();
	theRectangles.add(new Rectangle(1, 2, 5, 6));
	SolutionPainter si = new SolutionPainter(mapDimension, theRectangles,theReachabilityRectangles);
	frame.add("Center", si);

	frame.pack();
	frame.setVisible(true);
    }

    public static void drawSolution(Dimension mapDimension,
	    Vector<Rectangle> theTaskRectangles, Vector<Rectangle> theReachabilityRectangles) {
	JFrame frame = new JFrame("Solution");
	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});

	SolutionPainter si = new SolutionPainter(mapDimension, theTaskRectangles,theReachabilityRectangles);
	frame.add("Center", si);

	frame.pack();
	frame.setVisible(true);
    }

}
