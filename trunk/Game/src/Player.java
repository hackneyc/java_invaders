import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Player implements KeyListener {
	private Image rightImage[];
	private Image leftImage[];
	private int currentFrame = 0;
	private int x;
	private int y;
	private int[] annimation = {1, 1, 1, 2, 2, 2, 1, 1, 1, 0, 0, 0, 3}; 
	private int direction = 0;
	private int lastDirection = LEFT;
	private int jumpHeight = 0;
	private int xInc = 0;
	private final static int LEFT = 1;
	private final static int RIGHT = 2;
	private final static int JUMP = 4;
	private final static int FALL_SPEED = 4;
	private final static int WIDTH = 64;
	private final static int HEIGHT = 64;
	
	public Player(int x, int y)
	{
		BufferedImage spriteSheet = null;
		this.x = x;
		this.y = y;
		try {
			spriteSheet = ImageIO.read(new File("images/jumpman.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rightImage = new Image[4];
		
		rightImage[0] = spriteSheet.getSubimage(49, 465, 16, 16);		
		rightImage[0] = rightImage[0].getScaledInstance(WIDTH, -HEIGHT, Image.SCALE_SMOOTH);
		
		rightImage[1] = spriteSheet.getSubimage(72, 465, 16, 16);		
		rightImage[1] = rightImage[1].getScaledInstance(WIDTH, -HEIGHT, Image.SCALE_SMOOTH);
		
		rightImage[2] = spriteSheet.getSubimage(96, 465, 16, 16);		
		rightImage[2] = rightImage[2].getScaledInstance(WIDTH, -HEIGHT, Image.SCALE_SMOOTH);

		rightImage[3] = spriteSheet.getSubimage(88, 402, 16, 15);		
		rightImage[3] = rightImage[3].getScaledInstance(WIDTH, -HEIGHT, Image.SCALE_SMOOTH);

		leftImage = new Image[4];		
		leftImage[0] = spriteSheet.getSubimage(48, 445, 16, 16);		
		leftImage[0] = leftImage[0].getScaledInstance(WIDTH, -HEIGHT, Image.SCALE_SMOOTH);
		
		leftImage[1] = spriteSheet.getSubimage(74, 445, 16, 16);		
		leftImage[1] = leftImage[1].getScaledInstance(WIDTH, -HEIGHT, Image.SCALE_SMOOTH);
		
		leftImage[2] = spriteSheet.getSubimage(97, 445, 16, 16);		
		leftImage[2] = leftImage[2].getScaledInstance(WIDTH, -HEIGHT, Image.SCALE_SMOOTH);

		leftImage[3] = spriteSheet.getSubimage(88, 386, 16, 15);		
		leftImage[3] = leftImage[3].getScaledInstance(WIDTH, -HEIGHT, Image.SCALE_SMOOTH);
	}
	
	public void tick(LinkedList<Platform> pList)
	{
		if((jumping()) && (jumpHeight > 0))
		{
			jumpHeight-=7;
			y-=7;
		}
		else
		{	
			boolean onPlatform = false;
			// Create rectangle for player
			Rectangle rect = new Rectangle(x+8, y, WIDTH-16, rightImage[0].getHeight(null)+FALL_SPEED);
			for(int i = pList.size()-1; i>=0; i--)
			{
				Platform p = pList.get(i);
				// Check for collision with platform
				// if no collision move down.
				if(rect.intersects(p.getRect()) == true)
				{
					if(y+rightImage[0].getHeight(null) > p.getRect().y)
					// Force player on to the platform.
						y = p.getRect().y - rightImage[0].getHeight(null);
					onPlatform = true;
					clearDirection(JUMP);
				}
			}
			
			if(!onPlatform)
				y+=FALL_SPEED;
		}	
				
		// Update the animation frame
		if(jumping())
		{
			currentFrame = 12;
		}
		else
		{
			if((direction & (LEFT | RIGHT)) != 0)
				currentFrame = (currentFrame + 1) % 12;
			else
				currentFrame = 0;
		}

		x+=xInc;
		
/*
		if((direction & RIGHT)== RIGHT)
		{
			x+=7;
		}
		else
		if((direction & LEFT) == LEFT)
		{
			x-=7;
		}
		else
		{
			currentFrame = 0;
		}
		*/
	}
	
	public void render(Graphics g)
	{
		if((lastDirection & RIGHT) == RIGHT)
		{
			g.drawImage(rightImage[annimation[currentFrame]], x, y, null);
		}
		else
		if((lastDirection & LEFT) == LEFT)
		{
			g.drawImage(leftImage[annimation[currentFrame]], x, y, null);			
		}
	}

	public boolean jumping()
	{
		if((direction & JUMP) == JUMP)
		{
			return(true);
		}
		return(false);
	}
	
	public void setDirection(int dir)
	{
		// No direction update if jumping
		if(!jumping())
		{
			// If Jump pressed set the jump height
			if(dir == JUMP)
				jumpHeight = rightImage[0].getHeight(null);
			else
			{
				if(dir == LEFT)
				{
					xInc = -7;
				}
				else
				{
					xInc = 7;
				}
				lastDirection = dir;
			}
			direction |= dir;
		}
	}

	public void clearDirection(int dir)
	{
		direction &= ~dir;
		if((direction & (LEFT | RIGHT)) == 0)
			xInc = 0;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// Check for right or left key press
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			setDirection(RIGHT);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			setDirection(LEFT);
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			setDirection(JUMP);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Check for right or left key release
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			clearDirection(RIGHT);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			clearDirection(LEFT);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
