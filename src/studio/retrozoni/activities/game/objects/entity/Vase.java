package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Vase extends Entity {
	
	protected static BufferedImage[] sprite;
	private int durability;
	
	public Vase(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("vase", Theme.Primary);
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, true);
		setEffect(Variables.Breakable);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[durability];
	}

	@Override
	public void tick() {
		Player P = Game.getPlayer();
		Tile currentTile = Game.getLevel().getTile((int)getX()/Tile.getSize(), (int)getY()/Tile.getSize());
		if(P.getOE().nextTile() == currentTile) {
			durability = 1;
		}else if(durability == 1){
			durability = 2;
		}
		if(durability >= sprite.length-1) {
			setVar(Variables.Breakable, false);
			durability = sprite.length-1;
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
