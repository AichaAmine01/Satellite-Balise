package satellite;

import announcer.Announcer;

public class Satellite {
	private int x,y;
	private int direction;
	Announcer announcer;
	
	public Satellite (int x, int y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.announcer = new Announcer();
	}
	
	public void move(int gap) {
		this.x = this.x + (direction*gap);
		announcer.announce(new SatelliteMoveEvent(this));
	}

	public void registerMoveEvent(Object o) {
		this.announcer.register(o, SatelliteMoveEvent.class);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	
}
