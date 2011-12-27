import java.awt.Graphics;

public class Lives {
	private int lives;
	private int x;
	private int y;
	
	public Lives(int X, int Y)
	{
		x = X;
		y = Y;
		lives = 3;
	}
	
	public int decrement()
	{
		if(lives > 0)
		{
			lives -= 1;
		}
		return(lives);
	}
	
	void draw(Graphics g)
	{
		g.drawString(String.format("Lives: %d", lives), x, y);
	}
}