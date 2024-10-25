package scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class Snowflake3D extends Snowflake {
    private ArrayList<double[][]> segments;	
	private ArrayList<double[][]> segmentsCopy;

    private final int xOffset;
    private final int yOffset;
    private Random rand = new Random();

	private double Rx = rand.nextDouble(0.0, 2);
	private double Ry = rand.nextDouble(0.0, 2);
	private double Rz = rand.nextDouble(0.0, 2);

    public Snowflake3D(double x, double y, Color color, double width, double height, double xSpeed, double ySpeed, int xOffset, int yOffset) {
		super(x, y, color, width, height, xSpeed, ySpeed, 0, 0);
      this.xOffset = xOffset;
      this.yOffset = yOffset;

	  segments = new ArrayList<>();

	  segments = getSegments();
    }

	@Override
	public void draw(Graphics2D window) {
     	window.setColor(getColor());

		segmentsCopy = copySegments(segments);

		Rx += rand.nextDouble(0.01, 0.05);
		Ry += rand.nextDouble(0.01, 0.05);
		Rz += rand.nextDouble(0.01, 0.05);

		rotate(segmentsCopy, centroid(segments), Rx, Ry, Rz);

		for (double[][] segment : segmentsCopy) {
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

		translate(segments, getXPos(), getYPos(), centroid);

    	draw(window); 
    }

	public void rotate(ArrayList<double[][]> segments, double[] centroid, double xTheta, double yTheta, double zTheta) {
		double[][] coordinates = new double[3][segments.size() * 2];

		for (int i = 0, j = 0; i < coordinates[0].length; i+=2, j++) {
			coordinates[0][i] = segments.get(j)[0][0] - centroid[0];
			coordinates[0][i + 1] = segments.get(j)[1][0] - centroid[0];
			
			coordinates[1][i] = segments.get(j)[0][1] - centroid[1];
			coordinates[1][i + 1] = segments.get(j)[1][1] - centroid[1];
		}

		double[][] xRotation = {
			{1, 0, 0},
			{0, Math.cos(xTheta), -Math.sin(xTheta)},
			{0, Math.sin(xTheta),  Math.cos(xTheta)}
		};
		double[][] yRotation = {
			{ Math.cos(yTheta), 0, Math.sin(yTheta)},
			{0, 1, 0},
			{-Math.sin(yTheta), 0, Math.cos(yTheta)}
		};
		double[][] zRotation = {
			{Math.cos(zTheta), -Math.sin(zTheta), 0},
			{Math.sin(zTheta),  Math.cos(zTheta), 0},
			{0, 0, 1}
		};

		double[][] xMatrix = multiplyMatrices(xRotation, coordinates);
		double[][] yMatrix = multiplyMatrices(yRotation, xMatrix);
		double[][] zMatrix = multiplyMatrices(zRotation, yMatrix);

		for (int i = 0, j = 0; i < coordinates[0].length; i+=2, j++) {
			segments.get(j)[0][0] = zMatrix[0][i] + centroid[0];
			segments.get(j)[1][0] = zMatrix[0][i + 1] + centroid[0];

			segments.get(j)[0][1] = zMatrix[1][i] + centroid[1];
			segments.get(j)[1][1] = zMatrix[1][i + 1] + centroid[1];
		}
	}

	public static double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;
        int rows2 = matrix2.length;
        int cols2 = matrix2[0].length;

        if (cols1 != rows2) {
            throw new IllegalArgumentException("Columns of matrix1 must be equal to rows of matrix2.");
        }

        double[][] result = new double[rows1][cols2];

        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < cols1; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return result;
    }

	private ArrayList<double[][]> copySegments(ArrayList<double[][]> original) {
		ArrayList<double[][]> copy = new ArrayList<>();
		for (double[][] segment : original) {
			double[][] segmentCopy = new double[segment.length][];
			for (int i = 0; i < segment.length; i++) {
				segmentCopy[i] = segment[i].clone(); // Copy each sub-array to avoid reference sharing
			}
			copy.add(segmentCopy);
		}
		return copy;
	}


}