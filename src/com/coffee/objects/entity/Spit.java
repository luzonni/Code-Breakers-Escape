package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.activity.game.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.tiles.Tile;

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
