import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Base extends Sprite implements KeyListener {
	final static int BASE_LEFT = 0x01;
	final static int BASE_RIGHT = 0x02;
	final static int BASE_FIRE = 0x10;

	private int baseSpeed;
	private int baseDir;
	public int hits;
	private SoundClip baseShoot;
	public Sprite missile;
	private SoundClip explode;
	private boolean fireReleased;

	public Base(int xPos, int yPos, int startFrame, String fileName,
			int maxFrames, int fps, int scaleX, int scaleY, Dimension d) {
		super(xPos, yPos, startFrame, fileName, maxFrames, fps, scaleX, scaleY, d);
		missile = new Sprite(0, 0, 0, "missile", 2, 0, 4, 32, d);
		baseShoot = new SoundClip("shoot.wav");
		hits = 0;
		baseSpeed = 4;
		explode = new SoundClip("explosion.wav");
		reset();
	}
	
	public void setDirection(int dir) {
		baseDir |= dir;
	}

	public void clearDirection(int dir) {
		baseDir &= ~dir;
	}
	
	public void reset() {
		super.reset();
		setX(((dim.width / 2) - (getWidth() / 2)));
		setY(dim.height - getHeight() - 16);
		baseDir = 0;
		missile.visible = false;
		fireReleased = true;
	}
	
	public void updatePosition() {
		//
		// Handle the movement of the base based on the
		// which keys are pressed. Check to make sure
		// it doesn't move off the visible window.
		//
		if (((baseDir & BASE_LEFT) != 0) && ((getX() - baseSpeed) > 0)) {
			setX(getX() - baseSpeed);
		} else if (((baseDir & BASE_RIGHT) != 0) && ((getX() + getWidth() + baseSpeed) < dim.width)) {
			setX(getX() + baseSpeed);
		}

		if (missile.visible)
		{
			if (missile.getY() - 8 >= 0)
				missile.setY(missile.getY()-8);
			else
			{
				missile.visible = false;
				clearDirection(Base.BASE_FIRE);
			}
		}
		else
		if (((baseDir & BASE_FIRE) != 0) && !missile.visible)
		{
			// New missile
			missile.visible = true;
			missile.setX((getX() + (getWidth() / 2)) - (missile.getWidth()/2));
			missile.setY(getY() - missile.getHeight());
			baseShoot.play();
		}
	}

	public void draw(Graphics g) {
		super.draw(g);
		missile.draw(g);
	}
	
	public void explode()
	{
		explode.play();		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Check for right or left key press
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			setDirection(Base.BASE_RIGHT);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			setDirection(Base.BASE_LEFT);
		} else if ((e.getKeyCode() == KeyEvent.VK_SPACE) && fireReleased) {
			setDirection(Base.BASE_FIRE);
			fireReleased = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// Check for right or left key release
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			clearDirection(Base.BASE_RIGHT);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			clearDirection(Base.BASE_LEFT);
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			clearDirection(Base.BASE_FIRE);
			fireReleased = true;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing
	}
}
