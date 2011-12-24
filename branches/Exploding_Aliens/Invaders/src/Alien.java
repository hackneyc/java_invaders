import java.awt.Dimension;
import java.awt.Graphics;

public class Alien extends Sprite {
	
	public Sprite missile;
	private int points;
	
	public Alien(int xPos, int yPos, int startFrame, String fileName,
			int maxFrames, int fps, int scaleX, int scaleY, Dimension d) {
		super(xPos, yPos, startFrame, fileName, maxFrames, fps, scaleX, scaleY, d);
		missile = new Sprite(0, 0, 0, "missile", 2, 0, 4, 24, d);
		reset();
	}	

	public void reset() {
		super.reset();
		missile.visible = false;
	}
	
	public void draw(Graphics g)
	{
		super.draw(g);
		missile.draw(g);
	}
	
	public boolean shoot()
	{
		boolean shooting = false;
		if (isAlive())
		{
			// New missile
			shooting = true;
			missile.visible = true;
			missile.setX((getX() + (getWidth() / 2)) - (missile.getWidth()/2));
			missile.setY(getY() + getHeight());
		}
		return(shooting);
	}
	
	public boolean updateMissile()
	{
		boolean shooting = false;
		if (missile.visible)
		{
			if ((missile.getY() + 8) < dim.height)
			{
				missile.setY(missile.getY()+8);
				shooting = true;
			}
			else
			{
				missile.visible = false;
			}
		}
		
		return(shooting);
	}
	
	int getPoints()
	{
		return(points);
	}

	void setPoints(int points)
	{
		this.points = points;
	}
}

