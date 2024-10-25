package scene;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Shape {
	private double xPos;
	private double yPos;

	private final double width;
	private final double height;

	private final Color color;

	private double xSpeed;
	private double ySpeed;


   protected Shape(double x, double y, double wid, double ht) {
		xPos = x;
		yPos = y;
		width = wid;
		height = ht;
		color = Color.WHITE;
		xSpeed = 0;
		ySpeed = 0;
   }
   
   protected Shape(double x, double y, double wid, double ht, Color col) {
		xPos = x;
		yPos = y;
		width = wid;
		height = ht;
		color = col;
		xSpeed = 0;
		ySpeed= 0;
   }   
   
   protected Shape(double x, double y, double wid, double ht, Color col, double xSpd, double ySpd) {
		xPos = x;
		yPos = y;
		width = wid;
		height = ht;
		color = col;
		xSpeed = xSpd;
		ySpeed= ySpd;
   }  
  
   public void setXPos(double xp) {
   	xPos = xp;
   }
   
   public void setYPos(double yp) {
   	yPos = yp;
   }
   
   public void setXSpeed(double xs) {
   	xSpeed = xs;
   }
   
   public void setYSpeed(double ys) {
   	ySpeed = ys;
   }
   
   public double getYSpeed() {
   	return ySpeed;
   }
   
   public double getXSpeed() {
   	return xSpeed;
   }
   
   public double getXPos() {
   	return xPos;
   }
   
   public double getYPos() {
   	return yPos;
   }
   
   public Color getColor() {
   	return color;
   }
   
   public double getWidth() {
   	return width;
   }
   
   public double getHeight() {
   	return height;
   }

   public abstract void draw(Graphics2D window);

   public abstract void moveAndDraw(Graphics2D window);

   @Override
   public String toString() {
   	return xPos + " " + yPos + " " + width + " " + height + " " + color + " " + xSpeed+ "  " + ySpeed;
   }
}