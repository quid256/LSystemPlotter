package turtle;


public class TurtleState {
	public double x, y, angle;
	public int distTraveled;
	public TurtleState(double x, double y, double angle, int distTraveled) {
		this.x = x;
		this.y = y; 
		this.angle = angle;
		this.distTraveled = distTraveled;
	}
}
