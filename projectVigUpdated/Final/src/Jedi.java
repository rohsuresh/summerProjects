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


public class Jedi implements ActionListener {

	private final int ACTION_TIMEOUT = 400;

	// Load the sounds from the disk. 

	private Image sprites;
	private Rectangle[] spriteRects;

	// The bottom left corner of Jedi.
	// We are drawing by the bottom left because the sprites have different heights, and we want different actions to change the y coordinate 
	// of his head rather than his feet.
	private int x, y;
	private int width, height;
	
	static int health = 100;
	static int damagePerStrike = 10;
	static double jumpStrength = 45;
	private double friction;
	private double gravity;

	private double vX, vY;

	private int action;

	private boolean isFacingRight = false;
	private boolean isOnGround = true;
	private boolean isBlocking = false;

	private JComponent surface; // for repainting when we make changes to his image

	private Timer actionTimer;

	/**
	 * Creates a Jedi object
	 * @param x the x coordinate of Jedi
	 * @param y the y coordinate of Jedi
	 * @param surface represents the surface that the Jedi is drawn to
	 */
	public Jedi(int x, int y, int width, int height, JComponent surface) {

		if(isFacingRight) {
			sprites = new ImageIcon("AnakinSkywalker.png").getImage();

			spriteRects = new Rectangle[6]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(9, 90, 45, 70);
			/*strike*/ spriteRects[1] = new Rectangle(53, 460, 66, 53);
			/*block*/ spriteRects[2] = new Rectangle(55, 378, 40, 65);
			/*runRight1*/ spriteRects[3] = new Rectangle(8, 160, 40, 62);
			/*runRight2*/ spriteRects[4] = new Rectangle(86, 160, 38, 64);
			/*jump*/ spriteRects[5] = new Rectangle(154, 224, 30, 70);
		}
		else {
			sprites = new ImageIcon("AnakinSkywalkerLeft.png").getImage();

			spriteRects = new Rectangle[6]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(232, 90, 45, 70);
			/*strike*/ spriteRects[1] = new Rectangle(166, 460, 60, 53);
			/*block*/ spriteRects[2] = new Rectangle(150, 378, 40, 65);
			/*runLeft1*/ spriteRects[3] = new Rectangle(82, 160, 40, 62);
			/*runLeft2*/ spriteRects[4] = new Rectangle(162, 160, 38, 63);
			/*jump*/ spriteRects[5] = new Rectangle(96, 224, 30, 70);
		}

		this.x = x;
		this.y = y;
		this.width =  width;   // Default width and height is the width and height of the first image loaded from the sprite sheet
		this.height =  height;

		vX = 0;
		vY = 0;
		
		gravity = 5;
		friction = 1;

		action = 0;

		this.surface = surface;
		actionTimer = new Timer(ACTION_TIMEOUT,this);
		actionTimer.setRepeats(false);
	}
	
	/**
	 * Makes decision for Jedi based on position
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
	 * Changes the direction that the Jedi is facing
	 * @post Jedi faces the opposite direction that it was facing
	 */
	public void changeDirection() {
		if(isFacingRight) {
			sprites = new ImageIcon("AnakinSkywalkerLeft.png").getImage();

			spriteRects = new Rectangle[6]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(232, 90, 45, 70);
			/*strike*/ spriteRects[1] = new Rectangle(166, 460, 60, 53);
			/*block*/ spriteRects[2] = new Rectangle(150, 378, 40, 65);
			/*runLeft1*/ spriteRects[3] = new Rectangle(82, 160, 40, 62);
			/*runLeft2*/ spriteRects[4] = new Rectangle(162, 160, 38, 64);
			/*jump*/ spriteRects[5] = new Rectangle(96, 224, 30, 70);
			isFacingRight = false;
		}
		else {
			sprites = new ImageIcon("AnakinSkywalker.png").getImage();

			spriteRects = new Rectangle[6]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(9, 90, 45, 70);
			/*strike*/ spriteRects[1] = new Rectangle(53, 460, 66, 53);
			/*block*/ spriteRects[2] = new Rectangle(55, 378, 40, 65);
			/*runRight1*/ spriteRects[3] = new Rectangle(8, 160, 40, 62);
			/*runRight2*/ spriteRects[4] = new Rectangle(86, 160, 38, 64);
			/*jump*/ spriteRects[5] = new Rectangle(154, 224, 30, 70);
			isFacingRight = true;
		}
	}

	/**
	 * Jedi standing, doing nothing.
	 */
	public void stand() {
		isBlocking = false;
		action = 0;
	}

	/**
	 * Jedi slashes his lightsaber.
	 */
	public void slash() {
		isBlocking = false;
		action = 1;
		actionTimer.restart();
	}

	/**
	 * Jedi blocks
	 */
	public void block() {
		isBlocking = true;
		action = 2;
		actionTimer.restart();
	}

	/**
	 * Causes the Jedi to move horizontally
	 * @param dir the direction that the Jedi should walk in (negative for left, positive for right)
	 */
	public void walk(int dir) {
		if(action == 0 || action == 4) {
			if((!isFacingRight && dir > 0) || (isFacingRight && dir < 0)) {
				changeDirection();
			}
			action = 3;
		}
		else if(action == 3) {
			if((!isFacingRight && dir > 0) || (isFacingRight && dir < 0)) {
				changeDirection();
			}
			action = 4;
		}
		vX = 15*dir;
	}

	/**
	 * Causes the Jedi to stop moving
	 */
	public void stop() {
		isBlocking = false;
		action = 0;
		vX = 0;
	}

	/**
	 * Causes the Jedi to jump
	 */
	public void jump() {
		if(isOnGround) {
			action = 5;
			isOnGround = false;
			vY = -1*jumpStrength;
			actionTimer.restart();
		}
	}

	/**
	 * 
	 * @return the x coordinate of the Jedi
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @return the y coordinate of the Jedi
	 */
	public int getY() {
		return y;
	}

	/**
	 * 
	 * @return the width of the Jedi
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * @return the height of the Jedi
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 
	 * @return whether or not the Jedi is facing right
	 */
	public boolean isFacingRight() {
		return isFacingRight;
	}
	
	/**
	 * 
	 * @return whether or not the Jedi is blocking
	 */
	public boolean isBlocking() {
		return isBlocking;
	}

	/**
	 * Draw Jedi using the correct sprite.
	 */
	public void draw(Graphics2D g2, ImageObserver io) {
		AffineTransform at = g2.getTransform();
		double xScale = (double)width / spriteRects[0].width;
		double yScale = (double)height / spriteRects[0].height;
		g2.drawImage(sprites, x,(int)(y-yScale*spriteRects[action].height+height),(int)(x+xScale*spriteRects[action].width),y+height,spriteRects[action].x,spriteRects[action].y,spriteRects[action].x+spriteRects[action].width,spriteRects[action].y+spriteRects[action].height,io);
	}

	/**
	 * Performs Jedi actions
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
