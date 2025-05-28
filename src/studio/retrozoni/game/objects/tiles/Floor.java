package studio.retrozoni.game.objects.tiles;

import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.game.objects.Variables;

import java.awt.image.BufferedImage;

public class Floor extends Tile {
	
	protected static BufferedImage[] sprite;
	protected static int index;
	
	public Floor(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) {
			sprite = getSprite("floor", Theme.Secondary);
			index = Engine.RAND.nextInt(sprite.length);
		}
		this.setSolid(false);
		setVar(Variables.Movable, true);
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
