package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.Variables;

import java.awt.image.BufferedImage;

public class Floor extends Tile {
	
	protected static int index;
	
	public Floor(int id, int x, int y) {
		super(id, x, y);
		loadSprite("floor");
		getSheet().setIndex(index);
		this.setSolid(false);
		setVar(Variables.Movable, true);
	}
	
	@Override
	public void dispose() {

	}

}
