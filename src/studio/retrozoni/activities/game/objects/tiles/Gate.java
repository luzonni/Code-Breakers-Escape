package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.entity.Button;
import studio.retrozoni.activities.game.objects.entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Gate extends Tile {

	public Gate(int id, int x, int y) {
		super(id, x, y);
		loadSprite("reforced_door");
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
	public void tick() {
		setSolid(!open());
		getSheet().setIndex(isSolid() ? 0 : 1);
	}

	@Override
	public void render(Graphics2D g) {
		BufferedImage spriteFloor = Engine.sheetHolder.getSheet("tiles", "floot").getSprite();
		renderTile(spriteFloor, g);
		renderTile(getSprite(), g);
	}
	
	@Override
	public void dispose() {

	}

}
