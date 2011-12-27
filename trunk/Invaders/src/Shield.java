import java.awt.Graphics;


public class Shield {
	public Sprite shieldBlock[][];
	
	public Shield()
	{
		int row;
		int col;
		
		shieldBlock = new Sprite[5][5];

		for(row=0; row<5; row++)
		{
			for(col=0; col<5; col++)
			{
				shieldBlock[row][col] = new Sprite("shield", 4);
				shieldBlock[row][col].setVisible(true);
			}
		}
		
		shieldBlock[0][0].setVisible(false);
		shieldBlock[0][4].setVisible(false);
		shieldBlock[4][1].setVisible(false);
		shieldBlock[4][2].setVisible(false);
		shieldBlock[4][3].setVisible(false);
	}

	public void setXY(int newX, int newY)
	{
		int col;
		int row;
		
		for(row=0; row<5; row++)
		{
			for(col=0; col<5; col++)
			{
				shieldBlock[row][col].setX(newX + (col * 8));
				shieldBlock[row][col].setY(newY + (row * 8));
			}
		}
	}

	public void draw(Graphics g)
	{
		int col;
		int row;
		
		for(row=0; row<5; row++)
		{
			for(col=0; col<5; col++)
			{
				shieldBlock[row][col].draw(g);
			}
		}
	}

	public boolean collision(Sprite s, int dammage)
	{
		return(collision(s.getX(), s.getY(), s.getWidth(), s.getHeight(), dammage));
	}
	
	public boolean collision(int xPos, int yPos, int width, int height, int dammage)
	{
		int col;
		int row;
		boolean collided = false;
		
		for(row=0; row<5; row++)
		{
			for(col=0; col<5; col++)
			{
				if(shieldBlock[row][col].collision(xPos, yPos, width, height))
				{
					shieldBlock[row][col].frame += dammage;
					if(shieldBlock[row][col].frame >= 4)
					{
						shieldBlock[row][col].setVisible(false);
					}
					collided = true;
				}
			}
		}
		return(collided);
	}	
}