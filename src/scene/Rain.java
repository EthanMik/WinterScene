package scene;

import java.awt.Color;
import java.awt.Graphics2D;

public class Rain extends Shape {
   private final int xOffset;
   private final int yOffset;

   public Rain(int x, int y, Color color, int width, int height, int xSpeed, int ySpeed, int xOffset, int yOffset) {
		super(x, y, width, height, color, xSpeed, ySpeed);
      this.xOffset = xOffset;
      this.yOffset = yOffset;
   }

   public void draw(Graphics2D window) {
      window.setColor(getColor());
      
      int x = (int)getXPos();
      int y = (int)getYPos();       
      int w = (int)getWidth();
      int h = (int)getHeight();
      
      window.drawLine(x,y,x+w,y+h); 
      
   }

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

    	draw(window); 
   }
}