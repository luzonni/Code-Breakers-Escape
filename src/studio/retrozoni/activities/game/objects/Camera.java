package studio.retrozoni.activities.game.objects;

import studio.retrozoni.engine.Engine;

import java.awt.*;

public class Camera implements Runnable {

    private boolean running;
	private int X, Y;
	private Point position;
	private Point vector;
	//TODO melhorar sistema.

	public synchronized void start() {
		position = new Point();
		vector = new Point();
		running = true;
        Thread thread = new Thread(this, "Camera");
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public int getX() {
		return position.x;
	}
	
	public int getY() {
		return position.y;
	}

	public void setPosition(int x, int y) {
		this.X = x;
		this.Y = y;
		this.position.setLocation(x, y);
	}
	
	public void impact(Directions dir, int force) {
		set(dir, force);
	}
	
	private void tick() {
		moving();
	}
	
	private void set(Directions directions, int amount) {
		if(directions.equals(Directions.Up)) {
			vector.y = Math.abs(amount);
		}else if(directions.equals(Directions.Down)) {
			vector.y = Math.abs(amount) * -1;
		}else if(directions.equals(Directions.Left)) {
			vector.x = Math.abs(amount);
		}else if(directions.equals(Directions.Right)) {
			vector.x = Math.abs(amount) * -1;
		}
	}
	
	private void moving() {
		if(this.vector.x != 0) {
			this.position.x += this.vector.x;
			if(this.vector.x > 0)
				this.vector.x --;
			else if(this.vector.x < 0)
				this.vector.x ++;
		}else {
			if(this.position.x > this.X)
				this.position.x -= Math.abs(this.position.x - this.X)/2;
			else if(this.position.x < this.X)
				this.position.x += Math.abs(this.position.x - this.X)/2;
		}
		if(this.vector.y != 0) {
			this.position.y += this.vector.y;
			if(this.vector.y > 0)
				this.vector.y --;
			else if(this.vector.y < 0)
				this.vector.y ++;
		}else {
			if(this.position.y > this.Y)
				this.position.y -= Math.abs(this.position.y - this.Y)/2;
			else if(this.position.y < this.Y)
				this.position.y += Math.abs(this.position.y - this.Y)/2;
		}
	}

	@Override
	public void run() {
		//System values
		long lastTimeHZ = System.nanoTime();
		double amountOfHz = Engine.HZ;
		double ns_HZ = Engine.T / amountOfHz;
		double delta_HZ = 0;
		while(running) {
			if(Thread.interrupted()) 
				running = false;
			try {
				long nowHZ = System.nanoTime();
				delta_HZ += (nowHZ - lastTimeHZ) / ns_HZ;
				lastTimeHZ = nowHZ;
				if(delta_HZ >= 1) {
					tick();
					delta_HZ--;
				}
				Thread.sleep(1);
			}catch(Exception ignore) {
				System.out.println("ERROR!");
				System.exit(1);
			}
		}
	}
	
}
