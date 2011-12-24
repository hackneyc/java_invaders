import javax.swing.*;
import java.awt.*;

import de.pxlab.pxl.*;
import java.awt.image.BufferStrategy;

public class Game extends JFrame implements Runnable {
	private static final long serialVersionUID = -2669437745998537325L;

	private final int WINDOW_WIDTH = 647;
	private final int WINDOW_HEIGHT = 480;

	private Thread updateThread;
	private Base base;
	private int score;
	private Insets insets;
	private Dimension dim;
	private Shield shield[];
	private Aliens aliens;
	
	private void createSprites()
	{
		int i;
		
		base = new Base(0, 0, 0, "base", 1, 0, 32, 22, dim);
		aliens = new Aliens( dim, insets);
		shield = new Shield[3];
		
		for(i=0; i<3; i++)
		{
			shield[i] = new Shield();
			shield[i].setXY(100+(i*200)+insets.left, 375);
		}
	}
	
	public Game() {
		//
		// Things that are reset per game
		//
		score = 0;
		
		//
		// Create the window
		//
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);

		setVisible(true);
		createBufferStrategy(2);

		//
		// Size and insets not valid until visible.
		//
		dim = getSize();
		insets = getInsets();

		createSprites();

		addKeyListener(base);
		
		//
		// Create a new thread to run the main game loop
		//
		updateThread = new Thread(this);
		updateThread.start();
	}

	public static void main(String[] args) {
		new Game();
		System.out.println("Hello World!");
	}

	@Override
	public void update(Graphics g) {
		super.update(g);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	@Override
	public void run() {
		int i;
		BufferStrategy bf = getBufferStrategy();
		Graphics g = bf.getDrawGraphics();
		Font font = new Font("Verdana", Font.BOLD, 24);
		g.setFont(font);
		g.setColor(Color.GREEN);
		
		while (Thread.currentThread() == updateThread && (aliens.numAlive() != 0)) {

			g.clearRect(insets.left, insets.top, dim.width - insets.left - insets.right, dim.height - insets.bottom - insets.top);

			base.updatePosition();
			aliens.updatePosition();
			aliens.updateMissiles();

			//
			// Collision checking
			// 
			score += aliens.collision(base);
			for(i=0; i<3; i++)
			{
				shield[i].collision(base);
			}

			for(i=0; i<3; i++)
			{
				shield[i].draw(g);
			}
			aliens.draw(g);
			base.draw(g);
			g.drawString(String.format("Score: % 4d", score), insets.left, insets.top + 20);

			// Wait for vertical retrace then update the screen
			VideoSync.waitForBeginOfVerticalBlank();
			// Make the buffer visible
			bf.show();
			// Force the buffer to the screen
			Toolkit.getDefaultToolkit().sync();
/*			
			try {
				// Give other things time to do stuff.
				Thread.sleep(10);
			} catch (InterruptedException e) {
				break;
			}
*/
		}
		g.dispose();
	}
}
