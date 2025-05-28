package studio.retrozoni.game.objects.tiles;

import studio.retrozoni.game.Game;
import studio.retrozoni.game.items.Item;
import studio.retrozoni.game.items.Key;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.game.objects.entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Door extends Tile {

	private static BufferedImage[] sprite;
	
	public Door(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) {
			sprite = getSprite("door", Theme.Primary);
		}
		this.setSolid(true);
	}
	
	public void open() {
		this.setSolid(false);
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite[isSolid() ? 0 : 1];
	}
	
	@Override
	public void tick() {
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
		renderTile(Floor.sprite[Floor.index], g);
		renderTile(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
