import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Sprite {
	
	private int numFrames;
	private Image image[];
	private Timer timer;
	private boolean visible;
	
	private int startFrame;
	private int startX;
	private int startY;
	
	public int frame;
	private int x;
	private int y;
	public Dimension dim;
	
	public int getWidth()
	{
		return(image[frame].getWidth(null));
	}

	public int getHeight()
	{
		return(image[frame].getHeight(null));
	}
	
	public int getX()
	{
		return(x);
	}

	public int getY()
	{
		return(y);
	}

	public void setX(int newX)
	{
		x = newX;
	}

	public void setY(int newY)
	{
		y = newY;
	}
	
	public void nextAnimationFrame()
	{
		//
		// Update the animation
		//
		frame = (frame + 1) % numFrames;
	}
	
	/**
	 * Timer task to update current animation frame.
	 */
	private class task extends TimerTask
	{
		@Override
		public void run()
		{
			nextAnimationFrame();
		}
	}

	public void reset()
	{
		frame = startFrame;
		x = startX;
		y = startY;
		visible = true;
	}
	
	public Sprite(String fileName, int numFrames)
	{
		image = new Image[numFrames];
		startFrame = 0;
		for(int i=0; i<numFrames; i++)
		{
			InputStream stream = ClassLoader.getSystemResourceAsStream(fileName+i+".gif");
			try {
				image[i] = ImageIO.read(stream);
			} catch (IOException e) {
				System.out.println("Error loading " + fileName+i+".gif");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param xPos Starting X position
	 * @param yPos Starting Y position
	 * @param startFrame Starting animation frame number
	 * @param fileName Image file name minus frame number and extension "fileName#.gif"
	 * @param maxFrames Total number of animation frames
	 * @param fps Animation speed in frames per second
	 * @param scaleX Image X scaling size
	 * @param scaleY Image Y scaling size
	 */
	public Sprite(int xPos, int yPos, int startFrame, String fileName, int maxFrames, int fps, int scaleX, int scaleY, Dimension d)
	{
		dim = d;
		timer = new Timer();
		image = new Image[maxFrames];

		this.startFrame = startFrame;
		this.startX = xPos;
		this.startY = yPos;
		numFrames 	= maxFrames;
		
		for(int i=0; i<numFrames; i++)
		{
			InputStream stream = ClassLoader.getSystemResourceAsStream(fileName+i+".gif");
			try {
				image[i] = ImageIO.read(stream);
			} catch (IOException e) {
				System.out.println("Error loading " + fileName+i+".gif");
				e.printStackTrace();
			}

			image[i] = image[i].getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
		}
				
		if(fps != 0)
			timer.schedule(new task(), 1000/fps, 1000/fps);
	}

	/**
	 * Draw the sprite on the specified graphics context.
	 * @param g Graphic context
	 * @param parent Parent class
	 */
	public void draw(Graphics g)
	{
		if(visible)
		{
			g.drawImage(image[frame], x, y, null);
		}
	}

	public boolean collision(Sprite sprite)
	{
		return(collision(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight()));
	}
	
	/**
	 * Check if this sprite has collided with the specified rectangle. 
	 * @param xPos X coordinate of the colliding sprite.
	 * @param yPos Y coordinate of the colliding sprite.
	 * @param w Width of the colliding sprite.
	 * @param h Height of the colliding sprite.
	 */
	public boolean collision(int xPos, int yPos, int w, int h)
	{
		if(visible)
		{
			Rectangle rect = new Rectangle(x, y, getWidth(), getHeight());
			Rectangle tmp = new Rectangle(xPos, yPos, w, h);
			return(rect.intersects(tmp));
		}
		
		return(false);
	}
	
	public boolean isVisible()
	{
		return(visible);
	}

	public void setVisible(boolean vis)
	{
		visible = vis;
	}
}
	
