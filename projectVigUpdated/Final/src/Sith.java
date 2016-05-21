import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Timer;


public class Sith implements ActionListener {

	private final int ACTION_TIMEOUT = 400;

	// Load the sounds from the disk. 


	private Image sprites;
	private Rectangle[] spriteRects;

	// The bottom left corner of Sith.
	// We are drawing by the bottom left because the sprites have different heights, and we want different actions to change the y coordinate 
	// of his head rather than his feet.
	private int x, y;
	private int width, height;

	private double vX, vY;

	private int action;

	private boolean isFacingRight = true;
	private boolean isOnGround = true;
	private boolean isBlocking = false;
	
	static int health = 100;
	static int damagePerStrike = 10;
	static double jumpStrength = 45;
	private double friction;
	private double gravity;

	private JComponent surface; // for repainting when we make changes to his image

	private Timer actionTimer;

	/**
	 * Creates a Sith object
	 * @param x the x coordinate of Sith
	 * @param y the y coordinate of Sith
	 * @param surface represents the surface that the Sith is drawn to
	 */
	public Sith(int x, int y, int width, int height, JComponent surface) {

		if(isFacingRight) {
			sprites = new ImageIcon("Vader.png").getImage();

			spriteRects = new Rectangle[4]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(364, 0, 50, 68);
			/*strike*/ spriteRects[1] = new Rectangle(347, 320, 95, 75);
			/*block*/ spriteRects[2] = new Rectangle(268, 710, 50, 72);
			/*jump*/ spriteRects[3] = new Rectangle(287, 1248, 67, 100);
		}
		else {
			sprites = new ImageIcon("Vader.png").getImage();

			spriteRects = new Rectangle[4]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(20, 0, 50, 68);
			/*strike*/ spriteRects[1] = new Rectangle(0, 320, 95, 75);
			/*block*/ spriteRects[2] = new Rectangle(120, 710, 50, 72);
			/*jump*/ spriteRects[3] = new Rectangle(85, 1248, 67, 100);
		}

		this.x = x;
		this.y = y;
		this.width =  width;   // Default width and height is the width and height of the first image loaded from the sprite sheet
		this.height =  height;

		vX = 0;
		vY = 0;

		width *= 1;  // We scale the size of Sith up x4 for visibility
		height *= 1;
		
		gravity = 5;
		friction = 1;

		action = 0;

		this.surface = surface;
		actionTimer = new Timer(ACTION_TIMEOUT,this);
		actionTimer.setRepeats(false);
	}
	
	/**
	 * Makes decision for Sith based on position
	 * @param obstacles the obstacles on the screen
	 */
	public void act(ArrayList<Shape> obstacles) {
		double xCoord = getX();
		double yCoord = getY();
		double width = getWidth();
		double height = getHeight();

		// ***********Y AXIS***********

		vY += gravity; // GRAVITY
		double yCoord2 = yCoord + vY;

		Rectangle2D.Double strechY = new Rectangle2D.Double(xCoord,Math.min(yCoord,yCoord2),width,height+Math.abs(vY));

		isOnGround = false;

		if (vY > 0) {
			Shape standingSurface = null;
			for (Shape s : obstacles) {
				if (s.intersects(strechY)) {
					isOnGround = true;
					standingSurface = s;
					vY = 0;
				}
			}
			if (standingSurface != null) {
				Rectangle r = standingSurface.getBounds();
				yCoord2 = r.getY()-height;
			}
		} else if (vY < 0) {
			Shape headSurface = null;
			for (Shape s : obstacles) {
				if (s.intersects(strechY)) {
					headSurface = s;
					vY = 0;
				}
			}
			if (headSurface != null) {
				Rectangle r = headSurface.getBounds();
				yCoord2 = r.getY()+r.getHeight();
			}
		}

		if (Math.abs(vY) < .5)
			vY = 0;

		// ***********X AXIS***********


		vX *= friction;

		double xCoord2 = xCoord + vX;

		Rectangle2D.Double strechX = new Rectangle2D.Double(Math.min(xCoord,xCoord2),yCoord2,width+Math.abs(vX),height);

		if (vX > 0) {
			Shape rightSurface = null;
			for (Shape s : obstacles) {
				if (s.intersects(strechX)) {
					rightSurface = s;
					vX = 0;
				}
			}
			if (rightSurface != null) {
				Rectangle r = rightSurface.getBounds();
				xCoord2 = r.getX()-width;
			}
		} else if (vX < 0) {
			Shape leftSurface = null;
			for (Shape s : obstacles) {
				if (s.intersects(strechX)) {
					leftSurface = s;
					vX = 0;
				}
			}
			if (leftSurface != null) {
				Rectangle r = leftSurface.getBounds();
				xCoord2 = r.getX()+r.getWidth();
			}
		}


		if (Math.abs(vX) < .5)
			vX = 0;

		x = (int)xCoord2;
		y = (int)yCoord2;
	}

	/**
	 * Changes the direction that the Sith is facing
	 * @post Sith faces the opposite direction that it was facing
	 */
	public void changeDirection() {
		if(isFacingRight) {
			sprites = new ImageIcon("Vader.png").getImage();

			spriteRects = new Rectangle[4]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(20, 0, 50, 68);
			/*strike*/ spriteRects[1] = new Rectangle(0, 320, 95, 75);
			/*block*/ spriteRects[2] = new Rectangle(120, 710, 50, 72);
			/*jump*/ spriteRects[3] = new Rectangle(85, 1248, 67, 100);
			isFacingRight = false;
		}
		else {
			sprites = new ImageIcon("Vader.png").getImage();

			spriteRects = new Rectangle[4]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(364, 0, 50, 68);
			/*strike*/ spriteRects[1] = new Rectangle(347, 320, 95, 75);
			/*block*/ spriteRects[2] = new Rectangle(268, 710, 50, 72);
			/*jump*/ spriteRects[3] = new Rectangle(287, 1248, 67, 100);
			isFacingRight = true;
		}
	}

	/**
	 * Sith standing, doing nothing.
	 */
	public void stand() {
		action = 0;
	}

	/**
	 * Sith slashes his lightsaber.
	 */
	public void slash() {
		isBlocking = false;
		action = 1;
		actionTimer.restart();
	}

	/**
	 * Sith blocks
	 */
	public void block() {
		isBlocking = true;
		action = 2;
		actionTimer.restart();
	}

	/**
	 * Causes the Sith to move horizontally
	 * @param dir the direction that the Sith should walk in (negative for left, positive for right)
	 */
	public void walk(int dir) {
		if((!isFacingRight && dir > 0) || (isFacingRight && dir < 0)) {
			changeDirection();
		}
		vX = 15*dir;
	}

	/**
	 * Causes the Sith to stop moving
	 */
	public void stop() {
		isBlocking = false;
		action = 0;
		vX = 0;
	}

	/**
	 * Causes the Sith to jump
	 */
	public void jump() {
		if(isOnGround) {
			action = 3;
			isOnGround = false;
			vY = -1*jumpStrength;
			actionTimer.restart();
		}
	}

	/**
	 * 
	 * @return the x coordinate of the Sith
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @return the y coordinate of the Sith
	 */
	public int getY() {
		return y;
	}

	/**
	 * 
	 * @return the weight of the Sith
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * @return the height of the Sith
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 
	 * @return whether or not the Sith is facing right
	 */
	public boolean isFacingRight() {
		return isFacingRight;
	}
	
	/**
	 * 
	 * @return whether or not the Sith is blocking
	 */
	public boolean isBlocking() {
		return isBlocking;
	}

	/**
	 * Draw Sith using the correct sprite.
	 */
	public void draw(Graphics2D g2, ImageObserver io) {
		AffineTransform at = g2.getTransform();
		double xScale = (double)width / spriteRects[0].width;
		double yScale = (double)height / spriteRects[0].height;
		g2.drawImage(sprites, x,(int)(y-yScale*spriteRects[action].height+height),(int)(x+xScale*spriteRects[action].width),y+height,spriteRects[action].x,spriteRects[action].y,spriteRects[action].x+spriteRects[action].width,spriteRects[action].y+spriteRects[action].height,io);
	}

	/**
	 * Performs Sith actions
	 */
	public void actionPerformed(ActionEvent e) {
		if(action == 1) {
			stand();
			surface.repaint();
		}
		else if(action == 2) {
			action = 2;
		}
		else {
			action = 0;
		}
	}




}
