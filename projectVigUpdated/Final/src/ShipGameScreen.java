import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

public class ShipGameScreen extends JPanel implements KeyListener, Runnable
{

	Main m;

	public static final int DRAWING_WIDTH = 800;
	public static final int DRAWING_HEIGHT = 600;

	private Ship ship;
	private Wall upperWall;
	private Wall lowerWall;
	
	private int player;

	private static int score;

	private final MovingImage background;

	boolean jumpFlag = false;
	static boolean stop = true;
	
	static boolean notOnPanel = false;

	/**
	 * Creates a ShipGameScreen object
	 * @param m represents the Main object used to switch to a different screen
	 */
	public ShipGameScreen (Main m) {
		super();
		this.m = m;
		score = 0;
		ship = new Ship(0,0);
		int randHeight = (int)(Math.random()*400);
		upperWall = new Wall(DRAWING_WIDTH*3, 0, randHeight);
		lowerWall = new Wall(DRAWING_WIDTH*3, randHeight+100, 600-randHeight);
		background = new MovingImage("Background.jpg", 0, 0, DRAWING_WIDTH, DRAWING_HEIGHT);
		player = 1;
	}

	/**
	 * Overriden method used to display the screen
	 * @param g represents an instace of the Graphics JAVA library class
	 */
	public void paintComponent(Graphics g)
	{

		background.draw(g, this);		
		upperWall.draw(g, this);
		lowerWall.draw(g, this);

		int width = getWidth();
		int height = getHeight();

		double ratioX = (double)width/DRAWING_WIDTH;
		double ratioY = (double)height/DRAWING_HEIGHT;

		Graphics2D g2 = (Graphics2D)g;
		AffineTransform at = g2.getTransform();
		g2.scale(ratioX,ratioY);

		g2.setColor(Color.YELLOW);
		g2.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g2.drawString(getScore()+"", DRAWING_WIDTH-50, 50);

		ship.draw(g,this);


		g2.setTransform(at);
	}

	/**
	 * Method invoked when a key is pressed
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && jumpFlag == false) {
			jumpFlag = true;
		}
	}

	/**
	 * Method invoked when a key is released
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			jumpFlag = false;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	/**
	 * 
	 * @return the score of the current player of the game
	 */
	public static int getScore() {
		return score;
	}

	/**
	 * Sets the score to a new score
	 * @param i the score to be set to
	 */
	public static void setScore(int i) {
		score = i;
	}

	/**
	 * Used to run the program
	 */
	public void run() {
		while(true) {
			if(stop) {
				ship.moveToLocation(0, 0);
				int randHeight = (int)(Math.random()*400);
				upperWall = new Wall(DRAWING_WIDTH, 0, randHeight);
				lowerWall = new Wall(DRAWING_WIDTH, randHeight+100, 600-randHeight);
			}
			else {
				ship.fall();
				upperWall.move();
				lowerWall.move();
				if(jumpFlag == true) {
					ship.jump();
				}
				repaint();
				try {
					Thread.sleep(20);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
				check();
			}
		}
	}

	/**
	 * Checks the ship's current position and acts accordingly
	 */
	public void check() {
		int y = ship.getY() + ship.getHeight();
		if(notOnPanel) {
			player = 1;
			reset();
		}
		if (y > DRAWING_HEIGHT+20 || 
				upperWall.isPointInImage(ship.getX()+ship.getWidth(), ship.getY()+ship.getHeight()/2) ||
				lowerWall.isPointInImage(ship.getX()+ship.getWidth(), ship.getY()+ship.getHeight()/2)) {
			if(player == 1) {
				UpgradeScreen.jediNumberOfCoins += score;
			}
			if(player == 2) {
				UpgradeScreen.sithNumberOfCoins += score;
			}
			player++;
			reset();
			if(player > 2) {
				notOnPanel = true;
				reset();
				m.changePanel("5");
			}
			else {
				notOnPanel = false;
			}
			repaint();
			stop = false;
		}
		if(upperWall.getX() <= 0) {
			score++;
			int randHeight = (int)(Math.random()*400);
			upperWall = new Wall(DRAWING_WIDTH, 0, randHeight);
			lowerWall = new Wall(DRAWING_WIDTH, randHeight+100, 600-randHeight);
		}
	}

	/**
	 * Resets the ship and walls to their original positions
	 */
	public void reset() {
		setScore(0);
		stop = true;
		ship = new Ship(0,0);
		int randHeight = (int)(Math.random()*400);
		upperWall = new Wall(DRAWING_WIDTH*3, 0, randHeight);
		lowerWall = new Wall(DRAWING_WIDTH*3, randHeight+100, 600-randHeight);
	}



}

