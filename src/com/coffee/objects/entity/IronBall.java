package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.activity.Game;
import com.coffee.objects.Variables;
import com.coffee.objects.particles.Boom;
import com.coffee.objects.tiles.Tile;

class IronBall extends Entity {

	private final double radians;
	private double rotate;
	private static BufferedImage sprite;
	private final int speed;

	public IronBall(int x, int y, double radians, int speed) {
		super(0, x - 5*Engine.SCALE, y - 5*Engine.SCALE);
		this.setSize(10*Engine.SCALE, 10*Engine.SCALE);
		this.radians = radians;
		this.speed = speed;
		if(sprite == null) 
			sprite = getSprite("IronBall", Theme.Color_Primary)[0];
		for(int i = 0; i < 30; i++)
			Game.getLevel().addParticle(new Boom(getMiddle().x, getMiddle().y, radians - Math.PI/2 + Engine.RAND.nextDouble()*Math.PI));
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite;
	}

	@Override
	public void tick() {
		if(colliding())
			disappear();
		List<Entity> entities = Game.getLevel().getEntities();
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(e == this || (e instanceof Cannon))
				continue;
			if(e.collidingWith(this) && e.getVar(Variables.Alive)) {
				e.kill();
				disappear();
			}
		}
		this.setX(this.getX() + Math.cos(this.radians)*this.speed);
		this.setY(this.getY() + Math.sin(this.radians)*this.speed);
		rotate += 0.25;
	}
	
	private boolean colliding() {
		int x = getMiddle().x / Tile.getSize();
		int y = getMiddle().y / Tile.getSize();
		Tile tile = Game.getLevel().getTile(x, y);
		if(tile == null)
			return false;
		return tile.isSolid();
	}
	
	@Override
	public void render(Graphics2D g) {
		g.rotate(rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		renderEntity(sprite, g);
		g.rotate(-rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
