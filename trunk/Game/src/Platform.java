import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Platform {
	private int x;
	private int y;
	private int w = 64;
	private int h = 16;
	
	Platform(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Rectangle getRect()
	{
		return(new Rectangle(x, y, w, h));
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.RED);
		g.fillRect(x, y, w, h);
	}
	
	public void tick()
	{
	}
}
