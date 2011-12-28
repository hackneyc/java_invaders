import java.awt.Dimension;
import java.awt.Graphics;

public class Alien extends Sprite {
	public Sprite missile;
	public Sprite explode;
	private int points;
	private int dieCount;
	
	public Alien(int xPos, int yPos, int startFrame, String fileName,
			int maxFrames, int fps, int scaleX, int scaleY, Dimension d) {
		super(xPos, yPos, startFrame, fileName, maxFrames, fps, scaleX, scaleY, d);
		missile = new Sprite(0, 0, 0, "missile", 2, 0, 4, 24, d);
		explode = new Sprite(0, 0, 0, "alien_explode_", 1, 0, 16, 16, d);
		reset();
	}	

	public void reset() {
		super.reset();
		missile.setVisible(false);
		explode.setVisible(false);
	}
	
	public void setDieing()
	{
		setVisible(false);
		dieCount = 0;
		explode.setX((getX() + (getWidth() / 2)) - (explode.getWidth()/2));
		explode.setY((getY() + (getHeight() / 2)) - (explode.getHeight()/2));
		explode.setVisible(true);
	}
	
	public void draw(Graphics g)
	{
		super.draw(g);
		missile.draw(g);
		if (explode.isVisible())
		{
			if (dieCount++ > 20)
			{
				explode.setVisible(false);
			}
			else
			{
				explode.draw(g);
			}
		}
	}
	
	public boolean shoot()
	{
		boolean shooting = false;
		if (isVisible())
		{
			// New missile
			shooting = true;
			missile.setVisible(true);
			missile.setX((getX() + (getWidth() / 2)) - (missile.getWidth()/2));
			missile.setY(getY() + getHeight());
		}
		return(shooting);
	}
	
	public boolean updateMissile()
	{
		if (missile.isVisible())
		{
			if ((missile.getY() + 8) < dim.height)
			{
				missile.setY(missile.getY()+8);
			}
			else
			{
				missile.setVisible(false);
			}
		}
		
		return(missile.isVisible());
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
