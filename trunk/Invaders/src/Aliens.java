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
	private LinkedList<Alien> liveAliens = null;
	private LinkedList<Alien> shootingAliens = null;
	private int alienSpeed;
	private int alienSpeedCount = 0;

	void explode()
	{
		invaderKilled.play();
	}
	
	void updatePosition()
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

	public int collision(Base base)
	{
		Sprite missile = base.missile;
		ListIterator<Alien> list;

		//
		// Iterate through all the aliens that are alive and
		// display them.
		//
		list = liveAliens.listIterator();
		while(list.hasNext())
		{
			Alien a = list.next();
			if (missile.isVisible())
			{
				// Check for alien collision with a missile
				if (a.collision(missile.getX(),
								missile.getY(),
								missile.getWidth(),
								missile.getHeight()))
				{
					// Kill the missile
					missile.setVisible(false);
					// Hide the invader
					a.setVisible(false);
					// Remove alien from the living list
					list.remove();
					// Play the kill sound
					explode();
					base.clearDirection(Base.BASE_FIRE);
					// Clear the fire flag
					// Speed up the aliens as more die
					if (alienSpeed > 0)
						alienSpeed--;
					// Increase the score
					return(a.getPoints());
				}
			}
		}
		return(0);
	}
	
	void draw(Graphics g)
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
		}
/*		
		for(row=0; row<NUM_ROWS; row++)
		{
			for(i=0; i<ALIENS_PER_ROW; i++)
			{
				aliens[row][i].draw(g);
				
				for(n=0; n<3; n++)
				{
					if(shield[n].collision(aliens[row][i].missile))
					{
						aliens[row][i].missile.visible = false;
					}
					if(shield[n].collision(aliens[row][i]))
					{
						// No damage to the aliens
					}
				}
				//
				// Check for collisions with the base
				//
				if(aliens[row][i].collision(base.getX(),
											base.getY(),
											base.getWidth(),
											base.getHeight()))
				{
					alienDirection = -1;
					aliens[row][i].visible= false;
					base.hits++;
				}
				else
				if(aliens[row][i].missile.collision(base.getX(),
						base.getY(),
						base.getWidth(),
						base.getHeight()))
				{
					baseExplode.play();
//					alienDirection = -1;
					aliens[row][i].missile.visible= false;
					base.hits++;
					base.reset();
				}
				else
			}
		}
*/
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
				liveAliens.add(alien[row][i]);
			}
		}
		alienDirection = ALIEN_DIRECTION_RIGHT;
		moveDown = false;
		alienMarchIndex = 0;
		alienSpeed = 60;
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
}