package com.coffee.objects;

public enum Directions {
	
	Up(0, -1), Right(1, 0), Down(0, 1), Left(-1, 0), Idle(0, 0);
	
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
