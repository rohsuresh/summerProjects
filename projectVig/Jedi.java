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

	// The bottom left corner of Test.
	// We are drawing by the bottom left because the sprites have different heights, and we want different actions to change the y coordinate 
	// of his head rather than his feet.
	private int x, y;
	private int width, height;
	
	private double jumpStrength = 50;

	private double vX, vY;

	private int action;

	private boolean isFacingRight = false;
	private boolean isOnGround = true;

	private JComponent surface; // for repainting when we make changes to his image

	private Timer actionTimer;

	public Jedi(int x, int y, JComponent surface) {

		if(isFacingRight) {
			sprites = new ImageIcon("AnakinSkywalker.png").getImage();

			spriteRects = new Rectangle[6]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(9, 90, 45, 70);
			/*strike*/ spriteRects[1] = new Rectangle(119, 442, 42, 70);
			/*block*/ spriteRects[2] = new Rectangle(55, 378, 40, 70);
			/*runRight1*/ spriteRects[3] = new Rectangle(8, 160, 40, 62);
			/*runRight2*/ spriteRects[4] = new Rectangle(86, 160, 38, 64);
			/*jump*/ spriteRects[5] = new Rectangle(154, 224, 30, 70);
		}
		else {
			sprites = new ImageIcon("AnakinSkywalkerLeft.png").getImage();

			spriteRects = new Rectangle[6]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(232, 90, 45, 70);
			/*strike*/ spriteRects[1] = new Rectangle(128, 442, 42, 70);
			/*block*/ spriteRects[2] = new Rectangle(150, 378, 40, 70);
			/*runLeft1*/ spriteRects[3] = new Rectangle(82, 160, 40, 62);
			/*runLeft2*/ spriteRects[4] = new Rectangle(162, 160, 38, 63);
			/*jump*/ spriteRects[5] = new Rectangle(96, 224, 30, 70);
		}

		this.x = x;
		this.y = y;
		width =  spriteRects[0].width;   // Default width and height is the width and height of the first image loaded from the sprite sheet
		height =  spriteRects[0].height;

		vX = 0;
		vY = 0;

		width *= 1;  // We scale the size of Test up x4 for visibility
		height *= 1;

		action = 0;

		this.surface = surface;
		actionTimer = new Timer(ACTION_TIMEOUT,this);
		actionTimer.setRepeats(false);
	}

	public void changeDirection() {
		if(isFacingRight) {
			sprites = new ImageIcon("AnakinSkywalkerLeft.png").getImage();

			spriteRects = new Rectangle[6]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(232, 90, 45, 70);
			/*strike*/ spriteRects[1] = new Rectangle(128, 442, 42, 70);
			/*block*/ spriteRects[2] = new Rectangle(150, 378, 40, 70);
			/*runLeft1*/ spriteRects[3] = new Rectangle(82, 160, 40, 62);
			/*runLeft2*/ spriteRects[4] = new Rectangle(162, 160, 38, 64);
			/*jump*/ spriteRects[5] = new Rectangle(96, 224, 30, 70);
			isFacingRight = false;
		}
		else {
			sprites = new ImageIcon("AnakinSkywalker.png").getImage();

			spriteRects = new Rectangle[6]; // Coordinates of each action within the sprite sheet image
			/*stand*/ spriteRects[0] = new Rectangle(9, 90, 45, 70);
			/*strike*/ spriteRects[1] = new Rectangle(119, 442, 42, 70);
			/*block*/ spriteRects[2] = new Rectangle(55, 378, 40, 70);
			/*runRight1*/ spriteRects[3] = new Rectangle(8, 160, 40, 62);
			/*runRight2*/ spriteRects[4] = new Rectangle(86, 160, 38, 64);
			/*jump*/ spriteRects[5] = new Rectangle(154, 224, 30, 70);
			isFacingRight = true;
		}
	}

	/*
	 * Test standing, doing nothing.
	 */
	public void stand() {
		action = 0;
	}

	/*
	 * Test slashes his sword.
	 */
	public void slash() {
		action = 1;
		actionTimer.restart();
	}

	/*
	 * Test blocks with his shield.
	 */
	public void block() {
		action = 2;
		actionTimer.restart();
	}

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
	
	public void stop() {
		action = 0;
		vX = 0;
	}

	public void jump() {
		if(isOnGround) {
			action = 5;
			isOnGround = false;
			vY -= jumpStrength;
			actionTimer.restart();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	/*
	 * Draw Test using the correct sprite.
	 */
	public void draw(Graphics2D g2, ImageObserver io) {
		AffineTransform at = g2.getTransform();
		double xScale = (double)width / spriteRects[0].width;
		double yScale = (double)height / spriteRects[0].height;
		g2.drawImage(sprites, x,(int)(y-yScale*spriteRects[action].height),(int)(x+xScale*spriteRects[action].width),y,spriteRects[action].x,spriteRects[action].y,spriteRects[action].x+spriteRects[action].width,spriteRects[action].y+spriteRects[action].height,io);
	}

	public void act(ArrayList<Shape> obstacles) {				
		vY += 5;

		boolean hitGround = false;

		for(Shape s : obstacles) {
			Rectangle2D.Double motionY = new Rectangle2D.Double((vX > 0 ? x : x+vX), y, width, vY);
			if(s.intersects(motionY)) {
				vY = 0;
				hitGround = true;
			}
		}

		isOnGround = hitGround;
		x += vX;
		y += vY;
		//System.out.println(vY);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(action == 1) {
			stand();
			surface.repaint();
		}
		else if(action == 5) {
			action = 0;
		}
	}




}
