package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.Variables;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Crate extends Tile {
	
	private static int index = -1;
	
	public Crate(int id, int x, int y) {
		super(id, x, y);
		loadSprite("crate");
		if(index == -1) {
			index = Engine.RAND.nextInt(getSheet().size());
			getSheet().setIndex(index);
		}
		this.setSolid(true);
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, true);
	}
	@Override
	public void tick() {
		
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
