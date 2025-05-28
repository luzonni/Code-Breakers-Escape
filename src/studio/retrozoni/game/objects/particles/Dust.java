package studio.retrozoni.game.objects.particles;

import studio.retrozoni.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Dust extends Particle {
	
	private static BufferedImage[] sprite;
	private int index;
	private double rotate;
	private double radians;
	private double speed;

	public Dust(int x, int y, double radians) {
		super(x, y);
		if(sprite == null) {
			sprite = new BufferedImage[3];
			sprite[0] = getSprite("dust", Theme.Primary)[0].getSubimage(0, 0, 3*Engine.SCALE, 3*Engine.SCALE);
			sprite[1] = getSprite("dust", Theme.Secondary)[0].getSubimage(0, 0, 3*Engine.SCALE, 3*Engine.SCALE);
			sprite[2] = getSprite("dust", Theme.Tertiary)[0].getSubimage(0, 0, 3*Engine.SCALE, 3*Engine.SCALE);
		}
		this.radians = radians;
		this.speed = Engine.RAND.nextDouble();
		this.index = Engine.RAND.nextInt(sprite.length);
		setSize(sprite[index].getWidth(), sprite[index].getHeight());
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
		g.rotate(rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		renderParticle(sprite[index], g);
		g.rotate(-rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());

	}

}
