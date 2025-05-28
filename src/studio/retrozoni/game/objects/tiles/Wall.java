package studio.retrozoni.game.objects.tiles;

import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;

import java.awt.image.BufferedImage;

public class Wall extends Tile {
	
	protected static BufferedImage[] sprite;
	protected static int index;
	
	public Wall(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) {
			sprite = getSprite("wall", Theme.Primary);
			index = Engine.RAND.nextInt(sprite.length);
		}
		this.setSolid(true);
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite[index];
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}
	
}
