package studio.retrozoni.game.objects.tiles;

import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.game.objects.Variables;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Crate extends Tile {
	
	private static BufferedImage[] sprite;
	private static int index;
	
	public Crate(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) {
			sprite = getSprite("crate", Theme.Primary);
			index = Engine.RAND.nextInt(sprite.length);
		}
		this.setSolid(true);
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, true);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[index];
	}

	@Override
	public void tick() {
		
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
