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
				shieldBlock[row][col].visible = true;
			}
		}
		
		shieldBlock[0][0].visible = false;
		shieldBlock[0][4].visible = false;
		shieldBlock[4][1].visible = false;
		shieldBlock[4][2].visible = false;
		shieldBlock[4][3].visible = false;
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

	public boolean collision(Alien alien)
	{
		int col;
		int row;
		boolean collided = false;

		if (alien.visible)
		{
			for(row=0; row<5; row++)
			{
				for(col=0; col<5; col++)
				{
					if(shieldBlock[row][col].collision(alien.getX(), alien.getY(), alien.getWidth(), alien.getHeight()))
					{
						shieldBlock[row][col].visible = false;
						collided = true;
					}
				}
			}
		}
		return(collided);
	}

	public boolean collision(Base base)
	{
		Sprite missile = base.missile;
		int col;
		int row;
		boolean collided = false;

		if (missile.visible)
		{
			for(row=0; row<5; row++)
			{
				for(col=0; col<5; col++)
				{
					if(shieldBlock[row][col].collision(missile.getX(), missile.getY(), missile.getWidth(), missile.getHeight()))
					{
						if(++shieldBlock[row][col].frame >= 4)
						{
							shieldBlock[row][col].visible = false;
						}
						collided = true;
					}
				}
			}
		}
		
		if (collided)
		{
			missile.visible = false;
			base.clearDirection(Base.BASE_FIRE);
		}
		
		return(collided);
	}
	
	public boolean collision(int xPos, int yPos, int width, int height)
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
					if(++shieldBlock[row][col].frame >= 4)
					{
						shieldBlock[row][col].visible = false;
					}
					collided = true;
				}
			}
		}
		return(collided);
	}
	
}