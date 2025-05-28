package studio.retrozoni.game.objects.tiles;

import studio.retrozoni.game.objects.Variables;

import java.awt.image.BufferedImage;

public class Air extends Tile {
	
	
	public Air(int id, int x, int y) {
		super(id, x, y);
		this.setVar(Variables.Movable, true);
		this.setSolid(false);
	}
	
	@Override
	public BufferedImage getSprite() {
		return null;
	}
	
	@Override
	public void dispose() {
		
	}
	
}
