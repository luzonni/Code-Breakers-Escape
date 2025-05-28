package studio.retrozoni.game.objects;

public enum Directions {

	Idle(0, 0),
	Up(0, -1),
	UpRight(1,-1),
	Right(1, 0),
	RightDown(1, 1),
	Down(0, 1),
	DownLeft(-1, 1),
	Left(-1, 0),
	LeftUp(-1, -1);
	
	final int x, y;
	
	Directions(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int[] getDir() {
		return new int[] {x, y};
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

    public double getRadians() {
		return Math.atan2(getDir()[1], getDir()[0]);
    }
}
