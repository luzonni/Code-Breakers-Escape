package studio.retrozoni.activities.game.objects.particles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Dust extends Particle {
	
	private int index;
	private double rotate;
	private double radians;
	private double speed;

	public Dust(int x, int y, double radians) {
		super(x, y);
		loadSprite("dust");
		this.radians = radians;
		this.speed = Engine.RAND.nextDouble();
		getSheet().setState(Engine.RAND.nextInt(getSheet().size()));
		setSize(getSprite().getWidth(), getSprite().getHeight());
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
		rotate += 0.15;
	}

	@Override
	public void render(Graphics2D g) {
		//TODO mudar method de rotação!
		g.rotate(rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		renderParticle(getSprite(), g);
		g.rotate(-rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());

	}

}
