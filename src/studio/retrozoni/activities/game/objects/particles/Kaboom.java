package studio.retrozoni.activities.game.objects.particles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;

import java.awt.*;

public class Kaboom extends Particle {

	private final double radians;
	private double rotate;
	private final double speed;
	private final Color c;
	
	public Kaboom(int x, int y) {
		super(x, y);
		this.radians = Engine.RAND.nextDouble()*(Math.PI*2);
		this.speed = Engine.RAND.nextDouble();
		c = Engine.RAND.nextBoolean() ? Theme.Primary : Theme.Tertiary;
	}

	@Override
	public void spawn(int x, int y) {

	}

	@Override
	public void tick() {
		dead();
		double dirX = this.getX() + Math.cos(this.radians)*speed;
		double dirY = this.getY() + Math.sin(this.radians)*speed;
		this.setX(dirX);
		this.setY(dirY);
		rotate += 0.25;
	}

	@Override
	public void render(Graphics2D g) {
		g.rotate(rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		renderParticle(c, g);
		g.rotate(-rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
	}

}
