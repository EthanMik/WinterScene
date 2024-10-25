package scene;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

public class Scene extends JPanel implements Runnable {
	private final List<Shape> shapes;
	private final Shape background;
	private final int xError = 50;
	private final int yError = 50;

	public Scene(int w, int h, SceneType scene, ParticleType[] particle, int[] particleNums) {
		if (particle.length != particleNums.length) {
			throw new IllegalArgumentException("Must give particle types and amounts");
		}
		
		setVisible(true);	
		shapes = new ArrayList<>();
		Random rand = new Random();

		for (int i = 0; i < particle.length; i++) {
			for (int j = 0; j < particleNums[i]; j++) {
				int x = rand.nextInt(0 - xError, w + xError);
				int y = rand.nextInt(0 - yError, h + yError);
				shapes.add(particle[i].createParticle(x, y));
			}
		}

		background = new Background("scenes\\" + scene + ".png", 0, 0, w, h);

		new Thread(this).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D window = (Graphics2D) g;

		super.paintComponent(window);

		background.draw(window);

		for (Shape sh : shapes) {
			if (sh.getYPos() >= getHeight() + yError) {
				sh.setYPos(yError * -1);
			} else if (sh.getYPos() < yError * -1) {
				sh.setYPos(getHeight() + yError);
			}

			if (sh.getXPos() >= getWidth() + xError) {
				sh.setXPos(xError * -1);
			} else if (sh.getXPos() < xError * -1) {
				sh.setXPos(getWidth() + xError);
			}

			sh.moveAndDraw(window);
		}
	}

	@Override
	public void run() {
		try {
			while(true) {
				Thread.currentThread().sleep(35);
				repaint();
			}
		} catch(Exception e) { 
		}
  	}
}