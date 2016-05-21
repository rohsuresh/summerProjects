
public class Wall extends MovingImage {

	private double velX;

	/**
	 * 
	 * @param x the x coordinate of the wall
	 * @param y the y coordinate of the wall
	 * @param height the height of the wall
	 */
	public Wall(int x, int y, int height) {
		super("Wall.png", x, y, 10, height);
		velX = 0;
	}

	/**
	 * moves the wall to the left
	 */
	public void move() {
		velX = -15;
		moveByAmount((int)velX, 0);
	}
	
	/**
	 * stops the wall from moving
	 */
	public void stop() {
		velX = 0;
	}
}
