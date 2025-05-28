package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.Directions;
import studio.retrozoni.activities.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

class Spit extends Entity {

	private Directions dir;

	public Spit(int x, int y, Directions dir) {
		super(-1, x, y);
		this.dir = dir;
		setSize(2*Engine.SCALE, 2*Engine.SCALE);
	}

	@Override
	public BufferedImage getSprite() {
		return null;
	}

	@Override
	public void tick() {
		if(colliding())
			Game.getLevel().getEntities().remove(this);
		if(this.dir.equals(Directions.Left))
			setX(getX() - 2*Engine.SCALE);
		else
			setX(getX() + 2*Engine.SCALE);
		Player p = Game.getPlayer();
		if(this.collidingWith(p))
			p.kill();
	}
	
	private boolean colliding() {
		double x = getMiddle().x / Tile.getSize();
		double y = getMiddle().y / Tile.getSize();
		Tile tile = Game.getLevel().getTile((int)x, (int)y);
		if(tile == null)
			return false;
		return tile.isSolid();
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Theme.Primary);
		g.fillRect((int)getX() - Game.getCam().getX(), (int)getY() - Game.getCam().getY(), getWidth(), getHeight());
	}
	
	@Override
	public void dispose() {
		
	}

}
