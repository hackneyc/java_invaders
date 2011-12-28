import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class Aliens {
	private final int NUM_ROWS = 5;
	private final int ALIENS_PER_ROW = 10;
	public final static int ALIEN_DIRECTION_RIGHT = 1;
	public final static int ALIEN_DIRECTION_LEFT = 0;
	private final int MAX_ALIEN_MARCH = 4;

	private SoundClip alienMarch[];
	private int alienMarchIndex;
	private Insets insets;
	private Random rand;
	private boolean moveDown;
	private Dimension screenDimension;
	private int alienDirection;
	private Alien alien[][];
	private SoundClip invaderKilled;
	public LinkedList<Alien> liveAliens = null;
	public LinkedList<Alien> shootingAliens = null;
	private int alienSpeed;
	private int alienSpeedCount = 0;

	public void increaseSpeed()
	{
		if (alienSpeed > 0)
			alienSpeed--;
	}
	
	public void explode()
	{
		invaderKilled.play();
	}
	
	public void updatePosition()
	{
		int row;
		int col;
		ListIterator<Alien> list;
		
		if(alienSpeedCount++ >= alienSpeed)
		{
			alienSpeedCount = 0;
			//
			// Iterate through all the aliens that are alive to
			// see if we need to move them down.
			//
			list = liveAliens.listIterator();
			while(list.hasNext())
			{
				Alien a = list.next();
	
				if (alienDirection == ALIEN_DIRECTION_RIGHT)
				{
					if((a.getX() + a.getWidth() + 16) > (screenDimension.width - insets.right))
					{
						moveDown = true;
						break;
					}
				}
				else
				if (alienDirection == ALIEN_DIRECTION_LEFT)
				{
					if((a.getX() - 16) < insets.left)
					{
						moveDown = true;
						break;
					}
				}
			}
	
			//
			// Update the alien positions
			//
			list = liveAliens.listIterator();
			while(list.hasNext())
			{
				Alien a = list.next();
				if (moveDown)
				{
					a.setY(a.getY() + 16);
				}
				else
				if (alienDirection == ALIEN_DIRECTION_RIGHT)
				{
					a.setX(a.getX() + 16);
				}
				else
				if (alienDirection == ALIEN_DIRECTION_LEFT)
				{
					a.setX(a.getX() - 16);
				}
				
				//
				// Update the animation frame
				//
				a.nextAnimationFrame();
			}
	
			//
			// Change direction after moving down
			//
			if(moveDown)
			{
				if (alienDirection == ALIEN_DIRECTION_RIGHT)
				{
					alienDirection = ALIEN_DIRECTION_LEFT;
				}
				else
				{
					alienDirection = ALIEN_DIRECTION_RIGHT;
				}
				
				moveDown = false;
			}
			
			//
			// An alien can only shoot if there are no other aliens below him...
			//
			col = Math.abs(rand.nextInt() % ALIENS_PER_ROW);
			for(row=(NUM_ROWS - 1); row >= 0; row--)
			{
				if (alien[row][col].shoot())
				{
					shootingAliens.add(alien[row][col]);
					break;
				}
			}
			
			//
			// Make the "sound"
			//
			alienMarch[alienMarchIndex].play();
			alienMarchIndex = (alienMarchIndex + 1) % MAX_ALIEN_MARCH;
		}
	}
	
	public void draw(Graphics g)
	{
		ListIterator<Alien> list;

		//
		// Iterate through all the aliens that are alive and
		// display them.
		//
		list = liveAliens.listIterator();
		while(list.hasNext())
		{
			Alien a = list.next();
			a.draw(g);
			//
			// Aliens may be diing to check to see if they have finished
			// dieing and if so remove them from the live list.
			//
			if((a.isVisible() == false) && (a.explode.isVisible() == false))
				list.remove();
		}
	}
	
	public void updateMissiles()
	{
		//
		// Iterate through all the aliens that are shooting and
		// update the position of their missiles.
		//
		ListIterator<Alien> list = shootingAliens.listIterator();
		while(list.hasNext())
		{
			Alien a = list.next();
			if (a.updateMissile() == false)
			{
				//
				// Remove the alien from the shooting list if it is no
				// longer shooting.
				//
				list.remove();
			}
		}
	}

	public void setAlienSpeed(int speed)
	{
		alienSpeed = speed;
	}

	public int getAlienSpeed()
	{
		return(alienSpeed);
	}
	
	public void reset()
	{
		liveAliens.clear();
		shootingAliens.clear();
		//
		// Add all aliens to the live aliens list.
		//
		for(int row=0; row<NUM_ROWS; row++)
		{
			for(int i=0; i<ALIENS_PER_ROW; i++)
			{
				alien[row][i].reset();
				liveAliens.add(alien[row][i]);
			}
		}

		alienDirection = ALIEN_DIRECTION_RIGHT;
		moveDown = false;
		alienMarchIndex = 0;
	}
	
	public Aliens(Dimension d, Insets insets)
	{
		screenDimension = d;
		liveAliens = new LinkedList<Alien>();
		shootingAliens = new LinkedList<Alien>();		
		alien = new Alien [NUM_ROWS][ALIENS_PER_ROW];
		this.insets = insets;
		rand = new Random();
		alienMarch = new SoundClip[4];
		
		for (int i=0; i<4; i++)
		{
			alienMarch[i] = new SoundClip("fastinvader"+(i+1)+".wav");			
		}

		invaderKilled = new SoundClip("invaderkilled.wav");

		//
		// Create the alien sprites
		//
		for(int row=0; row<NUM_ROWS; row++)
		{
			for(int i=0; i<ALIENS_PER_ROW; i++)
			{
				alien[row][i] = new Alien(insets.left + (i*48), 60 + (40 * row), 0, "alien0"+row+"_", 2, 0, 32, 32, d);
				alien[row][i].setPoints(10 * (5 - row));
			}
		}
		
		reset();
	}
	
	public int numAlive()
	{
		return(liveAliens.size());
	}

	public void collision(Shield shield) {
		//
		// Iterate through all the aliens that are shooting and
		// update the position of their missiles.
		//
		ListIterator<Alien> list = shootingAliens.listIterator();
		while(list.hasNext())
		{
			Alien a = list.next();
			if (shield.collision(a.missile, 1))
			{
				a.missile.setVisible(false);
			}
		}

		//
		// Iterate through all the aliens that are alive and
		// see if they collided with the shield.
		//
		list = liveAliens.listIterator();
		while(list.hasNext())
		{
			Alien a = list.next();
			shield.collision(a, 5);
		}
	}

	public boolean collision(Base base) {
		//
		// Iterate through all the aliens that are shooting and
		// see if the missiles hit the base.
		//
		ListIterator<Alien> list = shootingAliens.listIterator();
		while(list.hasNext())
		{
			Alien a = list.next();
			if (base.collision(a.missile))
			{
				a.missile.setVisible(false);
				base.explode();
				base.reset();
				return(true);
			}
		}

		//
		// Iterate through all the aliens that are alive and
		// see if they hit the base.
		//
		list = liveAliens.listIterator();
		while(list.hasNext())
		{
			Alien a = list.next();
			if (base.collision(a))
			{
				base.explode();
				base.reset();
				return(true);
			}
		}

		return(false);
	}
}