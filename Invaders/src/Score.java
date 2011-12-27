import java.awt.Graphics;

public class Score {
	private int score;
	private int x;
	private int y;
	
	public Score(int X, int Y)
	{
		x = X;
		y = Y;
		score = 0;
	}
	
	public void add(int points)
	{
		score += points;
	}
	
	public int get()
	{
		return(score);
	}
	
	void draw(Graphics g)
	{
		g.drawString(String.format("Score: % 4d", score), x, y);
	}
}