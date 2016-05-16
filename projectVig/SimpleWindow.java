import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.*;

/**
   TODO Write a one-sentence summary of your class here.
   TODO Follow it with additional details about its purpose, what abstraction
   it represents, and how to use it.

   @author  TODO Your Name
   @version TODO Date

   Period - TODO Your Period
   Assignment - TODO Name of Assignment

   Sources - TODO list collaborators
 */
public class SimpleWindow extends JPanel implements Runnable
{
	 private Rectangle screenRect;
	private Jedi hero;
	private ArrayList<Shape> obstacles;

	public SimpleWindow () {
		super();
		setBackground(Color.WHITE);
		  screenRect = new Rectangle(0,0,800,600);
		  obstacles = new ArrayList<Shape>();
		  obstacles.add(new Rectangle(0,400,800,50));
		hero = new Jedi(380, 400, this);
		new Thread(this).start();
	}

	 public void paintComponent(Graphics g)
	  {
	    super.paintComponent(g);  // Call JPanel's paintComponent method to paint the background

		Graphics2D g2 = (Graphics2D) g;

	    int width = getWidth();
	    int height = getHeight();
	    
	    double ratioX = (double)width/800;
	    double ratioY = (double)height/600;
	    
	    AffineTransform at = g2.getTransform();
	    g2.scale(ratioX, ratioY);

	    g.setColor(new Color(205,102,29));
	    for (Shape s : obstacles) {
	    	g2.fill(s);
	    }
	    hero.draw(g2,this);
	    
	    g2.setTransform(at);

		// TODO Add any custom drawings here
	  }

	@Override
	public void run() {
		while (true) { // Modify this to allow quitting
			long startTime = System.currentTimeMillis();
			
		  	hero.act(obstacles);
		  	
		  	repaint();
		  	
		  	long waitTime = 17 - (System.currentTimeMillis()-startTime);
		  	try {
		  		if (waitTime > 0)
		  			Thread.sleep(waitTime);
		  		else
		  			Thread.yield();
		  	} catch (InterruptedException e) {}
		}
	}
	
	public class KeyHandler implements KeyListener {
		  private boolean rightKey, leftKey, upKey;
			
		  public void keyPressed(KeyEvent e) {
		  	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		  		leftKey = true;
		  		if(hero.getX() > 15) {		  			
		  			hero.walk(-1);
		  		}
		  		else {
		  			hero.walk(0);		  			
		  		}
		  	} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightKey = true;
				if(hero.getX() + hero.getWidth() < screenRect.getWidth()-15) {		  			
		  			hero.walk(1);
		  		}
		  		else {
		  			hero.walk(0);		  			
		  		}
		  	} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				upKey = true;
				hero.jump();
		  	} else if(e.getKeyCode() == KeyEvent.VK_SLASH) {
		  		hero.slash();
		  	} else if(e.getKeyCode() == KeyEvent.VK_PERIOD) {
		  		hero.block();
		  	}
		  }

		  public void keyReleased(KeyEvent e) {
		  	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		  		leftKey = false;
		  		if(rightKey)
		  			hero.walk(1);
		  		else
		  			hero.stop();
		  	} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightKey = false;
				if (leftKey)
					hero.walk(-1);
				else
					hero.stop();
		  	} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				upKey = false;
		  	} else if (e.getKeyCode() == KeyEvent.VK_PERIOD) {
				hero.stop();
		  	}
		  }

		  public void keyTyped(KeyEvent e) {

		  }
		}


	// As your program grows, you may want to...
	//   1) Move this main method into its own 'main' class
	//   2) Customize the JFrame by writing a class that extends it, then creating that type of object in your main method instead.
	//   3) Rename this class (SimpleWindow is not a good name - this class actually represents the *Panel*, not the window)
	public static void main(String[] args)
	{
		JFrame w = new JFrame("Simple Window");
		w.setBounds(100, 100, 800, 600);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SimpleWindow panel = new SimpleWindow();
		w.addKeyListener(panel.new KeyHandler());
		w.add(panel);
		w.setResizable(true);
		w.setVisible(true);
	}





}

