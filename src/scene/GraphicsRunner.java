package scene;

import javax.swing.JFrame;

public class GraphicsRunner extends JFrame
{
	private static final int WIDTH = 900;
	private static final int HEIGHT = 900;
	private static final SceneType BACKGROUND = SceneType.winter;
	private static final ParticleType[] PARTICLES = new ParticleType[]{ ParticleType.SNOWFLAKE3D, ParticleType.SNOWFLAKE}; 
	private static final int[] PARTICLES_NUM = new int[] {100, 40};

	public GraphicsRunner()
	{
		super("SCENE PROJECT");
		setSize(WIDTH, HEIGHT);

		getContentPane().add(new Scene(WIDTH, HEIGHT, BACKGROUND, PARTICLES, PARTICLES_NUM));

		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main( String args[] ) {
		GraphicsRunner run = new GraphicsRunner();
	}
}