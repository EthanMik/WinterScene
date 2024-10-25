package scene;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Background extends Shape 
{
   private Image image;

   public Background(String imagePath, int x, int y, int w, int h) {
      super(x, y, w, h);
      
      try {
         image = ImageIO.read(new File(imagePath));
      } catch (IOException e) {
         System.out.println("Failed to load image: " + imagePath);
      }
      this.image = image.getScaledInstance((int)(getWidth()), (int)(getHeight()), Image.SCALE_SMOOTH);
   }

   public void draw(Graphics2D window)
   {
      window.drawImage(image, (int)(getXPos()), (int)(getYPos()), null); 
   }

   public void moveAndDraw(Graphics2D window)
   {
      draw(window);
   }
}