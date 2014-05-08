
public class Position {
	private double posX;
	private double posY;
	private long time;
	
	public Position( double posX, double posY) {
		this.posX = posX;
		this.posY = posY;
		time = System.currentTimeMillis();
	}
}
