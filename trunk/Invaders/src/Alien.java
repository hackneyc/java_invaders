import java.awt.Dimension;
import java.awt.Graphics;

public class Alien extends Sprite {
	public Sprite missile;
	public Sprite explode;
	private int points;
	private int dieCount;
	private Dimension dim;
	
	/**
	 * Constructor for a new alien.
	 * @param xPos
	 * @param yPos
	 * @param startFrame
	 * @param fileName
	 * @param maxFrames
	 * @param fps
	 * @param scaleX
	 * @param scaleY
	 * @param d
	 */
	public Alien(int xPos, int yPos, int startFrame, String fileName,
			int maxFrames, int fps, int scaleX, int scaleY, Dimension d) {
		super(xPos, yPos, startFrame, fileName, maxFrames, fps, scaleX, scaleY);
		missile = new Sprite(0, 0, 0, "missile", 2, 0, 4, 24);
		explode = new Sprite(0, 0, 0, "alien_explode_", 1, 0, 16, 16);
		dim = d;
		reset();
	}

	/**
	 * Reset the alien and hide its missile and explosion sprite.
	 */
	public void reset() {
		super.reset();
		missile.setVisible(false);
		explode.setVisible(false);
	}

	/**
	 * Hide the alien, reset the explosion timer and set the x/y
	 * coordinate of the explosion.
	 */
	public void setDieing() {
		setVisible(false);
		dieCount = 0;
		explode.setX((getX() + (getWidth() / 2)) - (explode.getWidth() / 2));
		explode.setY((getY() + (getHeight() / 2)) - (explode.getHeight() / 2));
		explode.setVisible(true);
	}

	/**
	 * Display the alien and its missile. If the alien is exploding
	 * keep track of how long the explosion is visible.
	 * @param g Graphics context.
	 */
	public void draw(Graphics g) {
		super.draw(g);
		missile.draw(g);
		if (explode.isVisible()) {
			if (dieCount++ > 20) {
				explode.setVisible(false);
			} else {
				explode.draw(g);
			}
		}
	}

	/**
	 * Initiate the firing of this aliens missile.
	 * @return true if the alien started to shoot a missile else false.
	 */
	public boolean shoot() {
		if (isVisible() && !missile.isVisible()) {
			// New missile
			missile.setVisible(true);
			missile.setX((getX() + (getWidth() / 2)) - (missile.getWidth() / 2));
			missile.setY(getY() + getHeight());
			return(true);
		}
		return (false);
	}

	/**
	 * Update the missile position for this alien.
	 * @return true if the alien missile is active else false.
	 */
	public boolean updateMissile() {
		if (missile.isVisible()) {
			if ((missile.getY() + 8) < dim.height) {
				missile.setY(missile.getY() + 8);
			} else {
				missile.setVisible(false);
			}
		}

		return (missile.isVisible());
	}

	/**
	 * Obtain the number of points for hitting this alien.
	 * @return points.
	 */
	int getPoints() {
		return (points);
	}

	/**
	 * Set the number of points for this alien.
	 * @param points Number of points for this alien.
	 */
	void setPoints(int points) {
		this.points = points;
	}
}
