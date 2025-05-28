package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.particles.Dust;
import studio.retrozoni.activities.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Miniboll extends Entity {

	private double radians;
	private int count;
	
	public Miniboll(int x, int y, double radians) {
		super(-1, x, y);
		this.radians = radians;
		this.setSize(2*Engine.SCALE, 2*Engine.SCALE);
	}

	@Override
	public BufferedImage getSprite() {
		return null;
	}

	@Override
	public void tick() {
		this.setX(getX() + Engine.SCALE *Math.cos(radians)*2);
		this.setY(getY() + Engine.SCALE *Math.sin(radians)*2);
		int x_next = (int)(getMiddle().x + Engine.SCALE *Math.cos(radians)*2) / Tile.getSize();
		int y_next = (int)(getMiddle().y + Engine.SCALE *Math.sin(radians)*2) / Tile.getSize();
		int x = (int)(getMiddle().x) / Tile.getSize();
		int y = (int)(getMiddle().y) / Tile.getSize();
		Tile vertinal_tile = Game.getLevel().getTile(x_next, y);
		Tile horizontal_tile = Game.getLevel().getTile(x, y_next);
		double ax = Math.cos(radians);
		double ay = Math.sin(radians);
		if(vertinal_tile == null || vertinal_tile.isSolid()) {
			ax *= -1;
			this.radians = Math.atan2(ay, ax);
			this.count++;
		}
		if(horizontal_tile == null || horizontal_tile.isSolid()) {
			ay *= -1;
			this.radians = Math.atan2(ay, ax);
			this.count++;
		}
		if(Engine.RAND.nextInt(100) < 20 && count < 2)
			Game.getLevel().addParticle(new Dust(getMiddle().x, getMiddle().y, this.radians-Math.PI*2));
		for(int i = 0; i < Game.getLevel().getEntities().size(); i++) {
			Entity entity = Game.getLevel().getEntities().get(i);
			if(entity.getVar(Variables.Alive) && entity.collidingWith(this)) {
				entity.kill();
				disappear();
			}
		}
		if(count > 3) {
			disappear();
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor((count % 2 == 0) ? Theme.Primary : Theme.Tertiary);
		g.fillRect((int)getX() - Game.getCam().getX(), (int)getY() - Game.getCam().getY(), getWidth(), getHeight());
	}
	
	@Override
	public void dispose() {
		
	}

}
