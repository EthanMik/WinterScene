package scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class Snowflake extends Shape {
   private ArrayList<double[][]> segments;

   private final int xOffset;
   private final int yOffset;
   private Random rand = new Random();

    public Snowflake(double x, double y, Color color, double width, double height, double xSpeed, double ySpeed, int xOffset, int yOffset) {
		super(x, y, width, height, color, xSpeed, ySpeed);
      this.xOffset = xOffset;
      this.yOffset = yOffset;

	  segments = new ArrayList<>();
	  createShape();
    }

	protected ArrayList<double[][]> getSegments() {
		return segments;
	}

	protected void setSegments(ArrayList<double[][]> segments) {
		this.segments = segments;
	}

	@Override
	public void draw(Graphics2D window) {
     	window.setColor(getColor());

		for (double[][] segment : segments) {
			double x1 = segment[0][0];
			double y1 = segment[0][1];
			double x2 = segment[1][0];
			double y2 = segment[1][1];

			Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);

			window.draw(line);
		}
    }

   @Override
   public void moveAndDraw(Graphics2D window) {
      int x = (int)(xOffset * Math.random());
      int y = (int)(yOffset * Math.random());

      int rnd = (int)(Math.random() * 2);

    	if (rnd == 1) {
			x *= -1;
			y *= -1;
		}  

		setXPos(getXPos() + getXSpeed() + x);
    	setYPos(getYPos() + getYSpeed() + y);

		double[] centroid = centroid(segments);

		double rotationSpeed = rand.nextDouble(0.1, 5); 
		for (int i = 0; i < segments.size(); i++) {
			segments.set(i, rotate(rotationSpeed, segments.get(i), centroid));
		}

		translate(segments, getXPos(), getYPos(), centroid);

    	draw(window); 
    }

	public final double[] centroid(ArrayList<double[][]> segments) {
		double x = 0;
		double y = 0;

		for (int i = 0; i < segments.size(); i++) {
			x += segments.get(i)[0][0] + segments.get(i)[1][0];
			y += segments.get(i)[0][1] + segments.get(i)[1][1];
		}
		double points = segments.size() * 2.0;
		return new double[]{x / points, y / points};
	}

	public void translate(ArrayList<double[][]> segments, double targetX, double targetY, double[] centoid) {
		double translationX = targetX - centoid[0];
		double translationY = targetY - centoid[1];

		for (int i = 0; i < segments.size(); i++) {
			segments.get(i)[0][0] += translationX;
			segments.get(i)[1][0] += translationX;
			
			segments.get(i)[0][1] += translationY;
			segments.get(i)[1][1] += translationY;
		}
	}

	public final void createShape() {
		double x = getXPos();
		double y = getYPos();

		double branchLength = rand.nextInt((int)(getWidth()), (int)(getHeight()));
		
		segments.add(new double[][]{{x, y}, {x, y + branchLength}});

		double[][] initialSegment = getPrevLineSegment(segments);
		double sideBranchesNum = rand.nextInt(1, 4); 
		double offset = rand.nextDouble(0.1, 1);
		double angle = rand.nextInt(20, 70);
		double length = rand.nextDouble(0.8, 1);

		createMainBranch(branchLength, initialSegment, sideBranchesNum, offset, angle, length);
		rotateBranch(45, 8, new double[]{x, y});
	}

	public void createMainBranch(double branchLength, double[][] initialSegment, double sideBranchesNum, double offset, double angle, double length) {
		for (int i = 0; i < sideBranchesNum; i++) {
			double[] midpoint = midpoint(initialSegment[1], initialSegment[0], offset);
			double distance = distance(midpoint, initialSegment[1]);

			double horizontalDist = distance * Math.tan(angle * Math.PI / 180) * length;
			double veriticalDist = branchLength * length;

			segments.add(new double[][]{{midpoint[0], midpoint[1]}, {(getXPos() + horizontalDist), (getYPos() + veriticalDist)}});
			segments.add(reflect(getPrevLineSegment(segments), initialSegment));
			offset = rand.nextDouble(0.1, 1);
		}
	}

	public void rotateBranch(double degrees, double rotations, double[] rotationPoint) {
		if (rotationPoint.length != 2) {
			throw new IllegalArgumentException("Coordinates must come in pairs of 2");		
		}

		ArrayList<double[][]> segementQueue = new ArrayList<>(segments);
		final double angle = degrees;
		for (int i = 0; i < rotations; i++) {
			for (double[][] segment : segementQueue) {
				segments.add(rotate(degrees, segment, rotationPoint));
			}
			degrees += angle;
		}
	}

    public double[] midpoint(double[] coord1, double[] coord2) {
		if (coord1.length != 2 || coord2.length != 2) {
			throw new IllegalArgumentException("Coordinates must come in pairs of 2");		
		}
		// Midpoint formula
		return new double[]{((coord1[0] + coord2[0]) / 2.0), ((coord1[1] + coord2[1]) / 2.0)}; 
	}
    public double[] midpoint(double[] coord1, double[] coord2, double offset) {
		if (coord1.length != 2 || coord2.length != 2 || offset >= 1 || offset <= 0) {
			throw new IllegalArgumentException("Coordinates must come in pairs of 2, offset is between 0-1");		
		}
		// Midpoint offset formula of ((1 − t) * x1 ​+ t * x2​, (1 − t) * y1​ + t * y2​)
		return new double[]{((1 - offset) * coord1[0] + offset * coord2[0]), ((1 - offset) * coord1[1] + offset * coord2[1])}; 
	}
	public double[][] rotate(double degrees, double[][] segment, double[] rotationPoint) {
		if (segment.length != 2 || segment[0].length != 2 || rotationPoint.length != 2) {
			throw new IllegalArgumentException("Coordinates must come in pairs of 2");		
		}

		return new double[][] {rotate(degrees, segment[0], rotationPoint), rotate(degrees, segment[1], rotationPoint)};
	}

	public double[] rotate(double degrees, double[] coord, double[] rotationPoint) {
		if (coord.length != 2) {
			throw new IllegalArgumentException("coordinate must be of length 2.");		
		}

		double x = coord[0] - rotationPoint[0];
		double y = coord[1] - rotationPoint[1];

		double radians = Math.toRadians(degrees);

		double rotatedX = x * Math.cos(radians) - y * Math.sin(radians);
		double rotatedY = x * Math.sin(radians) + y * Math.cos(radians);

		return new double[] {
			(rotatedX + rotationPoint[0]), 
			(rotatedY + rotationPoint[1])
		};
	}


	public double[][] reflect(double[][] segment, double[][] line) {
		if (segment.length != 2 || segment[0].length != 2 || line.length != 2 || line[0].length != 2) {
			throw new IllegalArgumentException("Coordinates must come in pairs of 2");		
		}

		return new double[][] {reflect(segment[0], line), reflect(segment[1], line)};
	}
	public double[] reflect(double[] coord, double[][] line) {
		if (coord.length != 2 || line.length != 2 || line[0].length != 2) {
			throw new IllegalArgumentException("Coordinates must come in pairs of 2");		
		}

		double x0 = coord[0];
		double y0 = coord[1];
		double x1 = line[0][0];
		double y1 = line[0][1];
		double x2 = line[1][0];
		double y2 = line[1][1];

		// Check if reflection line is horizontal
		if (y1 == y2) {
			return new double[]{x0, 2 * y1 - y0};
		}
		// Check if reflection line is vertical
		if (x1 == x2) {
			return new double[]{2 * x1 - x0, y0};
		}
		// Create equation Ax + By + C = 0 with line coords
		double slope = ((y2 - y1) / (x2 - x1));
		double a = slope;
		double b = -1;
		double c = -1 * slope * x1 + y1; 

		// Mirror (x, y) across line segment
        double x = x0 - 2 * a * (a * x0 + b * y0 + c) / a * a + b * b;
        double y = y0 - 2 * b * (a * x0 + b * y0 + c) / a * a + b * b;

		return new double[]{x, y};
	}
	public double distance(double[][] segment) {
		if (segment[0].length != 2 || segment[1].length != 2 || segment.length != 2) {
			throw new IllegalArgumentException("Coordinates must come in pairs of 2");		
		}
		return distance(segment[0], segment[1]);
	}

	public double distance(double[] coord1, double[] coord2) {
		if (coord1.length != 2 || coord2.length != 2) {
			throw new IllegalArgumentException("Coordinates must come in pairs of 2");		
		}
		// Distance formula
		return (Math.sqrt(Math.pow(coord2[0] - coord1[0], 2) + Math.pow(coord2[1] - coord1[1], 2))); 
	}

	public double[][] getPrevLineSegment(ArrayList<double[][]> segments) {
		return segments.get(segments.size() - 1);
	}
}