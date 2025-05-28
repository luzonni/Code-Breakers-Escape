package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.entity.Button;
import studio.retrozoni.activities.game.objects.entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Gate extends Tile {

private static BufferedImage[] sprite;
	
	public Gate(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) {
			sprite = getSprite("reforced_door", Theme.Primary);
		}
		this.setSolid(true);
	}
	
	private boolean open() {
		boolean has = false;
		for(Entity e : Game.getLevel().getEntities())
			if(e instanceof Button) {
				has = true;
				if(!((Button) e).isPressed())
					return false;
			}
		return has;
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite[isSolid() ? 0 : 1];
	}
	
	@Override
	public void tick() {
		setSolid(!open());
	}

	@Override
	public void render(Graphics2D g) {
		renderTile(Floor.sprite[Floor.index], g);
		renderTile(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
