import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.*;

public class FightingGameScreen extends JPanel implements Runnable
{
	Main m;
	
	private Rectangle screenRect;
	private Jedi hero;
	private Sith villan;
	private ArrayList<Shape> obstacles;

	private int count = 0;

	public FightingGameScreen (Main m) {
		super();
		this.m = m;
		
		setBackground(Color.WHITE);
		screenRect = new Rectangle(0,0,800,600);
		obstacles = new ArrayList<Shape>();
		obstacles.add(new Rectangle(10, 275, 150, 50));
		obstacles.add(new Rectangle(640, 275, 150, 50));
		obstacles.add(new Rectangle(220,400,360,50));
		obstacles.add(new Rectangle(-1, 0, 1, 600));
		obstacles.add(new Rectangle(801, 0, 1, 600));
		obstacles.add(new Rectangle(0, 550, 800, 50));
		hero = new Jedi(380, 400, 50, 80, this);
		villan = new Sith(200, 400, 50, 70, this);
		new Thread(this).start();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g.drawString("Jedi Health: " + Jedi.health, 10, 20);
		g.drawString("Sith Health: " + Sith.health, MenuScreen.DRAWING_WIDTH-115, 20);

		if(Sith.health <= 0) {
			g.drawString("Jedi Wins!", 100, 100);
		}
		else if(Jedi.health <= 0) {
			g.drawString("Sith Wins!", 100, 100);
		}

		Graphics2D g2 = (Graphics2D)g;

		int width = getWidth();
		int height = getHeight();

		double ratioX = (double)width/800;
		double ratioY = (double)height/600;

		AffineTransform at = g2.getTransform();
		g2.scale(ratioX, ratioY);

		g.setColor(Color.DARK_GRAY);
		if(count < 1) {
			for (int i = 0; i < obstacles.size(); i++) {
				g2.fill(obstacles.get(i));
			}
		}
		else {
			for (int i = 0; i < obstacles.size()-1; i++) {
				g2.fill(obstacles.get(i));
			}
		}

		hero.draw(g2,this);
		villan.draw(g2, this);

		g2.setTransform(at);

	}

	@Override
	public void run() {
		while (true) {
			long startTime = System.currentTimeMillis();
			if(count < 1) {
				count++;
			}
			else {
				obstacles.remove(obstacles.size()-1);
			}
			obstacles.add(new Rectangle(villan.getX(), villan.getY(), villan.getWidth(), villan.getHeight()));
			hero.act(obstacles);
			obstacles.remove(obstacles.size()-1);
			obstacles.add(new Rectangle(hero.getX(), hero.getY(), hero.getWidth(), hero.getHeight()));
			villan.act(obstacles);

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

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if(hero.getX() > 15) {		  			
					hero.walk(-1);
				}
				else {
					hero.walk(0);		  			
				}
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if(hero.getX() + hero.getWidth() < screenRect.getWidth()-15) {		  			
					hero.walk(1);
				}
				else {
					hero.walk(0);		  			
				}
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				hero.jump();
			} else if(e.getKeyCode() == KeyEvent.VK_SLASH) {
				hero.slash();
				if((hero.isFacingRight() && (Math.abs((villan.getY()-10)-hero.getY()) < 10) && villan.getX()-(hero.getX()+hero.getWidth()) < 5 && hero.getX() < villan.getX())
						|| (!hero.isFacingRight() && ((Math.abs(villan.getY()-10)-hero.getY()) < 10) && hero.getX()-(villan.getX()+villan.getWidth()) < 15) && hero.getX() > villan.getX()) {
					if(!villan.isBlocking()) {
						Sith.health -= Jedi.damagePerStrike;
					}
					else {
						Sith.health -= Jedi.damagePerStrike/2;
					}
				}
			} else if(e.getKeyCode() == KeyEvent.VK_PERIOD) {
				hero.block();
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				if(villan.getX() > 15) {		  			
					villan.walk(-1);
				}
				else {
					villan.walk(0);		  			
				}
			} else if (e.getKeyCode() == KeyEvent.VK_D) {
				if(villan.getX() + villan.getWidth() < screenRect.getWidth()-15) {		  			
					villan.walk(1);
				}
				else {
					villan.walk(0);		  			
				}
			} else if (e.getKeyCode() == KeyEvent.VK_W) {
				villan.jump();
			} else if(e.getKeyCode() == KeyEvent.VK_X) {
				villan.slash();

				if((villan.isFacingRight() && (Math.abs(hero.getY()-(villan.getY()-10)) < 10) && hero.getX()-(villan.getX()+villan.getWidth()) < 5 && villan.getX() < hero.getX())
						|| (!villan.isFacingRight() && (Math.abs(hero.getY()-(villan.getY()-10)) < 10) && villan.getX()-(hero.getX()+hero.getWidth()) < 15) && villan.getX() > hero.getX()) {
					if(!hero.isBlocking()) {
						Jedi.health -= Sith.damagePerStrike;
					}
					else {
						Jedi.health -= Sith.damagePerStrike/2;
					}
				}
			} else if(e.getKeyCode() == KeyEvent.VK_C) {
				villan.block();
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				hero.stop();
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				hero.stop();
			} else if (e.getKeyCode() == KeyEvent.VK_PERIOD) {
				hero.stop();
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				villan.stop();
			} else if (e.getKeyCode() == KeyEvent.VK_D) {
				villan.stop();
			} else if (e.getKeyCode() == KeyEvent.VK_C) {
				villan.stop();
			}
		}

		public void keyTyped(KeyEvent e) {

		}
	}
}