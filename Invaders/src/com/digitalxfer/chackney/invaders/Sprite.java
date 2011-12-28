package com.digitalxfer.chackney.invaders;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Sprite {

	private int numFrames;
	private Image image[];
	private Timer timer;
	private boolean visible;

	private int startFrame;
	private int startX;
	private int startY;

	public int frame;
	private int x;
	private int y;

	/**
	 * Obtain the width of the sprite.
	 * @return Width of the sprite in pixels.
	 */
	public int getWidth() {
		return (image[frame].getWidth(null));
	}

	/**
	 * Obtain the height of the sprite.
	 * @return Height of the sprite in pixels.
	 */
	public int getHeight() {
		return (image[frame].getHeight(null));
	}

	/**
	 * Obtain the x coordinate of the sprite.
	 * @return x coordinate of the sprite.
	 */
	public int getX() {
		return (x);
	}

	/**
	 * Obtain the y coordinate of the sprite.
	 * @return y coordinate of the sprite.
	 */
	public int getY() {
		return (y);
	}

	/**
	 * Set the x coordinate of the sprite.
	 * @param newX The new x coordinate of the sprite.
	 */
	public void setX(int newX) {
		x = newX;
	}

	/**
	 * Set the y coordinate of the sprite.
	 * @param newY The new y coordinate of the sprite.
	 */
	public void setY(int newY) {
		y = newY;
	}

	/**
	 * Advance to the sprite's next frame of animation, loop to the
	 * first frame after the last frame. 
	 */
	public void nextAnimationFrame() {
		//
		// Update the animation
		//
		frame = (frame + 1) % numFrames;
	}

	/**
	 * Timer task to update the sprite's animation frame.
	 */
	private class task extends TimerTask {
		@Override
		public void run() {
			nextAnimationFrame();
		}
	}

	/**
	 * This function performs the following actions:
	 * - Resets the sprite's x and y coordinates to their initial values.
	 * - Resets the active animation frame to its initial value.
	 * - Set the sprite to be visible.
	 */
	public void reset() {
		frame = startFrame;
		x = startX;
		y = startY;
		visible = true;
	}

	/**
	 * Create a sprite using the specified base file name. The sprite
	 * is made up of the specified number of animation frames.
	 * @param fileName 
	 *            Image file name minus frame number and extension
	 *            "fileName#.gif".
	 * @param numFrames
	 *            Total number of animation frames.
	 */
	public Sprite(String fileName, int numFrames) {
		image = new Image[numFrames];
		startFrame = 0;
		for (int i = 0; i < numFrames; i++) {
			InputStream stream = ClassLoader.getSystemResourceAsStream(fileName
					+ i + ".gif");
			try {
				image[i] = ImageIO.read(stream);
			} catch (IOException e) {
				System.out.println("Error loading " + fileName + i + ".gif");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create a sprite using the specified base file name. The sprite
	 * is made up of the specified number of animation frames.
	 * @param fileName 
	 *            Image file name minus frame number and extension
	 *            "fileName#.gif".
	 * @param numFrames
	 *            Total number of animation frames.
	 * @param scaleX
	 *            Image X scaling size.
	 * @param scaleY
	 *            Image Y scaling size.
	 */
	public Sprite(String fileName, int numFrames, int scaleX, int scaleY) {
		image = new Image[numFrames];
		startFrame = 0;
		for (int i = 0; i < numFrames; i++) {
			InputStream stream = ClassLoader.getSystemResourceAsStream(fileName
					+ i + ".gif");
			try {
				image[i] = ImageIO.read(stream);
			} catch (IOException e) {
				System.out.println("Error loading " + fileName + i + ".gif");
				e.printStackTrace();
			}
			image[i] = image[i].getScaledInstance(scaleX, scaleY,
					Image.SCALE_SMOOTH);
		}
	}

	/**
	 * 
	 * @param xPos
	 *            Sprite's initial x coordinate.
	 * @param yPos
	 *            Sprite's initial y coordinate.
	 * @param startFrame
	 *            Sprite's initial animation frame number.
	 * @param fileName
	 *            Image file name minus frame number and extension
	 *            "fileName#.gif"
	 * @param maxFrames
	 *            Total number of animation frames for the sprite.
	 * @param fps
	 *            Animation speed in frames per second.
	 * @param scaleX
	 *            Image X scaling size.
	 * @param scaleY
	 *            Image Y scaling size.
	 */
	public Sprite(int xPos, int yPos, int startFrame, String fileName,
			int maxFrames, int fps, int scaleX, int scaleY) {
		timer = new Timer();
		image = new Image[maxFrames];

		this.startFrame = startFrame;
		this.startX = xPos;
		this.startY = yPos;
		numFrames = maxFrames;

		for (int i = 0; i < numFrames; i++) {
			InputStream stream = ClassLoader.getSystemResourceAsStream(fileName
					+ i + ".gif");
			try {
				image[i] = ImageIO.read(stream);
			} catch (IOException e) {
				System.out.println("Error loading " + fileName + i + ".gif");
				e.printStackTrace();
			}

			image[i] = image[i].getScaledInstance(scaleX, scaleY,
					Image.SCALE_SMOOTH);
		}

		if (fps != 0)
			timer.schedule(new task(), 1000 / fps, 1000 / fps);
	}

	/**
	 * Draw the sprite on the specified graphics context.
	 * @param g Graphic context.
	 */
	public void draw(Graphics g) {
		if (visible) {
			g.drawImage(image[frame], x, y, null);
		}
	}

	/**
	 * Check if the specified sprite has collided with this sprite.
	 * @param sprite Sprite to check for a collision with.
	 * @return true if a collision is detected else false.
	 */
	public boolean collision(Sprite sprite) {
		return (collision(sprite.getX(), sprite.getY(), sprite.getWidth(),
				sprite.getHeight()));
	}

	/**
	 * Check if this sprite has collided with the specified rectangle.
	 * 
	 * @param xPos
	 *            X coordinate of the colliding sprite.
	 * @param yPos
	 *            Y coordinate of the colliding sprite.
	 * @param w
	 *            Width of the colliding sprite.
	 * @param h
	 *            Height of the colliding sprite.
	 */
	public boolean collision(int xPos, int yPos, int w, int h) {
		if (visible) {
			Rectangle rect = new Rectangle(x, y, getWidth(), getHeight());
			Rectangle tmp = new Rectangle(xPos, yPos, w, h);
			return (rect.intersects(tmp));
		}

		return (false);
	}

	/**
	 * Determine if the sprite is visible.
	 * @return true if the sprite is visible else false.
	 */
	public boolean isVisible() {
		return (visible);
	}

	/**
	 * Set the visibility of the sprite.
	 * @param vis true if the sprite is visible, else false.
	 */
	public void setVisible(boolean vis) {
		visible = vis;
	}
}
