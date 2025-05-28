package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;

import java.awt.image.BufferedImage;

public class Wall extends Tile {
	
	protected static int index = -1;
	
	public Wall(int id, int x, int y) {
		super(id, x, y);
		loadSprite("wall");
		if(index == -1) {
			index = Engine.RAND.nextInt(getSheet().size());
			getSheet().setIndex(index);
		}
		this.setSolid(true);
	}
	
	@Override
	public void dispose() {

	}
	
}
