import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

import javax.swing.JFrame;


public class Game extends JFrame implements Runnable {
	private static final long serialVersionUID = -8921419424614180143L;
	private static final int WINDOW_WIDTH = 1024;
	private static final int WINDOW_HEIGHT = (WINDOW_WIDTH/16)*9;
	private Thread gameThread = null;
	private Player player = null;
	private Platform platform = null;
	private LinkedList<Platform> platformList = null;
	public static void main(String[] args) {
		new Game();
	}

	public Game()
	{
		//
		// Create the window
		//
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setVisible(true);
		createBufferStrategy(2);

		System.out.printf("Created window %dx%d\n", WINDOW_WIDTH, WINDOW_HEIGHT);

		player = new Player(200, 100);
		addKeyListener(player);
		
		platformList = new LinkedList<Platform>();
		
		for(int i=0; i<10; i++)
		{
			platform = new Platform(100+(i*64), 300 - (i * 4));
			platformList.add(platform);
		}
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run()
	{
		long lastTime = System.nanoTime();
		long now;
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int fps = 0;
		long timer = System.currentTimeMillis();
		BufferStrategy bs = getBufferStrategy();
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();

		g.setBackground(Color.WHITE);

		while(true)
		{
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1)
			{
				tick();
				delta--;
			}
			fps++;
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				System.out.printf("FPS: %s\n", fps);
				fps = 0;
			}

			g.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			render(g);			
			bs.show();
		}
	}

	private void render(Graphics2D g)
	{		
		player.render(g);
		
		for(int i=0; i<platformList.size(); i++)
		{
			Platform p = platformList.get(i);
			p.render(g);
		}
	}

	private void tick()
	{
		player.tick(platformList);
		for(int i=0; i<platformList.size(); i++)
		{
			Platform p = platformList.get(i);
			p.tick();
		}
	}	
}
