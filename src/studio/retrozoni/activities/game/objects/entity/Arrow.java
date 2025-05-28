package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Arrow extends Entity {
	
	private static BufferedImage sprite;
	private final double direction;
	private int speed;
	
	public Arrow(int x, int y, double direction, int speed) {
		super(0, x, y);
		this.direction = direction;
		this.speed = speed;
		if(sprite == null)
			sprite = getSprite("arrow", Theme.Primary)[0].getSubimage(0, 0, 15*Engine.SCALE, 5*Engine.SCALE);
		this.setSize(sprite.getWidth(), sprite.getHeight());
		this.setDepth(1);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite;
	}

	@Override
	public void tick() {
		if(colliding())
			kill();
		for(int i = 0; i < Game.getLevel().getEntities().size(); i++) {
			Entity e = Game.getLevel().getEntities().get(i);
			if(e.collidingWith(this) && this.speed != 0)
				if(e.getVar(Variables.Alive)) {
					e.kill();
					Game.getLevel().getEntities().remove(this);
				}
		}
		this.setX(this.getX() + (Math.cos(this.direction)*this.speed));
		this.setY(this.getY() + (Math.sin(this.direction)*this.speed));
	}
	
	private boolean colliding() {
		double x = (getMiddle().x + (Math.cos(this.direction)*this.speed)) / Tile.getSize();
		double y = (getMiddle().y + (Math.sin(this.direction)*this.speed)) / Tile.getSize();
		Tile tile = Game.getLevel().getTile((int)x, (int)y);
		if(tile == null)
			return false;
		return tile.isSolid();
	}

	@Override
	public void render(Graphics2D g) {
		g.rotate(direction, getMiddle().x - Game.getCam().getX(), getMiddle().getY() - Game.getCam().getY());
		g.drawImage(sprite, (int)getX() - getWidth()/2 - Game.getCam().getX(), (int)getY() - Game.getCam().getY(), getWidth(), getHeight(), null);
		g.rotate(-direction, getMiddle().x - Game.getCam().getX(), getMiddle().getY() - Game.getCam().getY());
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
