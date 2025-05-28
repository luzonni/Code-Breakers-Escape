package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.activities.game.items.Item;
import studio.retrozoni.activities.game.items.Key;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Door extends Tile {
	
	public Door(int id, int x, int y) {
		super(id, x, y);
		loadSprite("door");
		this.setSolid(true);
	}
	
	public void open() {
		this.setSolid(false);
	}

	@Override
	public void tick() {
		getSheet().setIndex(isSolid() ? 0 : 1);
		Player p = Game.getPlayer();
		if(this.isSolid() && p.getOE().nextTile() == this) 
			for(Item item : Game.getInventory().getList())
				if(item instanceof Key) {
					Game.getInventory().remove(item);
					open();
				}
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
