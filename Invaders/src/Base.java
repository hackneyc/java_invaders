import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ListIterator;

public class Base extends Sprite implements KeyListener {
	final static int BASE_LEFT = 0x01;
	final static int BASE_RIGHT = 0x02;
	final static int BASE_FIRE = 0x10;

	private int baseSpeed;
	private int baseDir;
	private SoundClip baseShoot;
	public Sprite missile;
	private SoundClip explode;
	private boolean fireReleased;
	private Dimension dim;

	public Base(String fileName, int maxFrames, int scaleX, int scaleY, Dimension d) {
		super(fileName, maxFrames, scaleX, scaleY);
		missile = new Sprite("missile", 1, 4, 24);
		baseShoot = new SoundClip("shoot.wav");
		baseSpeed = 4;
		explode = new SoundClip("explosion.wav");
		dim = d;
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
		missile.setVisible(false);
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
		} else if (((baseDir & BASE_RIGHT) != 0)
				&& ((getX() + getWidth() + baseSpeed) < dim.width)) {
			setX(getX() + baseSpeed);
		}

		if (missile.isVisible()) {
			if (missile.getY() - 8 >= 0)
				missile.setY(missile.getY() - 8);
			else {
				missile.setVisible(false);
				clearDirection(Base.BASE_FIRE);
			}
		} else if (((baseDir & BASE_FIRE) != 0) && !missile.isVisible()) {
			// New missile
			missile.setVisible(true);
			missile.setX((getX() + (getWidth() / 2)) - (missile.getWidth() / 2));
			missile.setY(getY() - missile.getHeight());
			baseShoot.play();
		}
	}

	public void draw(Graphics g) {
		super.draw(g);
		missile.draw(g);
	}

	public void explode() {
		explode.play();
	}

	public boolean collision(Shield shield) {
		boolean collided = false;

		if (missile.isVisible()) {
			if ((collided = shield.collision(missile, 1))) {
				missile.setVisible(false);
				clearDirection(Base.BASE_FIRE);
			}
		}
		return (collided);
	}

	public int collision(Aliens aliens) {
		//
		// Iterate through all the aliens that are alive and
		// display them.
		//
		if (missile.isVisible()) {
			ListIterator<Alien> list = aliens.liveAliens.listIterator();
			while (list.hasNext()) {
				Alien a = list.next();
				// Check for alien collision with a missile
				if (a.collision(missile)) {
					// Kill the missile
					missile.setVisible(false);
					// Set the alien as dieing
					a.setDieing();
					// Play the kill sound
					aliens.explode();
					// Clear the base fire direction so it can fire again.
					clearDirection(Base.BASE_FIRE);
					// Speed up the aliens as more die
					aliens.increaseSpeed();
					// Increase the score
					return (a.getPoints());
				}
			}

			list = aliens.shootingAliens.listIterator();
			while (list.hasNext()) {
				Alien a = list.next();
				if (missile.collision(a.missile)) {
					// Kill the missiles
					a.missile.setVisible(false);
					missile.setVisible(false);
					// Clear the base fire direction so it can fire again.
					clearDirection(Base.BASE_FIRE);
					list.remove();
				}
			}
		}
		return (0);
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
