
public class Ship extends MovingImage {

	private double velY;

	/**
	 * Creates a Ship object
	 * @param x the x coordinate of Ship
	 * @param y the y coordinate of Ship
	 */
	public Ship(int x, int y) {
		super("Ship.png",x,y,80,100);
		velY = 0;
	}

	/**
	 * Causes the Ship's y position to increase
	 */
	public void jump() {
		if(getY() > 10) {
			velY = -10;
			moveByAmount(0, (int)velY);
		}
	}

	/**
	 * Causes the Ship's y position to decrease
	 */
	public void fall() {
		if(velY < 15) {
			velY = velY + 1;
		}
		moveByAmount(0, (int)velY);
	}

}

